import Foundation

/// Simple user-id wrapper
public struct UserIdRequest: Codable, Hashable, Sendable {
    public let userId: String

    public init(userId: String) {
        self.userId = userId
    }

    private enum CodingKeys: String, CodingKey {
        case userId
    }
}
