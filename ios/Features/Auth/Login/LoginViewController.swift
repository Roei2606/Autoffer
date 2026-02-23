import UIKit
import Combine

final class LoginViewController: UIViewController {

    // MARK: - UI
    private let titleLabel: UILabel = {
        let l = UILabel()
        l.translatesAutoresizingMaskIntoConstraints = false
        l.text = "Login"
        l.textColor = AppColors.white
        l.font = AppFont.title()
        return l
    }()

    private let emailField = FilledTextField(placeholder: "Email", kind: .normal)
    private let passwordField = FilledTextField(placeholder: "Password", kind: .password(showToggle: true))

    private let forgotPassword: UIButton = {
        let b = UIButton(type: .system)
        b.translatesAutoresizingMaskIntoConstraints = false
        b.setTitle("Forgot password?", for: .normal)
        b.setTitleColor(AppColors.hint, for: .normal)
        b.titleLabel?.font = AppFont.small()
        b.contentHorizontalAlignment = .right
        return b
    }()

    private let loginButton = PrimaryWhiteButton(title: "Login")

    private let registerLink: UIButton = {
        let b = UIButton(type: .system)
        b.translatesAutoresizingMaskIntoConstraints = false
        b.setTitle("Don't have an account? Register", for: .normal)
        b.setTitleColor(AppColors.hint, for: .normal)
        b.titleLabel?.font = UIFont.italicSystemFont(ofSize: 14)
        return b
    }()

    private let statusLabel: UILabel = {
        let l = UILabel()
        l.translatesAutoresizingMaskIntoConstraints = false
        l.textAlignment = .center
        l.textColor = AppColors.hint
        l.font = AppFont.small()
        l.text = ""
        return l
    }()

    private let spinner: UIActivityIndicatorView = {
        let s = UIActivityIndicatorView(style: .medium)
        s.translatesAutoresizingMaskIntoConstraints = false
        s.hidesWhenStopped = true
        return s
    }()

    // MARK: - MVVM
    private let viewModel = LoginViewModel()
    private var cancellables = Set<AnyCancellable>()

    // MARK: - Lifecycle
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = AppColors.bg

        registerLink.addTarget(self, action: #selector(didTapRegister), for: .touchUpInside)
      //  forgotPassword.addTarget(self, action: #selector(didTapForgot), for: .touchUpInside)
        loginButton.addTarget(self, action: #selector(didTapLogin), for: .touchUpInside)

        let vstack = UIStackView(arrangedSubviews: [
            titleLabel,
            emailField,
            passwordField,
            forgotPassword,
            loginButton,
            registerLink,
            statusLabel,
            spinner
        ])
        vstack.axis = .vertical
        vstack.spacing = 16
        vstack.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(vstack)

        NSLayoutConstraint.activate([
            vstack.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 32),
            vstack.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -32),
            vstack.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 32)
        ])
        vstack.setCustomSpacing(32, after: titleLabel)
        vstack.setCustomSpacing(16, after: emailField)
        vstack.setCustomSpacing(8,  after: passwordField)
        vstack.setCustomSpacing(24, after: forgotPassword)
        vstack.setCustomSpacing(16, after: loginButton)

        forgotPassword.contentHorizontalAlignment = .right

        bindViewModel()
        viewModel.start()
    }

    private func bindViewModel() {
        // STATUS (non-optional)
        viewModel.$statusText
            .receive(on: DispatchQueue.main)
            .sink { [weak self] (text: String) in
                self?.statusLabel.text = text
            }
            .store(in: &cancellables)

        // LOADING
        viewModel.$isLoading
            .receive(on: DispatchQueue.main)
            .sink { [weak self] loading in
                self?.loginButton.isEnabled = !loading
                loading ? self?.spinner.startAnimating() : self?.spinner.stopAnimating()
            }
            .store(in: &cancellables)

        // ERROR (unwrap Optional<String> → String)
        viewModel.$errorMessage
            .compactMap { $0 }                                // <-- unwrap
            .receive(on: DispatchQueue.main)
            .sink { [weak self] (msg: String) in              // <-- explicit type helps inference
                self?.showAlert(title: "Login", message: msg)
                if msg.lowercased().contains("email")    { self?.emailField.setError(msg) }
                if msg.lowercased().contains("password") { self?.passwordField.setError(msg) }
            }
            .store(in: &cancellables)

        // SUCCESS
        viewModel.$didLogin
            .filter { $0 }
            .receive(on: DispatchQueue.main)
            .sink { [weak self] _ in self?.goToHome() }
            .store(in: &cancellables)
    }


    // MARK: - Actions
    @objc private func didTapRegister() {
        let vc = RegisterViewController()
        vc.title = "Register"
        navigationItem.backButtonTitle = ""
        if let nav = navigationController {
            nav.pushViewController(vc, animated: true)
        } else {
            present(UINavigationController(rootViewController: vc), animated: true)
        }
    }

//    @objc private func didTapForgot() {
//        let vc = ForgotPasswordViewController()
//        vc.title = "Forgot Password"
//        navigationItem.backButtonTitle = ""
//        if let nav = navigationController {
//            nav.pushViewController(vc, animated: true)
//        } else {
//            present(UINavigationController(rootViewController: vc), animated: true)
//        }
//    }

    @objc private func didTapLogin() {
        viewModel.email = emailField.text?.trimmingCharacters(in: .whitespacesAndNewlines) ?? ""
        viewModel.password = passwordField.text?.trimmingCharacters(in: .whitespacesAndNewlines) ?? ""
        viewModel.login()
    }

    @objc private func textDidChange(_ sender: UITextField) {
        if sender === emailField { viewModel.email = sender.text ?? "" }
        else if sender === passwordField { viewModel.password = sender.text ?? "" }
    }

    // MARK: - Helpers
    private func showAlert(title: String, message: String) {
        let ac = UIAlertController(title: title, message: message, preferredStyle: .alert)
        ac.addAction(UIAlertAction(title: "OK", style: .default))
        present(ac, animated: true)
    }

    private func goToHome() {
        let destination = MainTabBarController()
        if let nav = navigationController {
            nav.setViewControllers([destination], animated: true)
        } else {
            destination.modalPresentationStyle = .fullScreen
            present(destination, animated: true)
        }
    }
}
