import Foundation
import UsersSDK
import CoreModelsSDK

@MainActor
final class LoginViewModel: ObservableObject {

    // MARK: Inputs
    @Published var email: String = ""
    @Published var password: String = ""

    // MARK: Outputs
    @Published private(set) var statusText: String = ""
    @Published private(set) var isLoading: Bool = false
    @Published private(set) var errorMessage: String?
    @Published private(set) var didLogin: Bool = false

    // MARK: Deps
    private let session: SessionManager
    private var userManager: UserManager

    private var loginTask: Task<Void, Never>?

    // אל תשים ערך ברירת מחדל .shared בפרמטר (Swift 6 לא אוהב זאת)
    init(env: AppEnvironment) {
        // אם AppEnvironment מוכן – נשתמש בו; אחרת ניפול ל־SessionManager.shared ו־UserManager מותאם
        let sess = env.session ?? SessionManager.shared
        self.session = sess

        // בחר אחד מהשניים לפי ה־initializer שקיים אצלך ב-UsersSDK:

        // (א) אם יש init() דיפולטי ב-UserManager:
        // self.userManager = env.userManager ?? UserManager()

        // (ב) אם יש init(session:) או init(http:):
        // self.userManager = env.userManager ?? UserManager(session: sess)
        // self.userManager = env.userManager ?? UserManager(http: sess)

        // ↓ השאר רק את השורה שתואמת לחתימה אצלך:
        self.userManager = env.userManager ?? UserManager()
    }

    // נוח לקריאות בלי להעביר env מבחוץ. זה בטוח כי אנחנו @MainActor.
    convenience init() {
        self.init(env: AppEnvironment.shared)
    }

    func start() {
        statusText = ""
    }

    func login() {
        guard !isLoading else { return }
        guard isValidEmail(email) else {
            errorMessage = "Invalid email format"
            return
        }
        guard password.count >= 8 else {
            errorMessage = "Password must be at least 8 characters"
            return
        }

        isLoading = true
        statusText = "Signing in…"
        errorMessage = nil
        didLogin = false

        loginTask?.cancel()
        loginTask = Task { [weak self] in
            guard let self else { return }
            do {
                // ⬅️ השימוש הנכון: עטיפת הקלט ל-LoginRequest
                let req = LoginRequest(email: self.email, password: self.password)
                let user = try await self.userManager.login(req)

                await self.session.setCurrentUser(user)

                self.isLoading = false
                self.statusText = user.firstName.map { "Welcome \($0)!" } ?? "Welcome!"
                self.didLogin = true
            } catch is CancellationError {
                // ignore
            } catch {
                self.isLoading = false
                self.statusText = "Login error"
                self.errorMessage = (error as NSError).localizedDescription
                self.didLogin = false
            }
        }
    }

    deinit { loginTask?.cancel() }

    // MARK: - Helpers
    private func isValidEmail(_ email: String) -> Bool {
        let pattern = "^[A-Za-z0-9+_.-]+@(.+)$"
        return NSPredicate(format: "SELF MATCHES %@", pattern).evaluate(with: email)
    }
}
