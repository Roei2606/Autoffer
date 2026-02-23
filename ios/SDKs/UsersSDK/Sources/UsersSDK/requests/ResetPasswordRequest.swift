import Foundation

/// Reset password payload
public struct ResetPasswordRequest: Codable, Hashable, Sendable {
    public let phoneNumber: String
    public let newPassword: String

    public init(phoneNumber: String, newPassword: String) {
        self.phoneNumber = phoneNumber
        self.newPassword = newPassword
    }
}
