import Foundation

public struct AlumProfileDTO: Codable, Hashable, Sendable {
    public var profileNumber: String?
    public var usageType: String?
    public var pricePerSquareMeter: Double?

    public init(profileNumber: String? = nil,
                usageType: String? = nil,
                pricePerSquareMeter: Double? = nil) {
        self.profileNumber = profileNumber
        self.usageType = usageType
        self.pricePerSquareMeter = pricePerSquareMeter
    }
}
