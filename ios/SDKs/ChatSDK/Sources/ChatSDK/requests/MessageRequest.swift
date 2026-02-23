import Foundation

public struct MessageRequest: Codable, Hashable {
    public var chatId: String
    public var senderId: String
    public var content: String
    public var timestamp: String

    /// בנאי מלא – אתה מספק timestamp (כמחרוזת בפורמט שהשרת מצפה לו)
    public init(chatId: String, senderId: String, content: String, timestamp: String) {
        self.chatId = chatId
        self.senderId = senderId
        self.content = content
        self.timestamp = timestamp
    }

    /// נוחות: אם לא סיפקת timestamp – נשתמש בזמן נוכחי בפורמט ISO-8601
    public init(chatId: String, senderId: String, content: String) {
        self.init(chatId: chatId, senderId: senderId, content: content,
                  timestamp: ISO8601DateFormatter().string(from: Date()))
    }
}
