import Foundation
import ProjectsSDK   // ItemModelDTO

public struct CreateProjectRequest: Codable, Hashable {
    public var clientId: String
    public var projectAddress: String
    public var items: [ItemModelDTO]
    public var factoryIds: [String]

    public init(clientId: String,
                projectAddress: String,
                items: [ItemModelDTO],
                factoryIds: [String]) {
        self.clientId = clientId
        self.projectAddress = projectAddress
        self.items = items
        self.factoryIds = factoryIds
    }
}
