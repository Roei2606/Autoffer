import Foundation
import RSocketSDK   // עבור JSONCoding (אם תרצה, אפשר לעבור ל-CoreModelsSDK ולהסיר תלות בעתיד)

// MARK: - Swift 6 Sendable shims (עד שתוסיף Sendable בהגדרות המקור)
extension UserProjectRequest: @unchecked Sendable {}
extension SendBOQRequest: @unchecked Sendable {}
extension UpdateFactoryStatusRequest: @unchecked Sendable {}
extension SizeRequest: @unchecked Sendable {}
extension CreateProjectRequest: @unchecked Sendable {}
extension GetQuotePdfRequest: @unchecked Sendable {}

extension ProjectDTO: @unchecked Sendable {}
extension AlumProfile: @unchecked Sendable {}
extension Glass: @unchecked Sendable {}
extension QuoteStatus: @unchecked Sendable {}

// MARK: - Protocol (ללא שינוי)
@MainActor
public protocol ProjectManaging: AnyObject {
    func getUserProjects(_ request: UserProjectRequest) async throws -> [ProjectDTO]
    func sendBOQToFactories(_ request: SendBOQRequest) async throws
    func respondToBoqRequest(_ request: UpdateFactoryStatusRequest) async throws
    func deleteProjectById(_ projectId: String) async throws
    func getMatchingProfiles(width: Int, height: Int) async throws -> [AlumProfile]
    func getGlassesForProfile(profileNumber: String) async throws -> [Glass]
    func createProject(_ request: CreateProjectRequest) async throws -> ProjectDTO
    func updateFactoryStatus(projectId: String, factoryId: String, newStatus: QuoteStatus) async throws
    func getQuotePdf(projectId: String, factoryId: String) async throws -> Data
}

// MARK: - HTTP Routes (Gateway)
private enum ProjectHTTP {
    static let projectsRoot              = "projects"
    static let projectsForUser           = "projects/user"                 // POST UserProjectRequest -> [ProjectDTO]
    static let sendToFactories           = "projects/send-boq"             // POST -> 204
    static let respondBoq                = "projects/respond-boq"          // POST -> 204
    static func delete(_ id: String) -> String { "projects/\(id)" }        // DELETE -> 204
    static let matchProfiles             = "profiles/match"                // GET ?width=&height= -> [AlumProfile]
    static let glasses                   = "glasses"                       // GET ?profileNumber= -> [Glass]
    static let updateFactoryStatus       = "projects/update-factory-status"// POST UpdateFactoryStatusRequest -> 204
    static func quotePdf(_ pid: String, _ fid: String) -> String {
        "projects/\(pid)/factories/\(fid)/quote.pdf"                       // GET -> application/pdf
    }
}

// MARK: - Implementation (HTTP)
@MainActor
public final class ProjectManager: ProjectManaging {

    private let session: URLSession
    /// חייב להצביע ל־http(s)://<host>:8081/api
    private let apiBase: URL

    // DI init (לבדיקות/קונפיג מותאם)
    public init(apiBase: URL, session: URLSession = .shared) {
        self.apiBase = apiBase
        self.session = session
    }

    /// נוחות: קורא מ־Info.plist (Key: "GatewayBaseURL") או ברירת מחדל DEV
    public convenience init() {
        self.init(apiBase: Self.defaultAPIBase(), session: .shared)
    }

    // MARK: - API (ללא שינוי חתימות)

    public func getUserProjects(_ request: UserProjectRequest) async throws -> [ProjectDTO] {
        try await post(ProjectHTTP.projectsForUser, body: request)
    }

    public func sendBOQToFactories(_ request: SendBOQRequest) async throws {
        try await postNoContent(ProjectHTTP.sendToFactories, body: request)
    }

    public func respondToBoqRequest(_ request: UpdateFactoryStatusRequest) async throws {
        try await postNoContent(ProjectHTTP.respondBoq, body: request)
    }

    public func deleteProjectById(_ projectId: String) async throws {
        try await delete(ProjectHTTP.delete(projectId))
    }

    public func getMatchingProfiles(width: Int, height: Int) async throws -> [AlumProfile] {
        try await get(ProjectHTTP.matchProfiles,
                      query: [URLQueryItem(name: "width", value: String(width)),
                              URLQueryItem(name: "height", value: String(height))])
    }

    public func getGlassesForProfile(profileNumber: String) async throws -> [Glass] {
        try await get(ProjectHTTP.glasses,
                      query: [URLQueryItem(name: "profileNumber", value: profileNumber)])
    }

    public func createProject(_ request: CreateProjectRequest) async throws -> ProjectDTO {
        try await post(ProjectHTTP.projectsRoot, body: request)
    }

    public func updateFactoryStatus(projectId: String, factoryId: String, newStatus: QuoteStatus) async throws {
        let req = UpdateFactoryStatusRequest(projectId: projectId, factoryId: factoryId, newStatus: newStatus)
        try await postNoContent(ProjectHTTP.updateFactoryStatus, body: req)
    }

    public func getQuotePdf(projectId: String, factoryId: String) async throws -> Data {
        try await getRaw(ProjectHTTP.quotePdf(projectId, factoryId), accept: "application/pdf")
    }

    // MARK: - Tiny HTTP helpers

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

    private func getRaw(_ path: String, accept: String) async throws -> Data {
        var req = URLRequest(url: makeURL(path))
        req.httpMethod = "GET"
        req.setValue(accept, forHTTPHeaderField: "Accept")
        let (data, resp) = try await session.data(for: req)
        try throwIfBad(resp, data: data)
        return data
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

    private func postNoContent<In: Encodable>(_ path: String, body: In) async throws {
        var req = URLRequest(url: makeURL(path))
        req.httpMethod = "POST"
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        req.httpBody = try JSONCoding.encoder.encode(body)
        let (_, resp) = try await session.data(for: req)
        try throwIfBad(resp, data: Data())
    }

    private func delete(_ path: String) async throws {
        var req = URLRequest(url: makeURL(path))
        req.httpMethod = "DELETE"
        let (_, resp) = try await session.data(for: req)
        try throwIfBad(resp, data: Data())
    }

    private func throwIfBad(_ resp: URLResponse, data: Data) throws {
        guard let http = resp as? HTTPURLResponse, !(200..<300).contains(http.statusCode) else { return }
        // שמרתי על שגיאה בסגנון "RSocketError" כדי לא לשבור טיפוסי שגיאה קיימים באפליקציה
        throw RSocketError.requestFailed(status: http.statusCode, body: data)
    }

    // MARK: - Default base URL (Gateway)
    private static func defaultAPIBase() -> URL {
        // אם יש ב-Info.plist מפתח GatewayBaseURL (ללא /api בסוף) – עדיף
        if let s = Bundle.main.object(forInfoDictionaryKey: "GatewayBaseURL") as? String,
           let root = URL(string: s.trimmingCharacters(in: .whitespacesAndNewlines)),
           !s.isEmpty {
            // דאג ש־GatewayBaseURL כבר כולל /api, ואם לא – נוסיף
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
