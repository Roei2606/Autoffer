import Foundation

public enum JSONCoding {
    /// Encoder אחיד עם ISO-8601 (תואם Jackson בשרת)
    public static let encoder: JSONEncoder = {
        let enc = JSONEncoder()
        enc.dateEncodingStrategy = .iso8601
        // אם צריך snake_case ↔︎ camelCase, בטל/פעיל את השורה הבאה:
        // enc.keyEncodingStrategy = .convertToSnakeCase
        return enc
    }()

    /// Decoder אחיד עם ISO-8601
    public static let decoder: JSONDecoder = {
        let dec = JSONDecoder()
        dec.dateDecodingStrategy = .iso8601
        // dec.keyDecodingStrategy = .convertFromSnakeCase
        return dec
    }()
}
