import UIKit
import Combine
import CoreModelsSDK

// MARK: - Helpers to reach inner UITextField of custom views
private extension UIView {
    func firstTextFieldDescendant() -> UITextField? {
        if let tf = self as? UITextField { return tf }
        for v in subviews {
            if let tf = v.firstTextFieldDescendant() { return tf }
        }
        return nil
    }
}

final class RegisterViewController: UIViewController, UIGestureRecognizerDelegate {

    // MARK: - ViewModel
    private let viewModel = RegisterViewModel()
    private var cancellables = Set<AnyCancellable>()

    // MARK: - UI
    private let scrollView: UIScrollView = {
        let sv = UIScrollView()
        sv.translatesAutoresizingMaskIntoConstraints = false
        sv.keyboardDismissMode = .interactive
        sv.alwaysBounceVertical = true
        sv.delaysContentTouches = false
        sv.canCancelContentTouches = true
        return sv
    }()
    private let contentView: UIView = {
        let v = UIView()
        v.translatesAutoresizingMaskIntoConstraints = false
        return v
    }()
    private let stack: UIStackView = {
        let s = UIStackView()
        s.translatesAutoresizingMaskIntoConstraints = false
        s.axis = .vertical
        s.spacing = 12
        return s
    }()

    private let titleLabel: UILabel = {
        let l = UILabel()
        l.translatesAutoresizingMaskIntoConstraints = false
        l.text = "Create Account"
        l.textColor = AppColors.white
        l.font = AppFont.title()
        return l
    }()

    private let firstNameField = FilledTextField(placeholder: "First Name")
    private let lastNameField  = FilledTextField(placeholder: "Last Name")

    private let emailField: FilledTextField = {
        let f = FilledTextField(placeholder: "Email")
        f.keyboardType = .emailAddress
        f.textContentType = .emailAddress
        return f
    }()

    private let passwordField = FilledTextField(placeholder: "Password", kind: .password(showToggle: true))
    private let confirmPasswordField = FilledTextField(placeholder: "Confirm Password", kind: .password(showToggle: true))

    private let phoneField: FilledTextField = {
        let f = FilledTextField(placeholder: "Phone Number")
        f.keyboardType = .phonePad
        f.textContentType = .telephoneNumber
        return f
    }()
    private let addressField = FilledTextField(placeholder: "Address")

    private let toggleContainer: UIStackView = {
        let s = UIStackView()
        s.translatesAutoresizingMaskIntoConstraints = false
        s.axis = .horizontal
        s.spacing = 8
        s.distribution = .fillEqually
        return s
    }()
    private let privateBtn: UIButton = {
        let b = UIButton(type: .system)
        b.setTitle("Private", for: .normal)
        b.titleLabel?.font = AppFont.body()
        b.setTitleColor(AppColors.white, for: .normal)
        b.backgroundColor = AppColors.toggleUnselected
        b.layer.cornerRadius = 8
        b.heightAnchor.constraint(equalToConstant: 44).isActive = true
        return b
    }()
    private let architectBtn: UIButton = {
        let b = UIButton(type: .system)
        b.setTitle("Architect", for: .normal)
        b.titleLabel?.font = AppFont.body()
        b.setTitleColor(AppColors.white, for: .normal)
        b.backgroundColor = AppColors.toggleUnselected
        b.layer.cornerRadius = 8
        b.heightAnchor.constraint(equalToConstant: 44).isActive = true
        return b
    }()
    private let factoryBtn: UIButton = {
        let b = UIButton(type: .system)
        b.setTitle("Factory", for: .normal)
        b.titleLabel?.font = AppFont.body()
        b.setTitleColor(AppColors.white, for: .normal)
        b.backgroundColor = AppColors.toggleUnselected
        b.layer.cornerRadius = 8
        b.heightAnchor.constraint(equalToConstant: 44).isActive = true
        return b
    }()

    private let registerButton = PrimaryWhiteButton(title: "Register")

    private let loginRedirect: UIButton = {
        let b = UIButton(type: .system)
        b.setTitle("Already have an account? Login", for: .normal)
        b.setTitleColor(AppColors.hint, for: .normal)
        b.titleLabel?.font = UIFont.italicSystemFont(ofSize: 14)
        return b
    }()

    private enum ProfileType { case privateCustomer, architect, factory }
    private var selectedProfile: ProfileType = .privateCustomer { didSet { updateToggleUI() } }

    // Keep direct refs to the inner textfields to re-apply hacks when needed
    private weak var passwordInnerTF: UITextField?
    private weak var confirmPasswordInnerTF: UITextField?

