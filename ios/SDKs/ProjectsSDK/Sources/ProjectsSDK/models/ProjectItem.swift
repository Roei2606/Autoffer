import Foundation

public struct ProjectItem: Codable, Hashable {
    public var itemNumber: Int?
    public var profile: AlumProfile?
    public var glass: Glass?
    public var height: Double?
    public var width: Double?
    public var quantity: Int?
    public var location: String?

    public init(itemNumber: Int? = nil,
                profile: AlumProfile? = nil,
                glass: Glass? = nil,
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

    public func getDimensions() -> Int {
        let h = height ?? 0
        let w = width  ?? 0
        return Int(h * w)
    }
}
