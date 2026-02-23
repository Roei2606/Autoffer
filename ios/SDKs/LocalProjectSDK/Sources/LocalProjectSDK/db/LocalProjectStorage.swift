import Foundation

public actor LocalProjectStorage {
    public static let shared = LocalProjectStorage()

    private init() {}

    public var currentProject: LocalProjectEntity?
    private var items: [LocalItemEntity] = []

    public func setCurrentProject(_ project: LocalProjectEntity?) {
        currentProject = project
        items.removeAll()
    }

    public func addItem(_ item: LocalItemEntity) { items.append(item) }
    public func removeItem(_ item: LocalItemEntity) { items.removeAll { $0.id == item.id } }
    public func getCurrentItems() -> [LocalItemEntity] { items }
    public var hasProject: Bool { currentProject != nil }
    public func clear() { currentProject = nil; items.removeAll() }
    public var isEmpty: Bool { items.isEmpty }
    public var currentProjectAddress: String? { currentProject?.projectAddress }
    public func clearCurrentProject() { clear() }
}
