import UIKit

/// @MainActor isolates both the cache and any UI mutations
@MainActor
public final class ImageLoader {

    public static let shared = ImageLoader()

    // שמור בזיכרון – בטוח כי הכל רץ תחת MainActor
    private let cache = NSCache<NSURL, UIImage>()

    private init() {}

    /// Async API (מומלץ)
    public func loadImage(into imageView: UIImageView, from urlString: String?) async {
        imageView.image = nil

        guard let urlString, let url = URL(string: urlString) else { return }
        let key = url as NSURL

        // 1) cache hit
        if let cached = cache.object(forKey: key) {
            imageView.image = cached
            return
        }

        // 2) fetch
        do {
            let (data, _) = try await URLSession.shared.data(from: url)
            guard let image = UIImage(data: data) else { return }

            cache.setObject(image, forKey: key)
            imageView.image = image
        } catch {
            // אפשר להוסיף לוג/placeholder
        }
    }

    /// Non-async convenience (wraps the async one)
    public func loadImage(into imageView: UIImageView, urlString: String?) {
        Task { await loadImage(into: imageView, from: urlString) }
    }
}
