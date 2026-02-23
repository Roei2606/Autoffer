import UIKit

final class PrimaryButton: UIButton {
    private let h: CGFloat = 54
    private let corner: CGFloat = 16

    init(title: String) {
        super.init(frame: .zero)
        translatesAutoresizingMaskIntoConstraints = false
        setTitle(title, for: .normal)
        setTitleColor(AppColors.buttonFg, for: .normal)
        titleLabel?.font = AppFont.button()
        backgroundColor = AppColors.primary
        layer.cornerRadius = corner
        heightAnchor.constraint(equalToConstant: h).isActive = true
        contentEdgeInsets = UIEdgeInsets(top: 0, left: 16, bottom: 0, right: 16)
    }

    required init?(coder: NSCoder) { fatalError("init(coder:) has not been implemented") }
}
