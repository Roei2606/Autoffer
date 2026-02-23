import Foundation

public struct UnreadCountRequest: Codable, Hashable {
    public var chatId: String
    public var userId: String

    public init(chatId: String, userId: String) {
        self.chatId = chatId
        self.userId = userId
    }
}
