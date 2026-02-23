import Foundation

public protocol LocalProjectDAO {
    func insert(_ project: LocalProjectEntity)
    func update(_ project: LocalProjectEntity)
    func delete(_ project: LocalProjectEntity)
    func getAllProjects() -> [LocalProjectEntity]
    func getProjectById(_ id: String) -> LocalProjectEntity?
}
