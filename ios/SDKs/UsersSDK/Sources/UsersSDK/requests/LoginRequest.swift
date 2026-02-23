import Foundation

/// Login payload
public struct LoginRequest: Codable, Hashable, Sendable {
    public let email: String
    public let password: String

    public init(email: String, password: String) {
        self.email = email
        self.password = password
    }

    // JSON keys are identical, but this keeps it explicit
    private enum CodingKeys: String, CodingKey {
        case email, password
    }
}
