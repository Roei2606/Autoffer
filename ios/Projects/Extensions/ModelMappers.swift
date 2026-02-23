import ProjectsSDK
import CoreModelsSDK

extension AlumProfile {
    func toDTO() -> AlumProfileDTO {
        return AlumProfileDTO(
            profileNumber: self.profileNumber,
            usageType: self.usageType,
            pricePerSquareMeter: self.pricePerSquareMeter
        )
    }
}
    
extension Glass {
    func toDTO() -> GlassDTO {
        return GlassDTO(
            type: self.type,
            pricePerSquareMeter: self.pricePerSquareMeter
        )
    }
}

