import Foundation
import ProjectsSDK   // ItemModelDTO

public struct ProjectDTO: Codable, Hashable, Identifiable {
    public var id: String { projectId }
    
    public var projectId: String
    public var clientId: String
    public var projectAddress: String
    public var items: [ItemModelDTO]?
    public var factoryIds: [String]
    public var quoteStatuses: [String: String]
    public var boqPdf: Data?                       // 👈 במקום [UInt8] נשתמש ב־Data
    public var createdAt: String
    public var quotes: [String: QuoteModelDTO]?
    
    enum CodingKeys: String, CodingKey {
        case projectId, clientId, projectAddress, items, factoryIds, quoteStatuses, boqPdf, createdAt, quotes
    }
    
    // ✅ מפענח מותאם כדי להמיר [Int] / [UInt8] ל־Data
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        projectId = try container.decode(String.self, forKey: .projectId)
        clientId = try container.decode(String.self, forKey: .clientId)
        projectAddress = try container.decode(String.self, forKey: .projectAddress)
        items = try? container.decode([ItemModelDTO].self, forKey: .items)
        factoryIds = try container.decode([String].self, forKey: .factoryIds)
        quoteStatuses = try container.decode([String: String].self, forKey: .quoteStatuses)
        createdAt = try container.decode(String.self, forKey: .createdAt)
        quotes = try? container.decode([String: QuoteModelDTO].self, forKey: .quotes)
        
        if let bytes = try? container.decode([UInt8].self, forKey: .boqPdf) {
            boqPdf = Data(bytes)
        } else {
            boqPdf = nil
        }
    }
    
    // ✅ מקודד מותאם
    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(projectId, forKey: .projectId)
        try container.encode(clientId, forKey: .clientId)
        try container.encode(projectAddress, forKey: .projectAddress)
        try container.encodeIfPresent(items, forKey: .items)
        try container.encode(factoryIds, forKey: .factoryIds)
        try container.encode(quoteStatuses, forKey: .quoteStatuses)
        try container.encode(createdAt, forKey: .createdAt)
        try container.encodeIfPresent(quotes, forKey: .quotes)
        
        if let data = boqPdf {
            try container.encode([UInt8](data), forKey: .boqPdf)
        }
    }
}
