import Foundation
import ProjectsSDK   // QuoteStatus

public struct UpdateFactoryStatusRequest: Codable, Hashable {
    public var projectId: String
    public var factoryId: String
    public var newStatus: QuoteStatus

    public init(projectId: String, factoryId: String, newStatus: QuoteStatus) {
        self.projectId = projectId
        self.factoryId = factoryId
        self.newStatus = newStatus
    }
}
