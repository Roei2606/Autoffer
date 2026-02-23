import Foundation
import UsersSDK
import ProjectsSDK
import ChatSDK
@preconcurrency import AdsSDK

@MainActor
final class AppEnvironment {
    static let shared = AppEnvironment()

    // Core session (שומר currentUser/currentUserId + עזרי HTTP)
    private(set) var session: SessionManager!

    // SDKs מעל ה-Gateway (HTTP)
    private(set) var userManager: UserManager!
    private(set) var projectManager: ProjectManager!
    private(set) var chatManager: ChatManager!
    private(set) var adsManager: AdsManager!

    private init() {}

    /// בסיס ה-Gateway. עדיף לשים Info.plist key בשם "GatewayBaseURL".
    private var gatewayBaseURL: URL {
        if let s = Bundle.main.object(forInfoDictionaryKey: "GatewayBaseURL") as? String,
           let u = URL(string: s.trimmingCharacters(in: .whitespacesAndNewlines)),
           !s.isEmpty {
            // ודא שיש /api בסוף
            return u.path.hasSuffix("/api") ? u : u.appendingPathComponent("api")
        }
        #if targetEnvironment(simulator)
        return URL(string: "http://127.0.0.1:8081/api")!
        #else
        return URL(string: "http://192.168.1.123:8081/api")! // עדכן ל-IP של המאק
        #endif
    }

    func configure() async {
        // 1) SessionManager משותף + עדכון base
        session = SessionManager.shared
        await session.updateBaseURL(gatewayBaseURL)

        // 2) צריבת המנהלים ללא 'apiBase' — כל אחד קורא את ה-base מה-Info.plist או מהדיפולט שלו
        userManager    = UserManager()
        projectManager = ProjectManager()
        chatManager    = ChatManager()
        adsManager     = AdsManager()
    }

    var isReady: Bool {
        session != nil &&
        userManager != nil &&
        projectManager != nil &&
        chatManager != nil &&
        adsManager != nil
    }
}
