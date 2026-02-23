import Foundation

/// שקול ל-FactoryUser בג'אווה – כולל את כל שדות User + שדות מפעל
public struct FactoryUser: Codable, Hashable {
    // שדות User
    public let id: String?
    public let firstName: String?
    public let lastName: String?
    public let email: String?
    public let password: String?
    public let phoneNumber: String?
    public let address: String?
    public let profileType: UserType?
    public let registeredAt: Date?
    public let chats: [String]?
    public let photoBytes: Data?

    // שדות נוספים של Factory
    public let businessId: String?
    public let factor: Double?
    public let factoryName: String?

    public init(id: String? = nil,
                firstName: String? = nil,
                lastName: String? = nil,
                email: String? = nil,
                password: String? = nil,
                phoneNumber: String? = nil,
                address: String? = nil,
                profileType: UserType? = nil,
                registeredAt: Date? = nil,
                chats: [String]? = nil,
                photoBytes: Data? = nil,
                factoryName: String? = nil,
                businessId: String? = nil,
                factor: Double? = nil) {
        self.id = id
        self.firstName = firstName
        self.lastName = lastName
        self.email = email
        self.password = password
        self.phoneNumber = phoneNumber
        self.address = address
        self.profileType = profileType
        self.registeredAt = registeredAt
        self.chats = chats
        self.photoBytes = photoBytes
        self.factoryName = factoryName
        self.businessId = businessId
        self.factor = factor
    }

    private enum CodingKeys: String, CodingKey {
        case id, firstName, lastName, email, password,
             phoneNumber, address, profileType, registeredAt, chats, photoBytes,
             businessId, factor, factoryName
    }

    public init(from decoder: Decoder) throws {
        let c = try decoder.container(keyedBy: CodingKeys.self)
        id          = try c.decodeIfPresent(String.self, forKey: .id)
        firstName   = try c.decodeIfPresent(String.self, forKey: .firstName)
        lastName    = try c.decodeIfPresent(String.self, forKey: .lastName)
        email       = try c.decodeIfPresent(String.self, forKey: .email)
        password    = try c.decodeIfPresent(String.self, forKey: .password)
        phoneNumber = try c.decodeIfPresent(String.self, forKey: .phoneNumber)
        address     = try c.decodeIfPresent(String.self, forKey: .address)
        profileType = try c.decodeIfPresent(UserType.self, forKey: .profileType)
        chats       = try c.decodeIfPresent([String].self, forKey: .chats)
        photoBytes  = try c.decodeIfPresent(Data.self, forKey: .photoBytes)

        if let dateString = try c.decodeIfPresent(String.self, forKey: .registeredAt) {
            registeredAt = DateFormatters.userDateTime.date(from: dateString)
        } else {
            registeredAt = nil
        }

        businessId  = try c.decodeIfPresent(String.self, forKey: .businessId)
        factor      = try c.decodeIfPresent(Double.self, forKey: .factor)
        factoryName = try c.decodeIfPresent(String.self, forKey: .factoryName)
    }

    public func encode(to encoder: Encoder) throws {
        var c = encoder.container(keyedBy: CodingKeys.self)
        try c.encodeIfPresent(id, forKey: .id)
        try c.encodeIfPresent(firstName, forKey: .firstName)
        try c.encodeIfPresent(lastName, forKey: .lastName)
        try c.encodeIfPresent(email, forKey: .email)
        try c.encodeIfPresent(password, forKey: .password)
        try c.encodeIfPresent(phoneNumber, forKey: .phoneNumber)
        try c.encodeIfPresent(address, forKey: .address)
        try c.encodeIfPresent(profileType, forKey: .profileType)
        try c.encodeIfPresent(chats, forKey: .chats)
        try c.encodeIfPresent(photoBytes, forKey: .photoBytes)

        if let date = registeredAt {
            let s = DateFormatters.userDateTime.string(from: date)
            try c.encode(s, forKey: .registeredAt)
        }

        try c.encodeIfPresent(businessId, forKey: .businessId)
        try c.encodeIfPresent(factor, forKey: .factor)
        try c.encodeIfPresent(factoryName, forKey: .factoryName)
    }
}
