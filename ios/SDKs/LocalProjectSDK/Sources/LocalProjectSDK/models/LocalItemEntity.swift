import Foundation
import ProjectsSDK

public struct LocalItemEntity: Codable, Hashable, Identifiable, Sendable {
    public var id: String
    public var projectId: String
    public var itemNumber: Int
    public var profileNumber: String
    public var profileType: String
    public var glassType: String
    public var height: Double
    public var width: Double
    public var quantity: Int
    public var isExpensive: Bool
    public var location: String

    public var profile: AlumProfileDTO?
    public var glass: GlassDTO?

    public init(id: String,
                projectId: String,
                itemNumber: Int,
                profileNumber: String,
                profileType: String,
                glassType: String,
                height: Double,
                width: Double,
                quantity: Int,
                isExpensive: Bool,
                location: String,
                profile: AlumProfileDTO? = nil,
                glass: GlassDTO? = nil) {
        self.id = id
        self.projectId = projectId
        self.itemNumber = itemNumber
        self.profileNumber = profileNumber
        self.profileType = profileType
        self.glassType = glassType
        self.height = height
        self.width = width
        self.quantity = quantity
        self.isExpensive = isExpensive
        self.location = location
        self.profile = profile
        self.glass = glass
    }
}
