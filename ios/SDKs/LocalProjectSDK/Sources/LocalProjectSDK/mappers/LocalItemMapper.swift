import Foundation
import ProjectsSDK   // ItemModelDTO

public enum LocalItemMapper {
    public static func toItemModelDTO(_ e: LocalItemEntity) -> ItemModelDTO {
        // Prefer existing nested DTOs; otherwise build one from flat fields
        let profile: AlumProfileDTO? = e.profile ?? (
            (!e.profileNumber.isEmpty || !e.profileType.isEmpty)
            ? AlumProfileDTO(profileNumber: e.profileNumber,
                             usageType: e.profileType,
                             pricePerSquareMeter: e.profile?.pricePerSquareMeter)
            : nil
        )

        let glass: GlassDTO? = e.glass ?? (
            !e.glassType.isEmpty
            ? GlassDTO(type: e.glassType, pricePerSquareMeter: e.glass?.pricePerSquareMeter)
            : nil
        )

        return ItemModelDTO(
            itemNumber: e.itemNumber,
            profile: profile,
            glass: glass,
            height: e.height,
            width:  e.width,
            quantity: e.quantity,
            location: e.location
        )
    }
}

public extension Array where Element == LocalItemEntity {
    func toItemModelDTOs() -> [ItemModelDTO] { map(LocalItemMapper.toItemModelDTO) }
}
