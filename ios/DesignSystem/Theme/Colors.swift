import UIKit

enum AppColors {
    static let bg        = UIColor(hex: 0x1E1E1E)
    static let filledBox = UIColor(hex: 0x252525)
    static let hint      = UIColor(hex: 0xD0D0D0)
    static let white     = UIColor.white
    static let black     = UIColor.black
    static let toggleUnselected = UIColor(hex: 0x444444)

    // חדשים ↓
    static let textPrimary = UIColor.label
    static let border = UIColor { trait in
        trait.userInterfaceStyle == .dark
        ? UIColor(white: 1.0, alpha: 0.25)
        : UIColor(white: 0.0, alpha: 0.20)
    }

    // (אופציונלי, לשמירה על תאימות אם השתמשנו קודם)
    static let primary = UIColor { $0.userInterfaceStyle == .dark ? .white : .black }
    static let buttonFg = UIColor { $0.userInterfaceStyle == .dark ? .black : .white }
}

extension UIColor {
    convenience init(hex: UInt32, alpha: CGFloat = 1.0) {
        self.init(red: CGFloat((hex >> 16) & 0xff)/255.0,
                  green: CGFloat((hex >> 8) & 0xff)/255.0,
                  blue:  CGFloat(hex & 0xff)/255.0,
                  alpha: alpha)
    }
}
