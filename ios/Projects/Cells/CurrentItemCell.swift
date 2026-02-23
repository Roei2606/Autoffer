import UIKit
import LocalProjectSDK

final class CurrentItemCell: UITableViewCell {
    
    private let titleLabel = UILabel()
    private let subtitleLabel = UILabel()
    private let priceLabel = UILabel()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupUI() {
        titleLabel.font = .boldSystemFont(ofSize: 16)
        subtitleLabel.font = .systemFont(ofSize: 14)
        subtitleLabel.textColor = .darkGray
        priceLabel.font = .systemFont(ofSize: 14, weight: .semibold)
        
        let stack = UIStackView(arrangedSubviews: [titleLabel, subtitleLabel, priceLabel])
        stack.axis = .vertical
        stack.spacing = 4
        stack.translatesAutoresizingMaskIntoConstraints = false
        
        contentView.addSubview(stack)
        
        NSLayoutConstraint.activate([
            stack.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 8),
            stack.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 12),
            stack.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -12),
            stack.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -8)
        ])
    }
    
    func configure(with item: LocalItemEntity) {
        titleLabel.text = "Profile: \(item.profileNumber) | Glass: \(item.glassType)"
        subtitleLabel.text = "Size: \(Int(item.width))x\(Int(item.height)) | Qty: \(item.quantity) | Pos: \(item.location)"
        priceLabel.text = item.isExpensive ? "$$" : "$"
    }
}
