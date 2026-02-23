import Foundation
import CoreModelsSDK
import RSocketSDK // בשביל JSONCoding (Encoder/Decoder עם ISO-8601) וגם RSocketError לאחידות שגיאות

public protocol MessageManaging {
    func sendMessage(_ message: Message) async throws -> Message
    func sendFileMessage(_ request: FileMessageRequest) async throws
}

private enum MessageHTTP {
    static let send        = "messages/send"   // POST -> Message
    static let sendFile    = "chats/send-file" // POST -> 204 (ממפה ל-route chat.sendfile)
}

public final class MessageManager: MessageManaging {

    private let session: URLSession
    /// חייב להצביע ל־ http(s)://<host>:8081/api
    private let apiBase: URL

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

    public func sendMessage(_ message: Message) async throws -> Message {
        try await post(MessageHTTP.send, body: message)
    }

    public func sendFileMessage(_ request: FileMessageRequest) async throws {
        try await postNoContent(MessageHTTP.sendFile, body: request)
    }

    // MARK: - HTTP helpers (בדומה ל־Chat/Project)

    private func makeURL(_ path: String) -> URL {
        apiBase.appendingPathComponent(path)
    }

    private func post<In: Encodable, Out: Decodable>(_ path: String, body: In) async throws -> Out {
        var req = URLRequest(url: makeURL(path))
        req.httpMethod = "POST"
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        req.setValue("application/json", forHTTPHeaderField: "Accept")
        req.httpBody = try JSONCoding.encoder.encode(body)
        let (data, resp) = try await session.data(for: req)
        try throwIfBad(resp, data: data)
        return try JSONCoding.decoder.decode(Out.self, from: data)
    }

    private func postNoContent<In: Encodable>(_ path: String, body: In) async throws {
        var req = URLRequest(url: makeURL(path))
        req.httpMethod = "POST"
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        req.httpBody = try JSONCoding.encoder.encode(body)
        let (_, resp) = try await session.data(for: req)
        try throwIfBad(resp, data: Data())
    }

    private func throwIfBad(_ resp: URLResponse, data: Data) throws {
        guard let http = resp as? HTTPURLResponse, !(200..<300).contains(http.statusCode) else { return }
        // שומר על טיפוס שגיאה אחיד באפליקציה
        throw RSocketError.requestFailed(status: http.statusCode, body: data)
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
