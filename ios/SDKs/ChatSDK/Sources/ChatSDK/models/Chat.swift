import Foundation

public struct Chat: Codable, Hashable {
    public var id: String?
    public var participants: [String]?
    public var lastMessage: String?
    public var lastMessageTimestamp: String?

    public init(id: String? = nil,
                participants: [String]? = nil,
                lastMessage: String? = nil,
                lastMessageTimestamp: String? = nil) {
        self.id = id
        self.participants = participants
        self.lastMessage = lastMessage
        self.lastMessageTimestamp = lastMessageTimestamp
    }
}
