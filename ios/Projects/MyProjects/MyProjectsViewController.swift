import UIKit
import Lottie
import ProjectsSDK
import CoreModelsSDK
import UsersSDK

final class MyProjectsViewController: UIViewController {
    
    private let viewModel = MyProjectsViewModel()
    
    private let titleLabel: UILabel = {
        let label = UILabel()
        label.text = "My Projects"
        label.font = UIFont.boldSystemFont(ofSize: 22)
        label.textColor = .black
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let loadingAnimation: LottieAnimationView = {
        let animationView = LottieAnimationView(name: "loading_animation")
        animationView.translatesAutoresizingMaskIntoConstraints = false
        animationView.contentMode = .scaleAspectFit
        animationView.loopMode = .loop
        animationView.animationSpeed = 1.5
        animationView.isHidden = true
        return animationView
    }()
    
    private let noProjectsLabel: UILabel = {
        let label = UILabel()
        label.text = "No projects found"
        label.textColor = .gray
        label.font = UIFont.systemFont(ofSize: 16)
        label.isHidden = true
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let tableView: UITableView = {
        let table = UITableView()
        table.translatesAutoresizingMaskIntoConstraints = false
        table.separatorStyle = .singleLine
        table.tableFooterView = UIView()
        return table
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = UIColor(white: 0.96, alpha: 1.0)
        setupLayout()
        
        tableView.register(ProjectCardCell.self, forCellReuseIdentifier: "ProjectCardCell")
        tableView.delegate = self
        tableView.dataSource = self
        
        // ✅ טוענים פרויקטים לפי המשתמש המחובר
        Task {
            if let currentUser = await SessionManager.shared.currentUser,
               let userId = currentUser.id,
               let type = currentUser.profileType {
                await viewModel.loadProjects(for: userId, profileType: type)
            }
            updateUI()
        }
    }

    
    private func setupLayout() {
        view.addSubview(titleLabel)
        view.addSubview(loadingAnimation)
        view.addSubview(noProjectsLabel)
        view.addSubview(tableView)
        
        NSLayoutConstraint.activate([
            titleLabel.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 24),
            titleLabel.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            
            loadingAnimation.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 16),
            loadingAnimation.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            loadingAnimation.widthAnchor.constraint(equalToConstant: 150),
            loadingAnimation.heightAnchor.constraint(equalToConstant: 150),
            
            noProjectsLabel.topAnchor.constraint(equalTo: loadingAnimation.bottomAnchor, constant: 16),
            noProjectsLabel.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            
            tableView.topAnchor.constraint(equalTo: noProjectsLabel.bottomAnchor, constant: 16),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            tableView.bottomAnchor.constraint(equalTo: view.bottomAnchor, constant: -8)
        ])
    }
    
    private func updateUI() {
        if viewModel.isLoading {
            showLoading(true)
            showNoProjectsMessage(false)
            return
        }
        
        showLoading(false)
        if viewModel.projects.isEmpty {
            showNoProjectsMessage(true)
        } else {
            showNoProjectsMessage(false)
        }
        tableView.reloadData()
    }
    
    func showLoading(_ show: Bool) {
        loadingAnimation.isHidden = !show
        if show {
            loadingAnimation.play()
        } else {
            loadingAnimation.stop()
        }
    }
    
    func showNoProjectsMessage(_ show: Bool) {
        noProjectsLabel.isHidden = !show
    }
}

// MARK: - TableView
extension MyProjectsViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return viewModel.projects.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ProjectCardCell", for: indexPath) as! ProjectCardCell
        cell.configure(with: viewModel.projects[indexPath.row])
        return cell
    }
}
