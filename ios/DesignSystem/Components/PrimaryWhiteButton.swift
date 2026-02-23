import UIKit

final class PrimaryWhiteButton: UIButton {
    init(title: String) {
        super.init(frame: .zero)
        translatesAutoresizingMaskIntoConstraints = false
        setTitle(title, for: .normal)
        setTitleColor(AppColors.black, for: .normal)            // טקסט שחור
        titleLabel?.font = AppFont.button()
        backgroundColor = AppColors.white                       // רקע לבן
        layer.cornerRadius = 8
        heightAnchor.constraint(equalToConstant: 52).isActive = true
        contentEdgeInsets = .init(top: 0, left: 16, bottom: 0, right: 16)
    }
    required init?(coder: NSCoder) { fatalError("init(coder:) has not been implemented") }
}
