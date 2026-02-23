import Foundation

public enum QuoteStatus: String, Codable {
    case PENDING
    case RECEIVED
    case ACCEPTED
    case REJECTED
}
