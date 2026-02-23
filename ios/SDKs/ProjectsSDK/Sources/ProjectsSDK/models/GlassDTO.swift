import Foundation

public struct GlassDTO: Codable, Hashable, Sendable {
    public var type: String?
    public var pricePerSquareMeter: Double?

    public init(type: String? = nil, pricePerSquareMeter: Double? = nil) {
        self.type = type
        self.pricePerSquareMeter = pricePerSquareMeter
    }
}
