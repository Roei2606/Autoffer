import Foundation
import CoreModelsSDK   // UserType

public struct UserProjectRequest: Codable, Hashable {
    public var userId: String
    public var profileType: UserType

    public init(userId: String, profileType: UserType) {
        self.userId = userId
        self.profileType = profileType
    }
}
