import Foundation
import ProjectsSDK    // CreateProjectRequest, ItemModelDTO

public enum CreateProjectMapper {
    public static func makeRequest(project: LocalProjectEntity,
                                   items: [LocalItemEntity],
                                   factoryIds: [String]) -> CreateProjectRequest {
        let itemDTOs = items.toItemModelDTOs()
        return CreateProjectRequest(
            clientId: project.clientId,
            projectAddress: project.projectAddress,
            items: itemDTOs,
            factoryIds: factoryIds
        )
    }
}

