import Foundation

public actor LocalProjectDatabase {
    public static let shared = LocalProjectDatabase()

    // "טבלאות" בזיכרון
    private var projects: [String: LocalProjectEntity] = [:]
    private var itemsByProject: [String: [LocalItemEntity]] = [:]

    private init() {}

    // MARK: - Project DAO
    public func insertProject(_ p: LocalProjectEntity) {
        projects[p.id] = p
    }
    public func updateProject(_ p: LocalProjectEntity) {
        projects[p.id] = p
    }
    public func deleteProject(_ p: LocalProjectEntity) {
        projects.removeValue(forKey: p.id)
        itemsByProject[p.id] = nil
    }
    public func getAllProjects() -> [LocalProjectEntity] {
        Array(projects.values)
    }
    public func getProjectById(_ id: String) -> LocalProjectEntity? {
        projects[id]
    }

    // MARK: - Item DAO
    public func insertItem(_ item: LocalItemEntity) {
        itemsByProject[item.projectId, default: []].append(item)
    }
    public func insertItems(_ items: [LocalItemEntity]) {
        for it in items { insertItem(it) }
    }
    public func updateItem(_ item: LocalItemEntity) {
        var arr = itemsByProject[item.projectId] ?? []
        if let idx = arr.firstIndex(where: { $0.id == item.id }) {
            arr[idx] = item
            itemsByProject[item.projectId] = arr
        }
    }
    public func deleteItem(_ item: LocalItemEntity) {
        var arr = itemsByProject[item.projectId] ?? []
        arr.removeAll { $0.id == item.id }
        itemsByProject[item.projectId] = arr
    }
    public func deleteItems(projectId: String) {
        itemsByProject[projectId] = []
    }
    public func getItems(projectId: String) -> [LocalItemEntity] {
        itemsByProject[projectId] ?? []
    }
}
