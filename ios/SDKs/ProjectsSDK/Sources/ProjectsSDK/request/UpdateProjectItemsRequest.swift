import Foundation
import ProjectsSDK   // ProjectItem

public struct UpdateProjectItemsRequest: Codable, Hashable {
    public var projectId: String
    public var items: [ProjectItem]

    public init(projectId: String, items: [ProjectItem]) {
        self.projectId = projectId
        self.items = items
    }
}
