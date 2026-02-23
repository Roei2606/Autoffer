import Foundation

enum UsersRoutes {
    static let login          = "users/login"
    static let register       = "users/register"
    static let all            = "users"
    static func byId(_ id: String) -> String { "users/\(id)" }
    static let resetPassword  = "users/reset-password"
    static func byType(_ t: String) -> String { "users/type/\(t)" }
}