    // MARK: - Lifecycle

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = AppColors.bg
        setupScroll()
        layoutForm()
        setupToggle()
        wireActions()
        bindViewModel()
        preparePasswordFields()   // <- חשוב
        selectedProfile = .privateCustomer
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        // iOS לפעמים מחזיר את ה־overlay אחרי שהמסך מוצג – נחזק שוב
        reapplyPasswordHacks(passwordInnerTF)
        reapplyPasswordHacks(confirmPasswordInnerTF)
    }

    // MARK: - Build UI

    private func setupScroll() {
        view.addSubview(scrollView)
        scrollView.addSubview(contentView)

        NSLayoutConstraint.activate([
            scrollView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            scrollView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            scrollView.topAnchor.constraint(equalTo: view.topAnchor),
            scrollView.bottomAnchor.constraint(equalTo: view.bottomAnchor),

            contentView.leadingAnchor.constraint(equalTo: scrollView.contentLayoutGuide.leadingAnchor),
            contentView.trailingAnchor.constraint(equalTo: scrollView.contentLayoutGuide.trailingAnchor),
            contentView.topAnchor.constraint(equalTo: scrollView.contentLayoutGuide.topAnchor),
            contentView.bottomAnchor.constraint(equalTo: scrollView.contentLayoutGuide.bottomAnchor),

            contentView.widthAnchor.constraint(equalTo: scrollView.frameLayoutGuide.widthAnchor)
        ])
    }

    private func layoutForm() {
        contentView.layoutMargins = .init(top: 24, left: 24, bottom: 24, right: 24)

        contentView.addSubview(stack)
        NSLayoutConstraint.activate([
            stack.leadingAnchor.constraint(equalTo: contentView.layoutMarginsGuide.leadingAnchor),
            stack.trailingAnchor.constraint(equalTo: contentView.layoutMarginsGuide.trailingAnchor),
            stack.topAnchor.constraint(equalTo: contentView.layoutMarginsGuide.topAnchor),
            stack.bottomAnchor.constraint(equalTo: contentView.layoutMarginsGuide.bottomAnchor)
        ])

        let titleWrapper = UIView()
        titleWrapper.translatesAutoresizingMaskIntoConstraints = false
        titleWrapper.addSubview(titleLabel)
        NSLayoutConstraint.activate([
            titleLabel.centerXAnchor.constraint(equalTo: titleWrapper.centerXAnchor),
            titleLabel.topAnchor.constraint(equalTo: titleWrapper.topAnchor, constant: 32),
            titleLabel.bottomAnchor.constraint(equalTo: titleWrapper.bottomAnchor)
        ])
        stack.addArrangedSubview(titleWrapper)

        stack.setCustomSpacing(24, after: titleWrapper)

        stack.addArrangedSubview(firstNameField)
        stack.addArrangedSubview(lastNameField)
        stack.addArrangedSubview(emailField)
        stack.addArrangedSubview(passwordField)
        stack.addArrangedSubview(confirmPasswordField)
        stack.addArrangedSubview(phoneField)
        stack.addArrangedSubview(addressField)

        stack.setCustomSpacing(16, after: addressField)

        toggleContainer.addArrangedSubview(privateBtn)
        toggleContainer.addArrangedSubview(architectBtn)
        toggleContainer.addArrangedSubview(factoryBtn)
        stack.addArrangedSubview(toggleContainer)

        stack.setCustomSpacing(24, after: toggleContainer)
        stack.addArrangedSubview(registerButton)

        stack.setCustomSpacing(16, after: registerButton)
        let redirectWrapper = UIView()
        redirectWrapper.translatesAutoresizingMaskIntoConstraints = false
        redirectWrapper.addSubview(loginRedirect)
        loginRedirect.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            loginRedirect.centerXAnchor.constraint(equalTo: redirectWrapper.centerXAnchor),
            loginRedirect.topAnchor.constraint(equalTo: redirectWrapper.topAnchor),
            loginRedirect.bottomAnchor.constraint(equalTo: redirectWrapper.bottomAnchor)
        ])
        stack.addArrangedSubview(redirectWrapper)

        let spacer = UIView()
        spacer.translatesAutoresizingMaskIntoConstraints = false
        spacer.heightAnchor.constraint(equalToConstant: 24).isActive = true
        stack.addArrangedSubview(spacer)
    }

    private func setupToggle() {
        privateBtn.addTarget(self, action: #selector(didTapPrivate), for: .touchUpInside)
        architectBtn.addTarget(self, action: #selector(didTapArchitect), for: .touchUpInside)
        factoryBtn.addTarget(self, action: #selector(didTapFactory), for: .touchUpInside)
    }

    // MARK: - Strong Password OFF

    private func preparePasswordFields() {
        // מצא את ה-UITextField הפנימי של ה-FilledTextField
        passwordInnerTF = passwordField.firstTextFieldDescendant()
        confirmPasswordInnerTF = confirmPasswordField.firstTextFieldDescendant()

        [passwordInnerTF, confirmPasswordInnerTF].forEach { tf in
            // תחילה ניישם את כל ההגדרות
            reapplyPasswordHacks(tf)

            // ונחזק בכל תחילת עריכה / שינוי טקסט
            tf?.addTarget(self, action: #selector(reapplyPasswordHacksAction(_:)),
                          for: [.editingDidBegin, .editingChanged])
        }
    }

    @objc private func reapplyPasswordHacksAction(_ tf: UITextField) {
        reapplyPasswordHacks(tf)
    }

    private func reapplyPasswordHacks(_ tf: UITextField?) {
        guard let tf = tf else { return }
        // חשוב: .oneTimeCode הכי עקבי כדי למנוע Strong Password overlay
        tf.textContentType = .oneTimeCode
        tf.isSecureTextEntry = true
        if #available(iOS 12.0, *) { tf.passwordRules = nil }
        tf.autocorrectionType = .no
        tf.spellCheckingType   = .no
        tf.smartDashesType     = .no
        tf.smartQuotesType     = .no
        tf.smartInsertDeleteType = .no

        // ניקוי QuickType bar
        let item = tf.inputAssistantItem
        item.leadingBarButtonGroups = []
        item.trailingBarButtonGroups = []
    }

    // MARK: - Actions

    private func updateToggleUI() {
        func select(_ b: UIButton, selected: Bool) {
            b.layer.borderWidth = selected ? 1 : 0
            b.layer.borderColor = selected ? AppColors.white.withAlphaComponent(0.6).cgColor : UIColor.clear.cgColor
        }
        select(privateBtn,  selected: selectedProfile == .privateCustomer)
        select(architectBtn,selected: selectedProfile == .architect)
        select(factoryBtn,  selected: selectedProfile == .factory)
    }

    @objc private func didTapPrivate()   { selectedProfile = .privateCustomer }
    @objc private func didTapArchitect() { selectedProfile = .architect }
    @objc private func didTapFactory()   { selectedProfile = .factory }

    private func wireActions() {
        registerButton.addTarget(self, action: #selector(didTapRegister), for: .touchUpInside)
        loginRedirect.addTarget(self, action: #selector(didTapLoginRedirect), for: .touchUpInside)

        let tap = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard))
        tap.cancelsTouchesInView = false
        tap.delegate = self
        view.addGestureRecognizer(tap)
    }
    
    private func bindViewModel() {
        // כפתור Register במצב טעינה
        viewModel.$isLoading
            .receive(on: DispatchQueue.main)
            .sink { [weak self] isLoading in
                self?.registerButton.isEnabled = !isLoading
                self?.registerButton.alpha = isLoading ? 0.6 : 1.0
            }
            .store(in: &cancellables)

        // הודעות שגיאה
        viewModel.$errorMessage
            .compactMap { $0 }
            .receive(on: DispatchQueue.main)
            .sink { [weak self] msg in
                self?.showError(msg)
            }
            .store(in: &cancellables)

        // הצלחת הרשמה
        viewModel.$didRegister
            .filter { $0 }
            .receive(on: DispatchQueue.main)
            .sink { [weak self] _ in
                self?.showToast("Registered successfully!")
                self?.navigationController?.popViewController(animated: true)
            }
            .store(in: &cancellables)
    }


    // שלא יגנוב מגעים מהכפתורים/שדות
    func gestureRecognizer(_ g: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool {
        if touch.view is UIControl { return false }
        if let v = touch.view, v.isDescendant(of: registerButton) { return false }
        if let v = touch.view, v.isDescendant(of: toggleContainer) { return false }
        return true
    }

    @objc private func didTapRegister() {
        view.endEditing(true)

        let userType: UserType = {
            switch selectedProfile {
            case .privateCustomer: return .privateCustomer
            case .architect:       return .architect
            case .factory:         return .factory
            }
        }()

        Task { @MainActor in
            await viewModel.register(
                firstName: firstNameField.text?.trimmingCharacters(in: .whitespacesAndNewlines) ?? "",
                lastName:  lastNameField.text?.trimmingCharacters(in: .whitespacesAndNewlines) ?? "",
                email:     emailField.text?.trimmingCharacters(in: .whitespacesAndNewlines) ?? "",
                password:  passwordField.text ?? "",
                confirmPassword: confirmPasswordField.text ?? "",
                phone:     phoneField.text?.trimmingCharacters(in: .whitespacesAndNewlines) ?? "",
                address:   addressField.text?.trimmingCharacters(in: .whitespacesAndNewlines) ?? "",
                userType:  userType
            )
        }
    }

    @objc private func didTapLoginRedirect() { navigationController?.popViewController(animated: true) }
    @objc private func dismissKeyboard() { view.endEditing(true) }

    // MARK: - Alerts / Toasts

    private func showError(_ message: String) {
        let ac = UIAlertController(title: "Registration failed",
                                   message: message,
                                   preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: "OK", style: .default))
        present(ac, animated: true)
    }

    private func showToast(_ text: String) {
        let label = UILabel()
        label.text = text
        label.textColor = .white
        label.backgroundColor = UIColor.black.withAlphaComponent(0.7)
        label.textAlignment = .center
        label.numberOfLines = 0
        label.layer.cornerRadius = 8
        label.clipsToBounds = true
        label.alpha = 0

        label.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(label)
        NSLayoutConstraint.activate([
            label.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            label.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -24),
            label.leadingAnchor.constraint(greaterThanOrEqualTo: view.leadingAnchor, constant: 24),
            label.trailingAnchor.constraint(lessThanOrEqualTo: view.trailingAnchor, constant: -24)
        ])

        UIView.animate(withDuration: 0.2) { label.alpha = 1 }
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.2) {
            UIView.animate(withDuration: 0.25, animations: { label.alpha = 0 },
                           completion: { _ in label.removeFromSuperview() })
        }
    }
}
