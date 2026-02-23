import Foundation
import ProjectsSDK
import LocalProjectSDK
import CoreModelsSDK

@MainActor
final class AddManualViewModel: ObservableObject {
    @Published var profiles: [AlumProfile] = []
    @Published var glasses: [Glass] = []
    @Published var selectedProfile: AlumProfile?
    @Published var selectedGlass: Glass?
    
    private let projectManager = ProjectManager()
    
    func findProfiles(width: Int, height: Int) async {
        do {
            profiles = try await projectManager.getMatchingProfiles(width: width, height: height)
            selectedProfile = nil
            selectedGlass = nil
            glasses = []
        } catch {
            print("❌ Failed to load profiles: \(error)")
        }
    }
    
    func loadGlasses(for profile: AlumProfile) async {
        do {
            glasses = try await projectManager.getGlassesForProfile(profileNumber: profile.profileNumber ?? "")
            selectedProfile = profile
        } catch {
            print("❌ Failed to load glasses: \(error)")
        }
    }
    
    func addItemToProject(width: Double,
                          height: Double,
                          quantity: Int,
                          position: String) async {
        guard let project = await LocalProjectStorage.shared.currentProject,
              let profile = selectedProfile,
              let glass = selectedGlass else { return }
        
        let newItem = LocalItemEntity(
            id: UUID().uuidString,
            projectId: project.id,
            itemNumber: await LocalProjectStorage.shared.getCurrentItems().count + 1,
            profileNumber: profile.profileNumber ?? "",
            profileType: profile.usageType ?? "",
            glassType: glass.type ?? "",
            height: height,
            width: width,
            quantity: quantity,
            isExpensive: (profile.pricePerSquareMeter ?? 0) > 500,
            location: position,
            profile: profile.toDTO(),
            glass: glass.toDTO()
        )
        
        await LocalProjectStorage.shared.addItem(newItem)
    }
}
