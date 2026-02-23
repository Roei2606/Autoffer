import Foundation
import CoreModelsSDK
import RSocketSDK // רק בשביל JSONCoding (ISO-8601) ו-RSocketError לאחידות שגיאות

@MainActor
public protocol AdsManaging {
    func getAds(for profileType: UserType) async throws -> [Ad]
}

@MainActor
public final class AdsManager: AdsManaging {

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

    public func getAds(for profileType: UserType) async throws -> [Ad] {
        // אם יש לך AdsRequest בתוך CoreModelsSDK, השתמש בו כאן:
        // let req = AdsRequest(audience: profileType)
        // אחרת נשתמש במבנה פנימי זהה:
        struct AdsReq: Encodable { let audience: UserType }
        let req = AdsReq(audience: profileType)

        return try await post("ads/by-audience", body: req)
    }

    // MARK: - HTTP helpers

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

    private func throwIfBad(_ resp: URLResponse, data: Data) throws {
        guard let http = resp as? HTTPURLResponse, !(200..<300).contains(http.statusCode) else { return }
        throw RSocketError.requestFailed(status: http.statusCode, body: data) // שומר אחידות שגיאות באפליקציה
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
