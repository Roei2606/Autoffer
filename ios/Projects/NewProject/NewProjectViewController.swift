import UIKit
import LocalProjectSDK
import UsersSDK

final class NewProjectViewController: UIViewController {
    
    private let viewModel = NewProjectViewModel()
    
    private let scrollView = UIScrollView()
    private let contentView = UIStackView()
    
    private let projectAddressField: UITextField = {
        let textField = UITextField()
        textField.placeholder = "Project Address"
        textField.borderStyle = .roundedRect
        textField.backgroundColor = .white
        textField.layer.borderColor = UIColor.black.cgColor
        textField.layer.borderWidth = 1
        textField.layer.cornerRadius = 8
        textField.textColor = .black
        textField.translatesAutoresizingMaskIntoConstraints = false
        return textField
    }()
    
    private let chooseMethodLabel: UILabel = {
        let label = UILabel()
        label.text = "Add Products By:"
        label.font = UIFont.systemFont(ofSize: 16, weight: .medium)
        label.textColor = .black
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let buttonAddManual: UIButton = {
        let button = UIButton(type: .system)
        button.setImage(UIImage(named: "ic_add_manual"), for: .normal)
        button.tintColor = .black
        button.backgroundColor = .white
        button.layer.cornerRadius = 16
        button.contentMode = .scaleAspectFit
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private let textAddManual: UILabel = {
        let label = UILabel()
        label.text = "Add Manually"
        label.font = UIFont.systemFont(ofSize: 14, weight: .regular)
        label.textColor = .black
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let buttonAddCamera: UIButton = {
        let button = UIButton(type: .system)
        button.setImage(UIImage(named: "ic_add_camera"), for: .normal)
        button.tintColor = .black
        button.backgroundColor = .white
        button.layer.cornerRadius = 16
        button.contentMode = .scaleAspectFit
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private let textAddCamera: UILabel = {
        let label = UILabel()
        label.text = "Scan Space"
        label.font = UIFont.systemFont(ofSize: 14, weight: .regular)
        label.textColor = .black
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let buttonViewCurrentProject: UIButton = {
        let button = UIButton(type: .system)
        button.setTitle("View Current Project", for: .normal)
        button.setTitleColor(.white, for: .normal)
        button.titleLabel?.font = UIFont.boldSystemFont(ofSize: 16)
        button.backgroundColor = .black
        button.layer.cornerRadius = 16
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        title = "New Project"
        view.backgroundColor = UIColor(white: 0.96, alpha: 1.0)
        
        setupLayout()
        setupActions()
    }
    
    private func setupLayout() {
        scrollView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(scrollView)
        
        NSLayoutConstraint.activate([
            scrollView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            scrollView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            scrollView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            scrollView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
        
        contentView.axis = .vertical
        contentView.spacing = 16
        contentView.alignment = .center
        contentView.translatesAutoresizingMaskIntoConstraints = false
        scrollView.addSubview(contentView)
        
        NSLayoutConstraint.activate([
            contentView.topAnchor.constraint(equalTo: scrollView.topAnchor),
            contentView.leadingAnchor.constraint(equalTo: scrollView.leadingAnchor),
            contentView.trailingAnchor.constraint(equalTo: scrollView.trailingAnchor),
            contentView.bottomAnchor.constraint(equalTo: scrollView.bottomAnchor),
            contentView.widthAnchor.constraint(equalTo: scrollView.widthAnchor)
        ])
        
        // Project Address
        contentView.addArrangedSubview(projectAddressField)
        projectAddressField.widthAnchor.constraint(equalTo: contentView.widthAnchor).isActive = true
        projectAddressField.heightAnchor.constraint(equalToConstant: 48).isActive = true
        
        // Add Products By
        contentView.addArrangedSubview(chooseMethodLabel)
        
        // Add Manual
        contentView.addArrangedSubview(buttonAddManual)
        buttonAddManual.widthAnchor.constraint(equalToConstant: 200).isActive = true
        buttonAddManual.heightAnchor.constraint(equalToConstant: 200).isActive = true
        contentView.addArrangedSubview(textAddManual)
        
        // Add Camera
        contentView.addArrangedSubview(buttonAddCamera)
        buttonAddCamera.widthAnchor.constraint(equalToConstant: 200).isActive = true
        buttonAddCamera.heightAnchor.constraint(equalToConstant: 200).isActive = true
        contentView.addArrangedSubview(textAddCamera)
        
        // View Current Project
        contentView.addArrangedSubview(buttonViewCurrentProject)
        buttonViewCurrentProject.widthAnchor.constraint(equalTo: contentView.widthAnchor).isActive = true
        buttonViewCurrentProject.heightAnchor.constraint(equalToConstant: 50).isActive = true
    }
    
    private func setupActions() {
        buttonAddManual.addTarget(self, action: #selector(onAddManual), for: .touchUpInside)
        buttonAddCamera.addTarget(self, action: #selector(onAddCamera), for: .touchUpInside)
        buttonViewCurrentProject.addTarget(self, action: #selector(onViewCurrent), for: .touchUpInside)
    }
    
    // MARK: - Actions
    @objc private func onAddManual() {
        Task {
            viewModel.address = projectAddressField.text ?? ""
            if let currentUser = await SessionManager.shared.currentUser,
               let userId = currentUser.id {
                await viewModel.startNewProject(for: userId)
                navigationController?.pushViewController(AddManualViewController(), animated: true)
            }
        }
    }
    
    @objc private func onAddCamera() {
        Task {
            viewModel.address = projectAddressField.text ?? ""
            if let currentUser = await SessionManager.shared.currentUser,
               let userId = currentUser.id {
                await viewModel.startNewProject(for: userId)
                navigationController?.pushViewController(AddCameraViewController(), animated: true)
            }
        }
    }
    
    @objc private func onViewCurrent() {
        navigationController?.pushViewController(CurrentProjectViewController(), animated: true)
    }
}
