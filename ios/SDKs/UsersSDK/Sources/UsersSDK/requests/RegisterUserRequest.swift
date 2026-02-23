import Foundation
import CoreModelsSDK   // for UserType

/// Registration payload
public struct RegisterUserRequest: Codable, Hashable, Sendable {
    public var firstName: String
    public var lastName: String
    public var email: String
    public var password: String
    public var phoneNumber: String
    public var address: String
    /// Server expects key **"profileType"** (your Java used getter/setter names to serialize that key).
    public var profileType: UserType

    public init(firstName: String,
                lastName: String,
                email: String,
                password: String,
                phoneNumber: String,
                address: String,
                profileType: UserType) {
        self.firstName = firstName
        self.lastName = lastName
        self.email = email
        self.password = password
        self.phoneNumber = phoneNumber
        self.address = address
        self.profileType = profileType
    }

    private enum CodingKeys: String, CodingKey {
        case firstName, lastName, email, password, phoneNumber, address
        case profileType // <- keep external JSON key "profileType"
    }
}
