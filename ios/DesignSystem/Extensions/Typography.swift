import UIKit

enum AppFont {
    static func title() -> UIFont { .systemFont(ofSize: 28, weight: .bold) } // 28sp
    static func body()  -> UIFont { .systemFont(ofSize: 16, weight: .regular) }
    static func small() -> UIFont { .systemFont(ofSize: 14, weight: .regular) }
    static func button()-> UIFont { .systemFont(ofSize: 16, weight: .bold) }
}
