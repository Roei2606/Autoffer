import Foundation
import UIKit
@preconcurrency import AdsSDK     // נשאר כדי להשתיק אזהרות concurrency אם הספרייה לא מסומנת Sendable
import CoreModelsSDK

enum AdVisual: Hashable {
    case url(URL)
    case data(Data)
}

struct AdViewData: Hashable {
    let id: String
    let visual: AdVisual
}

@MainActor
final class HomeViewModel: ObservableObject {

    // MARK: Outputs
    @Published private(set) var greeting: String = "Hello, Guest!"
    @Published private(set) var userTypeText: String = "Unknown Type"
    @Published private(set) var avatar: UIImage?
    @Published private(set) var adItems: [AdViewData] = []
    @Published private(set) var isLoadingAds: Bool = false
    @Published private(set) var errorMessage: String?

    // MARK: Deps
    private let env: AppEnvironment
    private var adsService: (any AdsManaging)?

    // אפשר להזין env מותאם בבדיקות
    init(env: AppEnvironment) { self.env = env }

    convenience init() { self.init(env: AppEnvironment.shared) }

    func start() {
        Task { @MainActor in
            await populateUserCard()

            // אם ה־env לא סיפק שירות, נ fallback ל־AdsManager() (קורא GatewayBaseURL מ־Info.plist)
            self.adsService = env.adsManager ?? AdsManager()

            await loadAds()
        }
    }

    // MARK: - User card
    private func populateUserCard() async {
        guard let user = await env.session.currentUser else {
            greeting = "Hello, Guest!"
            userTypeText = "Unknown Type"
            avatar = nil
            return
        }

        let first = user.firstName ?? ""
        greeting = first.isEmpty ? "Hello!" : "Hello, \(first)!"

        if let type = user.profileType {
            userTypeText = type.rawValue.replacingOccurrences(of: "_", with: " ")
        } else {
            userTypeText = "Unknown Type"
        }

        avatar = imageFromUser(user) ?? UIImage(named: "ic_placeholder_user")
    }

    // MARK: - Ads
    private func loadAds() async {
        guard
            let user = await env.session.currentUser,
            let type = user.profileType,
            let adsService
        else {
            return
        }

        isLoadingAds = true
        errorMessage = nil
        defer { isLoadingAds = false }

        do {
            let ads: [Ad] = try await adsService.getAds(for: type)

            self.adItems = ads.compactMap { ad in
                if let url = extractURL(from: ad) {
                    return AdViewData(id: extractID(from: ad), visual: .url(url))
                }
                if let data = extractData(from: ad) {
                    return AdViewData(id: extractID(from: ad), visual: .data(data))
                }
                return nil
            }
        } catch {
            self.errorMessage = error.localizedDescription
            self.adItems = []
        }
    }

    // MARK: - Extraction helpers (גמישים לשמות שדות שונים ב-DTO)
    private func extractID(from ad: Ad) -> String {
        // אם יש לך שדה ידוע, לדוגמה:
        // if let id = (ad as? YourConcreteAdType)?.id { return id }
        let m = Mirror(reflecting: ad)
        for c in m.children {
            guard let name = c.label?.lowercased() else { continue }
            if name == "id" || name.hasSuffix("id") || name.contains("identifier") {
                return String(describing: c.value)
            }
        }
        return UUID().uuidString
    }

    private func extractURL(from ad: Ad) -> URL? {
        // אם יש לך שדה ידוע, לדוגמה:
        // if let s = (ad as? YourConcreteAdType)?.imageUrl, let u = URL(string: s) { return u }
        let m = Mirror(reflecting: ad)
        for c in m.children {
            guard let name = c.label?.lowercased() else { continue }
            if name.contains("url"), let s = c.value as? String, let u = URL(string: s) { return u }
            if let u = c.value as? URL { return u }
        }
        return nil
    }

    private func extractData(from ad: Ad) -> Data? {
        // אם יש לך שדה ידוע (לדוגמה imageBytes: [Int] או Data) – תעדיף אותו
        let m = Mirror(reflecting: ad)
        for c in m.children {
            if let d = c.value as? Data { return d }
            if let bytes = c.value as? [UInt8] { return Data(bytes) }
            if let s = c.value as? String, let d = Data(base64Encoded: s) { return d }
        }
        return nil
    }

    private func imageFromUser(_ user: User) -> UIImage? {
        // אם יש לך טיפוס קונקרטי עם מאפיין ידוע (למשל user.photoBytes) – תעדיף אותו
        let m = Mirror(reflecting: user)
        for c in m.children {
            guard let name = c.label?.lowercased() else { continue }
            if name.contains("photo") || name.contains("avatar") || name.contains("image") || name.contains("logo") {
                if let d = c.value as? Data { return UIImage(data: d) }
                if let bytes = c.value as? [UInt8] { return UIImage(data: Data(bytes)) }
                if let b64 = c.value as? String, let d = Data(base64Encoded: b64) { return UIImage(data: d) }
            }
        }
        return nil
    }
}
