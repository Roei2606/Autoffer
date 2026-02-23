import Foundation

public struct GetQuotePdfRequest: Codable, Hashable {
    public let projectId: String
    public let factoryId: String

    public init(projectId: String, factoryId: String) {
        self.projectId = projectId
        self.factoryId = factoryId
    }
}
