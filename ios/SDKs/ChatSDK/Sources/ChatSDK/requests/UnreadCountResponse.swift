import Foundation

public struct UnreadCountResponse: Codable, Hashable {
    public var chatId: String
    public var count: Int

    public init(chatId: String, count: Int) {
        self.chatId = chatId
        self.count = count
    }
}
