import Foundation

public struct Quote: Codable, Hashable {
    public var id: String?
    public var factoryId: String?
    public var projectId: String?
    public var pricedItems: [ItemModelDTO]?
    public var factor: Double?
    public var finalPrice: Double?
    public var quotePdf: Data?      // List<Byte> -> Data
    public var status: String?      // RECEIVED, CONFIRMED, DECLINED ...
    public var createdAt: String?

    public init(id: String? = nil,
                factoryId: String? = nil,
                projectId: String? = nil,
                pricedItems: [ItemModelDTO]? = nil,
                factor: Double? = nil,
                finalPrice: Double? = nil,
                quotePdf: Data? = nil,
                status: String? = nil,
                createdAt: String? = nil) {
        self.id = id
        self.factoryId = factoryId
        self.projectId = projectId
        self.pricedItems = pricedItems
        self.factor = factor
        self.finalPrice = finalPrice
        self.quotePdf = quotePdf
        self.status = status
        self.createdAt = createdAt
    }

    // התאמה ל-equals בג׳אווה: נספור זהים אם factoryId & projectId זהים
    public static func == (lhs: Quote, rhs: Quote) -> Bool {
        return lhs.factoryId == rhs.factoryId && lhs.projectId == rhs.projectId
    }

    public func hash(into hasher: inout Hasher) {
        hasher.combine(factoryId)
        hasher.combine(projectId)
    }
}
