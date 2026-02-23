import UIKit
import ProjectsSDK

final class ProjectCardCell: UITableViewCell {
    
    // MARK: - UI
    private let addressLabel = UILabel()
    private let detailsLabel = UILabel()
    private let statusLabel = UILabel()
    
    private let viewButton = UIButton(type: .system)
    private let requestButton = UIButton(type: .system)
    private let deleteButton = UIButton(type: .system)
    
    // callback handlers
    var onViewBOQ: (() -> Void)?
    var onRequestQuote: (() -> Void)?
    var onDelete: (() -> Void)?
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupUI() {
        selectionStyle = .none
        contentView.backgroundColor = .white
        contentView.layer.cornerRadius = 12
        contentView.layer.masksToBounds = false
        contentView.layer.shadowColor = UIColor.black.cgColor
        contentView.layer.shadowOpacity = 0.1
        contentView.layer.shadowRadius = 4
        contentView.layer.shadowOffset = CGSize(width: 0, height: 2)
        
        addressLabel.font = .boldSystemFont(ofSize: 16)
        addressLabel.textColor = .black
        
        detailsLabel.font = .systemFont(ofSize: 14)
        detailsLabel.textColor = .darkGray
        
        statusLabel.font = .systemFont(ofSize: 14, weight: .medium)
        statusLabel.textColor = .systemBlue
        statusLabel.numberOfLines = 0
        
        viewButton.setTitle("View BOQ", for: .normal)
        viewButton.setTitleColor(.white, for: .normal)
        viewButton.backgroundColor = .black
        viewButton.layer.cornerRadius = 6
        viewButton.addTarget(self, action: #selector(onViewTapped), for: .touchUpInside)
        
        requestButton.setTitle("Request Quote", for: .normal)
        requestButton.setTitleColor(.white, for: .normal)
        requestButton.backgroundColor = .systemBlue
        requestButton.layer.cornerRadius = 6
        requestButton.addTarget(self, action: #selector(onRequestTapped), for: .touchUpInside)
        
        deleteButton.setTitle("Delete", for: .normal)
        deleteButton.setTitleColor(.white, for: .normal)
        deleteButton.backgroundColor = .systemRed
        deleteButton.layer.cornerRadius = 6
        deleteButton.addTarget(self, action: #selector(onDeleteTapped), for: .touchUpInside)
        
        let buttonStack = UIStackView(arrangedSubviews: [viewButton, requestButton, deleteButton])
        buttonStack.axis = .horizontal
        buttonStack.spacing = 8
        buttonStack.distribution = .fillEqually
        
        let stack = UIStackView(arrangedSubviews: [addressLabel, detailsLabel, statusLabel, buttonStack])
        stack.axis = .vertical
        stack.spacing = 8
        stack.translatesAutoresizingMaskIntoConstraints = false
        
        contentView.addSubview(stack)
        
        NSLayoutConstraint.activate([
            stack.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 12),
            stack.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 12),
            stack.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -12),
            stack.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -12)
        ])
    }
    
    // MARK: - Configure
    func configure(with project: ProjectDTO) {
        addressLabel.text = project.projectAddress
        
        let itemCount = project.items?.count ?? 0
        detailsLabel.text = "Items: \(itemCount) | Date: \(project.createdAt ?? "-")"
        
        if let statuses = project.quoteStatuses, !statuses.isEmpty {
            let statusText = statuses.map { "\($0.key): \($0.value)" }.joined(separator: "\n")
            statusLabel.text = statusText
        } else {
            statusLabel.text = "No requests sent"
        }
    }

    // MARK: - Actions
    @objc private func onViewTapped() {
        onViewBOQ?()
    }
    
    @objc private func onRequestTapped() {
        onRequestQuote?()
    }
    
    @objc private func onDeleteTapped() {
        onDelete?()
    }
}
