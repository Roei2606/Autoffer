import Foundation

// MARK: - Public API
public protocol RSocketClient: Sendable {
    func requestResponse<T: Encodable>(route: String, data: T) async throws -> Data
    func requestStream<T: Encodable>(route: String, data: T) async throws -> AsyncThrowingStream<Data, Error>
    var isDisposed: Bool { get }
}

// MARK: - HTTP + SSE Client
public final class DefaultRSocketClient: RSocketClient, @unchecked Sendable {
    private let session: URLSession
    private let base: URL
    private let encoder = JSONEncoder()

    // טוען את הכתובת מתוך Info.plist → GatewayBaseURL
    public init(session: URLSession = .shared) {
        guard let baseString = Bundle.main.object(forInfoDictionaryKey: "GatewayBaseURL") as? String,
              let baseURL = URL(string: baseString + "/api") else {
            fatalError("❌ GatewayBaseURL missing or invalid in Info.plist")
        }
        self.base = baseURL
        self.session = session
    }

    public var isDisposed: Bool { false }

    // --------------------------
    // MARK: - requestResponse
    // --------------------------
    public func requestResponse<T: Encodable>(route: String, data: T) async throws -> Data {
        let body = try encoder.encode(data)
        let (method, url, httpBody) = try mapRouteToHTTPRequest(route: route, encodedBody: body)

        var req = URLRequest(url: url)
        req.httpMethod = method
        if let httpBody { req.httpBody = httpBody }
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        req.setValue("application/json", forHTTPHeaderField: "Accept")

        let (responseData, response) = try await session.data(for: req)
        try Self.throwIfBad(response: response, data: responseData)
        return responseData
    }

    // --------------------------
    // MARK: - requestStream (SSE)
    // --------------------------
    public func requestStream<T: Encodable>(route: String, data: T) async throws -> AsyncThrowingStream<Data, Error> {
        let body = try encoder.encode(data)
        let (url, headers) = try mapRouteToSSE(route: route, encodedBody: body)

        var req = URLRequest(url: url)
        req.httpMethod = "GET"
        req.setValue("text/event-stream", forHTTPHeaderField: "Accept")
        for (k, v) in headers { req.setValue(v, forHTTPHeaderField: k) }

        let (bytes, response) = try await session.bytes(for: req)
        try Self.throwIfBad(response: response, data: Data())

        return AsyncThrowingStream { continuation in
            Task {
                do {
                    var buffer = ""
                    for try await line in bytes.lines {
                        if line.hasPrefix("data:") {
                            let payload = String(line.dropFirst(5)).trimmingCharacters(in: .whitespaces)
                            if let d = payload.data(using: .utf8) {
                                continuation.yield(d)
                            }
                        } else if line.isEmpty {
                            buffer.removeAll(keepingCapacity: true)
                        }
                    }
                    continuation.finish()
                } catch {
                    continuation.finish(throwing: error)
                }
            }
        }
    }

    // --------------------------
    // MARK: - Helpers
    // --------------------------
    private static func throwIfBad(response: URLResponse, data: Data) throws {
        guard let http = response as? HTTPURLResponse else { return }
        guard (200..<300).contains(http.statusCode) else {
            throw HTTPTransportError.requestFailed(status: http.statusCode, body: data)
        }
    }

    private func mapRouteToHTTPRequest(route: String, encodedBody: Data) throws -> (method: String, url: URL, body: Data?) {
        switch route {
        case "users.login":
            return ("POST", base.appendingPathComponent("users/login"), encodedBody)
        case "users.register":
            return ("POST", base.appendingPathComponent("users/register"), encodedBody)
        case "users.getAll":
            return ("GET", base.appendingPathComponent("users"), nil)
        case "users.getById":
            let id = extractString(key: "userId", from: encodedBody)
            guard let id else { throw HTTPTransportError.mappingFailed("users.getById expects {userId}") }
            return ("GET", base.appendingPathComponent("users/\(id)"), nil)
        case "users.resetPassword":
            return ("POST", base.appendingPathComponent("users/reset-password"), encodedBody)
        case "users.getByType":
            let type = extractString(key: "type", from: encodedBody)
            guard let type else { throw HTTPTransportError.mappingFailed("users.getByType expects {type}") }
            return ("GET", base.appendingPathComponent("users/type/\(type)"), nil)

        default:
            throw HTTPTransportError.mappingFailed("No HTTP mapping for route \(route)")
        }
    }

    private func mapRouteToSSE(route: String, encodedBody: Data) throws -> (url: URL, headers: [String:String]) {
        switch route {
        case "messages.stream":
            guard let chatId = extractString(key: "chatId", from: encodedBody) else {
                throw HTTPTransportError.mappingFailed("messages.stream expects {chatId}")
            }
            var comps = URLComponents(url: base.appendingPathComponent("messages/stream"), resolvingAgainstBaseURL: false)!
            comps.queryItems = [URLQueryItem(name: "chatId", value: chatId)]
            return (comps.url!, [:])

        default:
            throw HTTPTransportError.mappingFailed("No SSE mapping for route \(route)")
        }
    }

    private func extractString(key: String, from data: Data) -> String? {
        guard let obj = try? JSONSerialization.jsonObject(with: data) as? [String: Any] else { return nil }
        if let v = obj[key] as? String { return v }
        if let nested = obj["request"] as? [String: Any], let v = nested[key] as? String { return v }
        return nil
    }
}

enum HTTPTransportError: Error, LocalizedError {
    case requestFailed(status: Int, body: Data)
    case mappingFailed(String)

    var errorDescription: String? {
        switch self {
        case .requestFailed(let s, let body):
            return "HTTP \(s): \(String(data: body, encoding: .utf8) ?? "")"
        case .mappingFailed(let m):
            return "Mapping error: \(m)"
        }
    }
}
