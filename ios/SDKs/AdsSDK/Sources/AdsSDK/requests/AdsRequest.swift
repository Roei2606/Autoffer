import Foundation
import CoreModelsSDK

public struct AdsRequest: Codable, Hashable {
    public var audience: String

    public init(profileType: UserType) {
        // מקביל ל- profileType.name() בג׳אווה
        self.audience = profileType.rawValue
    }
}
