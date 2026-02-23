import Foundation
import LocalProjectSDK
import ProjectsSDK

@MainActor
final class CurrentProjectViewModel: ObservableObject {
    @Published var items: [LocalItemEntity] = []
    
    func loadItems() async {
        items = await LocalProjectStorage.shared.getCurrentItems()
    }
    
    func deleteItem(_ item: LocalItemEntity) async {
        await LocalProjectStorage.shared.removeItem(item)
        await loadItems()
    }
    
    func saveProject() async throws {
        guard let project = await LocalProjectStorage.shared.currentProject else { return }
        let items = await LocalProjectStorage.shared.getCurrentItems()
        
        let request = CreateProjectMapper.makeRequest(
            project: project,
            items: items,
            factoryIds: []
        )
        
        let saved = try await ProjectManager().createProject(request)
        await LocalProjectStorage.shared.clear()
        print("✅ Project saved: \(saved)")
    }
}
