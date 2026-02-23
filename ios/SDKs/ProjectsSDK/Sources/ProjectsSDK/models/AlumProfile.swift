import Foundation
import UIKit

public struct AlumProfile: Codable, Hashable {
    public let id: String?
    public let profileNumber: String?
    public let usageType: String?
    public let minHeight: Int?
    public let maxHeight: Int?
    public let minWidth: Int?
    public let maxWidth: Int?
    public let isExpensive: Bool?
    public let recommendedGlassType: String?
    public let pricePerSquareMeter: Double?
    public let imageData: [Int]?    // ✅ מגיע מהשרת כמערך מספרים
    
    // ממיר את ה־[UInt8] ל־UIImage לשימוש נוח
    public var uiImage: UIImage? {
            guard let bytes = imageData else { return nil }
            let uint8Array = bytes.map { UInt8(truncatingIfNeeded: $0) } // המרה ל־UInt8
            return UIImage(data: Data(uint8Array))
        }
}

