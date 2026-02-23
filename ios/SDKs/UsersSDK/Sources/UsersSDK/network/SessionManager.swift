import Foundation
import RSocketSDK        // בשביל JSONCoding (ISO-8601)
import CoreModelsSDK     // User, UserType

// אם CoreModelsSDK לא מסומן Sendable, זה מונע אזהרות כשמחזירים User מחוץ ל-actor:
extension User: @unchecked Sendable {}
extension UserType: @unchecked Sendable {}

public enum UsersError: Error, LocalizedError {
    case requestFailed(status: Int, body: Data)
    case userNotLoggedIn
    case missingProfileType

    public var errorDescription: String? {
        switch self {
        case .requestFailed(let s, let body):
            return "HTTP \(s): \(String(data: body, encoding: .utf8) ?? "")"
        case .userNotLoggedIn:
            return "User is not logged in"
        case .missingProfileType:
            return "User profile type is missing"
        }
    }
}

public actor SessionManager {
    public nonisolated static let shared = SessionManager()

    // ---- רשת (כמו שהיה) ----
    private let session: URLSession
    private var baseURL: URL  // חייב להיות http(s)://.../api

    // ---- מצב משתמש נוכחי (מקביל לאנדרואיד) ----
    private var _currentUserId: String?
    private var _currentUser: User?

    // קריאה מבחוץ דורשת await (actor isolation)
    public var currentUserId: String? { _currentUserId }
    public var currentUser: User? { _currentUser }

    public init(session: URLSession = .shared, baseURL: URL? = nil) {
        self.session = session
        self.baseURL = baseURL ?? Self.defaultBaseURL()
    }

    // ---- API כמו באנדרואיד ----
    public func setCurrentUserId(_ id: String?) {
        self._currentUserId = id
    }

    /// כמו setCurrentUser(User user) באנדרואיד – מעדכן גם את ה-ID
    public func setCurrentUser(_ user: User?) {
        self._currentUser = user
        self._currentUserId = user?.id
    }

    /// כמו getCurrentUserProfileType() באנדרואיד – זורק אם אין משתמש/סוג
    public func getCurrentUserProfileType() throws -> UserType {
        guard let user = _currentUser else { throw UsersError.userNotLoggedIn }
        guard let t = user.profileType else { throw UsersError.missingProfileType }
        return t
    }

    /// ניקוי מצב (ל־Logout)
    public func clear() {
        _currentUser = nil
        _currentUserId = nil
    }

    // ---- קונפיג של בסיס ה-Gateway ----
    public func updateBaseURL(_ url: URL) {
        // וודא שסוגר ב-/api לטובת העוזרים למטה
        if url.path.hasSuffix("/api") {
            self.baseURL = url
        } else {
            self.baseURL = url.appendingPathComponent("api")
        }
    }

    // ---- עזרי HTTP (כמו שהיו) ----
    public func get<T: Decodable>(_ path: String, query: [URLQueryItem] = []) async throws -> T {
        var comps = URLComponents(url: baseURL.appendingPathComponent(path), resolvingAgainstBaseURL: false)!
        if !query.isEmpty { comps.queryItems = query }
        var req = URLRequest(url: comps.url!)
        req.httpMethod = "GET"
        req.setValue("application/json", forHTTPHeaderField: "Accept")
        let (data, resp) = try await session.data(for: req)
        try Self.throwIfBad(resp: resp, data: data)
        return try JSONCoding.decoder.decode(T.self, from: data)
    }

    public func post<In: Encodable, Out: Decodable>(_ path: String, body: In) async throws -> Out {
        var req = URLRequest(url: baseURL.appendingPathComponent(path))
        req.httpMethod = "POST"
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        req.setValue("application/json", forHTTPHeaderField: "Accept")
        req.httpBody = try JSONCoding.encoder.encode(body)
        let (data, resp) = try await session.data(for: req)
        try Self.throwIfBad(resp: resp, data: data)
        return try JSONCoding.decoder.decode(Out.self, from: data)
    }

    public func postNoContent<In: Encodable>(_ path: String, body: In) async throws {
        var req = URLRequest(url: baseURL.appendingPathComponent(path))
        req.httpMethod = "POST"
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        req.httpBody = try JSONCoding.encoder.encode(body)
        let (_, resp) = try await session.data(for: req)
        try Self.throwIfBad(resp: resp, data: Data())
    }

    // ---- helpers ----
    private static func throwIfBad(resp: URLResponse, data: Data) throws {
        guard let http = resp as? HTTPURLResponse, !(200..<300).contains(http.statusCode) else { return }
        throw UsersError.requestFailed(status: http.statusCode, body: data)
    }

    private static func defaultBaseURL() -> URL {
        if let s = Bundle.main.object(forInfoDictionaryKey: "GatewayBaseURL") as? String,
           let u = URL(string: s.trimmingCharacters(in: .whitespacesAndNewlines)),
           !s.isEmpty {
            // ודא שיש /api בסוף
            if u.path.hasSuffix("/api") { return u }
            return u.appendingPathComponent("api")
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
