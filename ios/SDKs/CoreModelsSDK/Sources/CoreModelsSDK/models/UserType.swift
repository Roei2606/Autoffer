import Foundation

public enum UserType: String, Codable, Hashable, Sendable {
    case privateCustomer = "PRIVATE_CUSTOMER"
    case architect       = "ARCHITECT"
    case factory         = "FACTORY"
}
