import Foundation
import CoreModelsSDK
import RSocketSDK // בשביל JSONCoding (encoder/decoder ISO-8601)

@MainActor
public protocol ChatManaging: AnyObject {
    func getUserChats(userId: String, page: Int, size: Int) async throws -> [Chat]
    func getChatMessages(chatId: String, page: Int, size: Int) async throws -> [Message]
    func getOrCreateChat(currentUserId: String, otherUserId: String) async throws -> Chat
    func streamMessages(chatId: String) async throws -> AsyncThrowingStream<Message, Error>
    func getUnreadCount(chatId: String, userId: String) async throws -> Int
    func hasChats(userId: String) async -> Bool
    func disposeMessageStream()
}

private enum ChatHTTP {
    static let base                    = "chats"
    static let getOrCreate             = "chats/get-or-create"    // POST ChatRequest -> Chat
    static let all                     = "chats/all"              // POST UserChatsRequest -> [Chat]
    static func messages(_ id: String) -> String { "chats/\(id)/messages" } // GET ?page=&size=
    static func stream(_ id: String)   -> String { "chats/\(id)/stream" }   // GET (SSE)
    static let unreadCount             = "chats/unread-count"     // POST UnreadCountRequest -> UnreadCountResponse
    static let hasAny                  = "chats/has-any"          // GET ?clientId=
}

@MainActor
public final class ChatManager: ChatManaging {

    private let session: URLSession
    /// חייב להצביע ל־ http(s)://<host>:8081/api
    private let apiBase: URL
    private var messageStreamTask: Task<Void, Never>?

    // DI init
    public init(apiBase: URL, session: URLSession = .shared) {
        self.apiBase = apiBase
        self.session = session
    }

    /// נוחות: קורא מ־Info.plist (GatewayBaseURL) או ברירת מחדל DEV
    public convenience init() {
        self.init(apiBase: Self.defaultAPIBase(), session: .shared)
    }

    // MARK: - API

    public func getUserChats(userId: String, page: Int, size: Int) async throws -> [Chat] {
        let req = UserChatsRequest(userId: userId, page: page, size: size)
        return try await post(ChatHTTP.all, body: req)
    }

    public func getChatMessages(chatId: String, page: Int, size: Int) async throws -> [Message] {
        try await get(ChatHTTP.messages(chatId),
                      query: [URLQueryItem(name: "page", value: String(page)),
                              URLQueryItem(name: "size", value: String(size))])
    }

    public func getOrCreateChat(currentUserId: String, otherUserId: String) async throws -> Chat {
        let req = ChatRequest(currentUserId: currentUserId, otherUserId: otherUserId)
        return try await post(ChatHTTP.getOrCreate, body: req)
    }

    public func streamMessages(chatId: String) async throws -> AsyncThrowingStream<Message, Error> {
        let url = makeURL(ChatHTTP.stream(chatId))
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.setValue("text/event-stream", forHTTPHeaderField: "Accept")

        let (bytes, response) = try await session.bytes(for: request)
        try throwIfBad(response, data: Data())

        return AsyncThrowingStream { cont in
            self.messageStreamTask?.cancel()
            self.messageStreamTask = Task {
                do {
                    for try await line in bytes.lines {
                        guard line.hasPrefix("data:") else { continue }
                        let json = String(line.dropFirst(5)).trimmingCharacters(in: .whitespaces)
                        if let d = json.data(using: .utf8) {
                            let msg = try JSONCoding.decoder.decode(Message.self, from: d)
                            cont.yield(msg)
                        }
                    }
                    cont.finish()
                } catch {
                    cont.finish(throwing: error)
                }
            }
        }
    }

    public func getUnreadCount(chatId: String, userId: String) async throws -> Int {
        let req = UnreadCountRequest(chatId: chatId, userId: userId)
        let resp: UnreadCountResponse = try await post(ChatHTTP.unreadCount, body: req)
        return resp.count
    }

    public func hasChats(userId: String) async -> Bool {
        do {
            let val: Bool = try await get(ChatHTTP.hasAny,
                                          query: [URLQueryItem(name: "clientId", value: userId)])
            return val
        } catch { return false }
    }

    public func disposeMessageStream() {
        messageStreamTask?.cancel()
        messageStreamTask = nil
    }

    // MARK: - HTTP helpers

    private func makeURL(_ path: String, query: [URLQueryItem] = []) -> URL {
        var comps = URLComponents(url: apiBase.appendingPathComponent(path), resolvingAgainstBaseURL: false)!
        comps.queryItems = query.isEmpty ? nil : query
        return comps.url!
    }

    private func get<T: Decodable>(_ path: String, query: [URLQueryItem] = []) async throws -> T {
        var req = URLRequest(url: makeURL(path, query: query))
        req.httpMethod = "GET"
        req.setValue("application/json", forHTTPHeaderField: "Accept")
        let (data, resp) = try await session.data(for: req)
        try throwIfBad(resp, data: data)
        return try JSONCoding.decoder.decode(T.self, from: data)
    }

    private func post<In: Encodable, Out: Decodable>(_ path: String, body: In) async throws -> Out {
        var req = URLRequest(url: makeURL(path))
        req.httpMethod = "POST"
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        req.httpBody = try JSONCoding.encoder.encode(body)
        let (data, resp) = try await session.data(for: req)
        try throwIfBad(resp, data: data)
        return try JSONCoding.decoder.decode(Out.self, from: data)
    }

    private func throwIfBad(_ resp: URLResponse, data: Data) throws {
        guard let http = resp as? HTTPURLResponse, !(200..<300).contains(http.statusCode) else { return }
        throw RSocketError.requestFailed(status: http.statusCode, body: data) // שומר סגנון שגיאה אחיד באפליקציה
    }

    private static func defaultAPIBase() -> URL {
        if let s = Bundle.main.object(forInfoDictionaryKey: "GatewayBaseURL") as? String,
           let root = URL(string: s.trimmingCharacters(in: .whitespacesAndNewlines)),
           !s.isEmpty {
            if root.path.hasSuffix("/api") { return root }
            return root.appendingPathComponent("api")
        }
        #if DEBUG
        #if targetEnvironment(simulator)
        return URL(string: "http://127.0.0.1:8081/api")!
        #else
        return URL(string: "http://192.168.1.123:8081/api")! // עדכן ל-IP של המאק
        #endif
        #else
        return URL(string: "https://your-gateway.example.com/api")!
        #endif
    }
}
