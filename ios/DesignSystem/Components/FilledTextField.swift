import UIKit

@MainActor
final class FilledTextField: UITextField {

    enum Kind { case normal, password(showToggle: Bool) }

    private let padding = UIEdgeInsets(top: 0, left: 14, bottom: 0, right: 14)
    private let corner: CGFloat = 8
    private let kind: Kind
    private let border = CALayer()

    // colors
    private var normalBorderColor: CGColor { AppColors.hint.withAlphaComponent(0.7).cgColor }
    private let errorBorderColor: CGColor = UIColor.systemRed.cgColor

    // track error state
    private var isErrored = false
    private var basePlaceholder: String = ""

    init(placeholder: String, kind: Kind = .normal) {
        self.kind = kind
        super.init(frame: .zero)
        translatesAutoresizingMaskIntoConstraints = false

        self.basePlaceholder = placeholder
        self.placeholder = placeholder
        backgroundColor = AppColors.filledBox
        textColor = AppColors.white
        font = AppFont.body()
        borderStyle = .none
        layer.cornerRadius = corner
        layer.masksToBounds = true

        // thin border
        border.borderColor = normalBorderColor
        border.borderWidth = 1
        layer.addSublayer(border)

        heightAnchor.constraint(equalToConstant: 52).isActive = true

        switch kind {
        case .normal:
            break
        case .password(let showToggle):
            isSecureTextEntry = true
            textContentType = .password
            if showToggle {
                let eye = UIButton(type: .system)
                eye.setImage(UIImage(systemName: "eye"), for: .normal)
                eye.tintColor = AppColors.white
                eye.addTarget(self, action: #selector(toggleSecure), for: .touchUpInside)
                rightView = eye
                rightViewMode = .always
            }
        }

        // placeholder color
        attributedPlaceholder = NSAttributedString(
            string: placeholder,
            attributes: [.foregroundColor: AppColors.hint]
        )

        // clear error styling while typing
        addTarget(self, action: #selector(onEditingChanged), for: .editingChanged)
        autocorrectionType = .no
        autocapitalizationType = .none
    }

    required init?(coder: NSCoder) { fatalError("init(coder:) has not been implemented") }

    override func layoutSubviews() {
        super.layoutSubviews()
        border.frame = bounds
        border.cornerRadius = layer.cornerRadius
    }

    // padding
    override func textRect(forBounds bounds: CGRect) -> CGRect { bounds.inset(by: padded(bounds)) }
    override func editingRect(forBounds bounds: CGRect) -> CGRect { bounds.inset(by: padded(bounds)) }
    override func placeholderRect(forBounds bounds: CGRect) -> CGRect { bounds.inset(by: padded(bounds)) }

    private func padded(_ bounds: CGRect) -> UIEdgeInsets {
        // leave room on the right if we show the eye button
        if rightView != nil { return UIEdgeInsets(top: 0, left: 14, bottom: 0, right: 40) }
        return padding
    }

    @objc private func toggleSecure() {
        isSecureTextEntry.toggle()
        if let btn = rightView as? UIButton {
            let name = isSecureTextEntry ? "eye" : "eye.slash"
            btn.setImage(UIImage(systemName: name), for: .normal)
        }
        // fix caret jump when toggling secure
        let tmp = text; text = nil; text = tmp
    }

    @objc private func onEditingChanged() {
        // typing clears error style
        if isErrored { setError(nil) }
    }

    // MARK: - Error API

    /// Show/clear inline error styling. Pass a message to mark error; pass nil to clear.
    func setError(_ message: String?) {
        if let message = message, !message.isEmpty {
            isErrored = true
            layer.borderWidth = 0 // we draw border via sublayer
            border.borderColor = errorBorderColor
            attributedPlaceholder = NSAttributedString(
                string: basePlaceholder,
                attributes: [.foregroundColor: UIColor.systemRed]
            )
            accessibilityHint = message
            shake()
        } else {
            isErrored = false
            border.borderColor = normalBorderColor
            attributedPlaceholder = NSAttributedString(
                string: basePlaceholder,
                attributes: [.foregroundColor: AppColors.hint]
            )
            accessibilityHint = nil
        }
    }

    private func shake() {
        let anim = CAKeyframeAnimation(keyPath: "transform.translation.x")
        anim.timingFunction = CAMediaTimingFunction(name: .linear)
        anim.duration = 0.3
        anim.values = [-6, 6, -5, 5, -3, 3, 0]
        layer.add(anim, forKey: "shake")
    }
}
