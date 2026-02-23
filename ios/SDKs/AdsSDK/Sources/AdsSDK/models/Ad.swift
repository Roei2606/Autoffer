import Foundation
import CoreModelsSDK

public struct Ad: Codable, Hashable {
    public let id: String?
    public let title: String?
    public let description: String?
    public let imageUrl: String?
    public let profileType: UserType?

    public init(id: String? = nil,
                title: String? = nil,
                description: String? = nil,
                imageUrl: String? = nil,
                profileType: UserType? = nil) {
        self.id = id
        self.title = title
        self.description = description
        self.imageUrl = imageUrl
        self.profileType = profileType
    }

    // מיפוי השם JSON "audience" לשדה profileType במודל
    private enum CodingKeys: String, CodingKey {
        case id, title, description, imageUrl
        case profileType = "audience"
    }
}
