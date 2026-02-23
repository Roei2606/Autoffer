import Foundation

public struct SendBOQRequest: Codable, Hashable {
    public var projectId: String
    public var factoryIds: [String]

    public init(projectId: String, factoryIds: [String]) {
        self.projectId = projectId
        self.factoryIds = factoryIds
    }
}
