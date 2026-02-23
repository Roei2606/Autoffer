import Foundation

public final class LocalProjectRepository {
    private let db = LocalProjectDatabase.shared

    public init() {}

    // MARK: Inserts
    public func insertProject(_ project: LocalProjectEntity) async {
        await db.insertProject(project)
    }

    public func insertItems(_ items: [LocalItemEntity]) async {
        await db.insertItems(items)
    }

    public func insertProjectWithItems(_ project: LocalProjectEntity, items: [LocalItemEntity]) async {
        await db.insertProject(project)
        await db.insertItems(items)
    }

    // MARK: Queries
    public func getAllProjects() async -> [LocalProjectEntity] {
        await db.getAllProjects()
    }

    public func getProjectById(_ id: String) async -> LocalProjectEntity? {
        await db.getProjectById(id)
    }

    public func getItemsForProject(_ projectId: String) async -> [LocalItemEntity] {
        await db.getItems(projectId: projectId)
    }

    // MARK: Deletes / Updates
    public func deleteProjectWithItems(_ projectId: String) async {
        if let p = await db.getProjectById(projectId) {
            await db.deleteItems(projectId: projectId)
            await db.deleteProject(p)
        }
    }

    public func updateProject(_ project: LocalProjectEntity) async {
        await db.updateProject(project)
    }

    public func updateItem(_ item: LocalItemEntity) async {
        await db.updateItem(item)
    }
}
