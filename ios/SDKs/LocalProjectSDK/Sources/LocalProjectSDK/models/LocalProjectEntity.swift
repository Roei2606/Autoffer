import Foundation

public struct LocalProjectEntity: Codable, Hashable, Identifiable, Sendable {
    public var id: String
    public var clientId: String
    public var projectAddress: String
    public var createdAt: String

    public init(id: String, clientId: String, projectAddress: String, createdAt: String) {
        self.id = id
        self.clientId = clientId
        self.projectAddress = projectAddress
        self.createdAt = createdAt
    }
}
