import Foundation
import LocalProjectSDK
import CoreModelsSDK

@MainActor
final class NewProjectViewModel: ObservableObject {
    @Published var address: String = ""
    @Published var errorMessage: String?
    
    func startNewProject(for userId: String) async {
        guard !address.isEmpty else {
            errorMessage = "❌ Please enter a project address"
            return
        }
        
        let formatter = ISO8601DateFormatter()
        let now = formatter.string(from: Date())
        
        let project = LocalProjectEntity(
            id: UUID().uuidString,
            clientId: userId,
            projectAddress: address,
            createdAt: now
        )
        
        await LocalProjectStorage.shared.setCurrentProject(project)
        print("✅ New local project created at \(project.createdAt): \(project.projectAddress)")
    }
}
