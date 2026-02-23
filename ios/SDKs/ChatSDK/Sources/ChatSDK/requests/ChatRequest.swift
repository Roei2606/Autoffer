import Foundation

public struct ChatRequest: Codable, Hashable {
    public let currentUserId: String
    public let otherUserId: String

    public init(currentUserId: String, otherUserId: String) {
        self.currentUserId = currentUserId
        self.otherUserId = otherUserId
    }
}
