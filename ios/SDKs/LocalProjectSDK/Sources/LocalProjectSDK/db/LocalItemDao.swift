import Foundation

public protocol LocalItemDAO {
    func insert(_ item: LocalItemEntity)
    func insertAll(_ items: [LocalItemEntity])
    func update(_ item: LocalItemEntity)
    func delete(_ item: LocalItemEntity)
    func deleteItems(projectId: String)
    func getItems(projectId: String) -> [LocalItemEntity]
}
