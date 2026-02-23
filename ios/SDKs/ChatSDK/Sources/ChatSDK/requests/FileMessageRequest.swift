import Foundation

public struct FileMessageRequest: Codable, Hashable {
    public var chatId: String
    public var senderId: String
    public var receiverId: String
    public var fileBytes: Data
    public var fileName: String
    public var fileType: String
    public var timestamp: String

    public init(chatId: String,
                senderId: String,
                receiverId: String,
                fileBytes: Data,
                fileName: String,
                fileType: String,
                timestamp: String) {
        self.chatId = chatId
        self.senderId = senderId
        self.receiverId = receiverId
        self.fileBytes = fileBytes
        self.fileName = fileName
        self.fileType = fileType
        self.timestamp = timestamp
    }
}
