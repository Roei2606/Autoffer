import UIKit

class ResetPasswordViewController: UIViewController {
    
    private let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "Reset Password"
        label.textColor = .white
        label.font = UIFont.boldSystemFont(ofSize: 24)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let newPasswordField: UITextField = {
        let field = UITextField()
        field.placeholder = "New Password"
        field.isSecureTextEntry = true
        field.backgroundColor = .white
        field.textColor = .darkGray
        field.layer.cornerRadius = 8
        field.setLeftPaddingPoints(12)
        field.translatesAutoresizingMaskIntoConstraints = false
        return field
    }()
    
    private let confirmPasswordField: UITextField = {
        let field = UITextField()
        field.placeholder = "Confirm Password"
        field.isSecureTextEntry = true
        field.backgroundColor = .white
        field.textColor = .darkGray
        field.layer.cornerRadius = 8
        field.setLeftPaddingPoints(12)
        field.translatesAutoresizingMaskIntoConstraints = false
        return field
    }()
    
    private let submitButton: UIButton = {
        let button = UIButton(type: .system)
        button.setTitle("Submit", for: .normal)
        button.backgroundColor = .white
        button.setTitleColor(.black, for: .normal)
        button.layer.cornerRadius = 8
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = .black
        setupLayout()
    }
    
    private func setupLayout() {
        view.addSubview(titleLabel)
        view.addSubview(newPasswordField)
        view.addSubview(confirmPasswordField)
        view.addSubview(submitButton)
        
        NSLayoutConstraint.activate([
            // Title
            titleLabel.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 40),
            titleLabel.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            
            // New Password Field
            newPasswordField.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 40),
            newPasswordField.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            newPasswordField.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            newPasswordField.heightAnchor.constraint(equalToConstant: 48),
            
            // Confirm Password Field
            confirmPasswordField.topAnchor.constraint(equalTo: newPasswordField.bottomAnchor, constant: 16),
            confirmPasswordField.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            confirmPasswordField.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            confirmPasswordField.heightAnchor.constraint(equalToConstant: 48),
            
            // Submit Button
            submitButton.topAnchor.constraint(equalTo: confirmPasswordField.bottomAnchor, constant: 24),
            submitButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            submitButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            submitButton.heightAnchor.constraint(equalToConstant: 50)
        ])
    }
    
}
