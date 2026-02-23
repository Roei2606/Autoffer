import UIKit
import LocalProjectSDK
import ProjectsSDK

final class CurrentProjectViewController: UIViewController {
    
    private let tableView = UITableView()
    private let addButton = UIButton(type: .system)
    private let saveButton = UIButton(type: .system)
    
    private let viewModel = CurrentProjectViewModel()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        title = "Current Project"
        view.backgroundColor = .systemBackground
        setupUI()
        Task { await viewModel.loadItems(); tableView.reloadData() }
    }
    
    private func setupUI() {
        tableView.register(ProjectItemCell.self, forCellReuseIdentifier: "ProjectItemCell")
        tableView.delegate = self
        tableView.dataSource = self
        tableView.translatesAutoresizingMaskIntoConstraints = false
        
        addButton.setTitle("Add Another Product", for: .normal)
        addButton.setTitleColor(.white, for: .normal)
        addButton.backgroundColor = .black
        addButton.layer.cornerRadius = 8
        addButton.translatesAutoresizingMaskIntoConstraints = false
        addButton.addTarget(self, action: #selector(onAddAnother), for: .touchUpInside)
        
        saveButton.setTitle("Save Project", for: .normal)
        saveButton.setTitleColor(.white, for: .normal)
        saveButton.backgroundColor = .darkGray
        saveButton.layer.cornerRadius = 8
        saveButton.translatesAutoresizingMaskIntoConstraints = false
        saveButton.addTarget(self, action: #selector(onSaveProject), for: .touchUpInside)
        
        view.addSubview(tableView)
        view.addSubview(addButton)
        view.addSubview(saveButton)
        
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 8),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 8),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -8),
            tableView.bottomAnchor.constraint(equalTo: addButton.topAnchor, constant: -16),
            
            addButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            addButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            addButton.heightAnchor.constraint(equalToConstant: 44),
            addButton.bottomAnchor.constraint(equalTo: saveButton.topAnchor, constant: -12),
            
            saveButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            saveButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            saveButton.heightAnchor.constraint(equalToConstant: 44),
            saveButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -12)
        ])
    }
    
    @objc private func onAddAnother() {
        navigationController?.popViewController(animated: true)
    }
    
    @objc private func onSaveProject() {
        Task {
            do {
                try await viewModel.saveProject()
                await MainActor.run {
                    let alert = UIAlertController(title: "Success",
                                                  message: "Project saved successfully!",
                                                  preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "OK", style: .default) { _ in
                        self.navigationController?.popToRootViewController(animated: true)
                    })
                    self.present(alert, animated: true)
                }
            } catch {
                print("❌ Save project failed:", error)
                await MainActor.run {
                    let alert = UIAlertController(title: "Error",
                                                  message: error.localizedDescription,
                                                  preferredStyle: .alert)
                    alert.addAction(UIAlertAction(title: "OK", style: .default))
                    self.present(alert, animated: true)
                }
            }

        }
    }
}

extension CurrentProjectViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return viewModel.items.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ProjectItemCell", for: indexPath) as! ProjectItemCell
        cell.configure(with: viewModel.items[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView,
                   commit editingStyle: UITableViewCell.EditingStyle,
                   forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            let item = viewModel.items[indexPath.row]
            Task {
                await viewModel.deleteItem(item)
                await MainActor.run { tableView.reloadData() }
            }
        }
    }
}
