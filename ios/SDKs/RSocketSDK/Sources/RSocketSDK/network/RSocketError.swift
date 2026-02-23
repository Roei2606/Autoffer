import Foundation

public enum RSocketError: Error, Sendable {
    // נשאר עבור תאימות לאחור
    case notConnected
    case transportClosed

    // קידוד/פענוח (שימושי גם ל-HTTP)
    case encodeFailed
    case decodeFailed

    // שגיאות שרת כלליות
    case serverError(String)

    // ⬇︎ חדשים – מותאמים ל-HTTP
    case requestFailed(status: Int, body: Data?)   // למשל HTTP 4xx/5xx
    case mappingFailed(String)                     // כשאין מיפוי route→HTTP/SSE
}
