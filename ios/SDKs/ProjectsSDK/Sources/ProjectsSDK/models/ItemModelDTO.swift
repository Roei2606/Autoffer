import Foundation

public struct ItemModelDTO: Codable, Hashable {
    public var itemNumber: Int?
    public var profile: AlumProfileDTO?
    public var glass: GlassDTO?
    public var height: Double?
    public var width: Double?
    public var quantity: Int?
    public var location: String?

    public init(itemNumber: Int? = nil,
                profile: AlumProfileDTO? = nil,
                glass: GlassDTO? = nil,
                height: Double? = nil,
                width: Double? = nil,
                quantity: Int? = nil,
                location: String? = nil) {
        self.itemNumber = itemNumber
        self.profile = profile
        self.glass = glass
        self.height = height
        self.width = width
        self.quantity = quantity
        self.location = location
    }
}
