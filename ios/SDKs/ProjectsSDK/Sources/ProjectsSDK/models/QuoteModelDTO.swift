import Foundation

public struct QuoteModelDTO: Codable, Hashable {
    public var factoryId: String?
    public var pricedItems: [ItemModelDTO]?
    public var finalPrice: Double?
    public var quotePdf: Data?      // 👈 במקום [UInt8]
    public var createdAt: String?
    
    enum CodingKeys: String, CodingKey {
        case factoryId, pricedItems, finalPrice, quotePdf, createdAt
    }
    
    public init(factoryId: String? = nil,
                pricedItems: [ItemModelDTO]? = nil,
                finalPrice: Double? = nil,
                quotePdf: Data? = nil,
                createdAt: String? = nil) {
        self.factoryId = factoryId
        self.pricedItems = pricedItems
        self.finalPrice = finalPrice
        self.quotePdf = quotePdf
        self.createdAt = createdAt
    }
    
    // ✅ Custom decoder
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        factoryId = try? container.decode(String.self, forKey: .factoryId)
        pricedItems = try? container.decode([ItemModelDTO].self, forKey: .pricedItems)
        finalPrice = try? container.decode(Double.self, forKey: .finalPrice)
        createdAt = try? container.decode(String.self, forKey: .createdAt)
        
        if let bytes = try? container.decode([UInt8].self, forKey: .quotePdf) {
            quotePdf = Data(bytes)
        } else {
            quotePdf = nil
        }
    }
    
    // ✅ Custom encoder
    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encodeIfPresent(factoryId, forKey: .factoryId)
        try container.encodeIfPresent(pricedItems, forKey: .pricedItems)
        try container.encodeIfPresent(finalPrice, forKey: .finalPrice)
        try container.encodeIfPresent(createdAt, forKey: .createdAt)
        
        if let data = quotePdf {
            try container.encode([UInt8](data), forKey: .quotePdf)
        }
    }
}
