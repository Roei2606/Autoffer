import Foundation
import ProjectsSDK
import CoreModelsSDK   // בשביל UserType

@MainActor
final class MyProjectsViewModel: ObservableObject {
    @Published var projects: [ProjectDTO] = []
    @Published var isLoading: Bool = false
    @Published var errorMessage: String?
    
    private let projectManager = ProjectManager()
    
    func loadProjects(for userId: String, profileType: UserType) async {
        isLoading = true
        errorMessage = nil
        do {
            let request = UserProjectRequest(userId: userId, profileType: profileType)
            let result = try await projectManager.getUserProjects(request)
            projects = result
        } catch {
            errorMessage = "❌ Failed to load projects"
            projects = []
            print(error)
        }
        isLoading = false
    }
}
