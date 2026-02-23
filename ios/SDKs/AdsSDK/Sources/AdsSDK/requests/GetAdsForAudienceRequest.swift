import Foundation
import CoreModelsSDK

public struct GetAdsForAudienceRequest: Codable {
    public let profileType: UserType
    public init(profileType: UserType) { self.profileType = profileType }
}
