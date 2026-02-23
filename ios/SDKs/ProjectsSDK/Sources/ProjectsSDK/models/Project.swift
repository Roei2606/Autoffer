import Foundation


public struct Project: Codable, Hashable {
    public var id: String?
    public var clientId: String?
    public var projectAddress: String?
    public var items: [ProjectItem]?
    public var factoryIds: [String]?

    // factoryId -> QuoteStatus
    public var quoteStatuses: [String: QuoteStatus]?

    // factoryId -> Quote
    public var quotes: [String: Quote]?

    public var createdAt: String?
    public var boqPdf: Data?      // List<Byte> -> Data (Base64)

    public init(id: String? = nil,
                clientId: String? = nil,
                projectAddress: String? = nil,
                items: [ProjectItem]? = nil,
                factoryIds: [String]? = nil,
                quoteStatuses: [String: QuoteStatus]? = nil,
                quotes: [String: Quote]? = nil,
                createdAt: String? = nil,
                boqPdf: Data? = nil) {
        self.id = id
        self.clientId = clientId
        self.projectAddress = projectAddress
        self.items = items
        self.factoryIds = factoryIds
        self.quoteStatuses = quoteStatuses
        self.quotes = quotes
        self.createdAt = createdAt
        self.boqPdf = boqPdf
    }
}
