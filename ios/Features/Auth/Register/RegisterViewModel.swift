import Foundation
import UsersSDK
import CoreModelsSDK

@MainActor
final class RegisterViewModel: ObservableObject {

    @Published var isLoading = false
    @Published var errorMessage: String?
    @Published var didRegister = false

    private let env: AppEnvironment
    private var userManager: UserManager { env.userManager }

    init(env: AppEnvironment) { self.env = env }
    convenience init() { self.init(env: .shared) }

    func register(firstName: String,
                  lastName: String,
                  email: String,
                  password: String,
                  confirmPassword: String,
                  phone: String,
                  address: String,
                  userType: UserType) async {

        // ✅ דרישה יחידה: סיסמה באורך 8+ (אפשר להשאיר גם בדיקות ריקות/תבנית אימייל אם תרצה)
        guard password.count >= 8 else {
            errorMessage = "Password must be at least 8 characters"
            return
        }

        isLoading = true
        errorMessage = nil
        defer { isLoading = false }

        let req = RegisterUserRequest(
            firstName: firstName,
            lastName: lastName,
            email: email,
            password: password,
            phoneNumber: phone,
            address: address,
            profileType: userType
        )

        do {
            _ = try await userManager.register(req)   // השם תואם ל-UsersSDK
            didRegister = true
        } catch {
            errorMessage = (error as NSError).localizedDescription
            didRegister = false
        }
    }
}
