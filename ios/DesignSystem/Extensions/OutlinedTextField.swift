import UIKit

final class OutlinedTextField: UITextField {

    private let padding = UIEdgeInsets(top: 0, left: 14, bottom: 0, right: 14)
    private let height: CGFloat = 52
    private let corner: CGFloat = 14
    private let borderLayer = CALayer()

    init(placeholder: String) {
        super.init(frame: .zero)
        translatesAutoresizingMaskIntoConstraints = false
        self.placeholder = placeholder
        backgroundColor = .clear
        layer.cornerRadius = corner
        layer.masksToBounds = true
        font = AppFont.body()
        textColor = AppColors.textPrimary
        setupBorder()
        heightAnchor.constraint(equalToConstant: height).isActive = true
    }

    required init?(coder: NSCoder) { fatalError("init(coder:) has not been implemented") }

    private func setupBorder() {
        borderStyle = .none
        borderLayer.borderColor = AppColors.border.cgColor
        borderLayer.borderWidth = 1
        borderLayer.frame = bounds
        borderLayer.cornerRadius = 14
        layer.addSublayer(borderLayer)
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        borderLayer.frame = bounds
    }

    // padding לטקסט
    override func textRect(forBounds bounds: CGRect) -> CGRect { bounds.inset(by: padding) }
    override func editingRect(forBounds bounds: CGRect) -> CGRect { bounds.inset(by: padding) }
    override func placeholderRect(forBounds bounds: CGRect) -> CGRect { bounds.inset(by: padding) }
}
