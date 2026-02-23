import Foundation

public struct ChatMessagesRequest: Codable, Hashable {
    public var chatId: String
    public var page: Int
    public var size: Int

    public init(chatId: String, page: Int, size: Int) {
        self.chatId = chatId
        self.page = page
        self.size = size
    }
}
