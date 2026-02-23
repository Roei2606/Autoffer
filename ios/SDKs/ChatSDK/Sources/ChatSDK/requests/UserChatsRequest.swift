import Foundation

public struct UserChatsRequest: Codable, Hashable {
    public var userId: String
    public var page: Int
    public var size: Int

    public init(userId: String, page: Int, size: Int) {
        self.userId = userId
        self.page = page
        self.size = size
    }
}
