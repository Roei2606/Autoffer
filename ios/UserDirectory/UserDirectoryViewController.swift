import UIKit

class UserDirectoryViewController: UIViewController {

    private let toggleGroup: UISegmentedControl = {
        let control = UISegmentedControl(items: ["Architects", "Factories"])
        control.selectedSegmentIndex = 0
        control.backgroundColor = .white
        control.selectedSegmentTintColor = .black
        control.setTitleTextAttributes([.foregroundColor: UIColor.black], for: .normal)
        control.setTitleTextAttributes([.foregroundColor: UIColor.white], for: .selected)
        control.translatesAutoresizingMaskIntoConstraints = false
        return control
    }()

    private let usersTableView: UITableView = {
        let table = UITableView()
        table.translatesAutoresizingMaskIntoConstraints = false
        table.separatorStyle = .none
        table.backgroundColor = .clear
        return table
    }()

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor(white: 0.96, alpha: 1.0) // #F5F5F5
        title = "User Directory"

        setupLayout()
        setupTableView()
    }

    private func setupLayout() {
        view.addSubview(toggleGroup)
        view.addSubview(usersTableView)

        NSLayoutConstraint.activate([
            // 🔘 Toggle group
            toggleGroup.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            toggleGroup.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            toggleGroup.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            toggleGroup.heightAnchor.constraint(equalToConstant: 40),

            // 👥 TableView
            usersTableView.topAnchor.constraint(equalTo: toggleGroup.bottomAnchor, constant: 16),
            usersTableView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            usersTableView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            usersTableView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor)
        ])
    }

    private func setupTableView() {
        usersTableView.dataSource = self
        usersTableView.delegate = self
        usersTableView.register(UITableViewCell.self, forCellReuseIdentifier: "UserCell")
    }
}

extension UserDirectoryViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // 🟢 כאן תכניס את כמות המשתמשים (כרגע דמה)
        return 10
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "UserCell", for: indexPath)
        cell.textLabel?.text = "User \(indexPath.row + 1)"
        return cell
    }
}
