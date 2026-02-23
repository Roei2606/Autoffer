import Foundation
import UIKit

public struct Glass: Codable, Hashable {
    public let id: String?
    public let type: String?
    public let pricePerSquareMeter: Double?
    public let imageData: [Int]?   // ✅ לא UInt8

    public var uiImage: UIImage? {
        guard let bytes = imageData else { return nil }
        let uint8Array = bytes.map { UInt8(truncatingIfNeeded: $0) }
        return UIImage(data: Data(uint8Array))
    }
}

