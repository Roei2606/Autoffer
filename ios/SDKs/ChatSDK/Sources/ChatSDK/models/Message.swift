import Foundation

public struct Message: Codable, Hashable, Sendable {
    public var id: String?
    public var chatId: String?
    public var senderId: String?
    public var receiverId: String?
    public var content: String?
    public var timestamp: String?
    public var readBy: [String]?

    // 📎 שדות קובץ
    public var fileBytes: Data?      // List<Byte> -> Data (Base64 JSON)
    public var fileName: String?
    public var fileType: String?

    // ✅ סוג ההודעה ("TEXT", "FILE", "BOQ_REQUEST", ...)
    public var type: String?

    // ✅ מידע נוסף
    public var metadata: [String: String]?

    public init(id: String? = nil,
                chatId: String? = nil,
                senderId: String? = nil,
                receiverId: String? = nil,
                content: String? = nil,
                timestamp: String? = nil,
                readBy: [String]? = nil,
                fileBytes: Data? = nil,
                fileName: String? = nil,
                fileType: String? = nil,
                type: String? = nil,
                metadata: [String: String]? = nil) {
        self.id = id
        self.chatId = chatId
        self.senderId = senderId
        self.receiverId = receiverId
        self.content = content
        self.timestamp = timestamp
        self.readBy = readBy
        self.fileBytes = fileBytes
        self.fileName = fileName
        self.fileType = fileType
        self.type = type
        self.metadata = metadata
    }
}
