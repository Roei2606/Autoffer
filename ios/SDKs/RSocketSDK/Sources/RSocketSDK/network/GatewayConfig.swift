import Foundation

public enum GatewayConfig {
    public static var baseURL: URL {
        if let s = Bundle.main.object(forInfoDictionaryKey: "GatewayBaseURL") as? String,
           let u = URL(string: s.trimmingCharacters(in: .whitespacesAndNewlines)),
           !s.isEmpty {
            return u
        }
        #if DEBUG
        #if targetEnvironment(simulator)
        return URL(string: "http://127.0.0.1:8090")!   // 👈 שים לב: 8090
        #else
        return URL(string: "http://192.168.1.123:8090")! // 👈 גם כאן 8090
        #endif
        #else
        return URL(string: "https://your-gateway.example.com")!
        #endif
    }

    public static var apiBase: URL { baseURL.appendingPathComponent("api") }
}

