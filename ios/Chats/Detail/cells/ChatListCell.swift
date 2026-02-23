import UIKit
import CoreModelsSDK

final class ChatListCell: UITableViewCell {

    private let profileImageView: UIImageView = {
        let iv = UIImageView()
        iv.translatesAutoresizingMaskIntoConstraints = false
        iv.contentMode = .scaleAspectFill
        iv.clipsToBounds = true
        iv.layer.cornerRadius = 26 // חצי מגודל התמונה (52dp באנדרואיד)
        iv.image = UIImage(named: "ic_placeholder_user") // תמונת ברירת מחדל
        iv.layer.borderWidth = 1
        iv.layer.borderColor = UIColor.lightGray.cgColor
        return iv
    }()

    private let nameLabel: UILabel = {
        let label = UILabel()
        label.font = .boldSystemFont(ofSize: 16)
        label.textColor = .black
        label.numberOfLines = 1
        return label
    }()

    private let lastMessageLabel: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 14)
        label.textColor = .darkGray
        label.numberOfLines = 1
        return label
    }()

    private let timestampLabel: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 12)
        label.textColor = .gray
        return label
    }()

    private let unreadCountLabel: UILabel = {
        let label = UILabel()
        label.font = .boldSystemFont(ofSize: 12)
        label.textColor = .white
        label.backgroundColor = .systemRed
        label.textAlignment = .center
        label.layer.cornerRadius = 10
        label.clipsToBounds = true
        label.isHidden = true
        return label
    }()

    private let verticalStack = UIStackView()
    private let horizontalStack = UIStackView()

    // MARK: Init
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupLayout()
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    // MARK: Layout
    private func setupLayout() {
        selectionStyle = .none

        verticalStack.axis = .vertical
        verticalStack.alignment = .leading
        verticalStack.spacing = 4
        verticalStack.translatesAutoresizingMaskIntoConstraints = false
        verticalStack.addArrangedSubview(nameLabel)
        verticalStack.addArrangedSubview(lastMessageLabel)

        horizontalStack.axis = .horizontal
        horizontalStack.alignment = .center
        horizontalStack.spacing = 12
        horizontalStack.translatesAutoresizingMaskIntoConstraints = false
        horizontalStack.addArrangedSubview(profileImageView)
        horizontalStack.addArrangedSubview(verticalStack)
        horizontalStack.addArrangedSubview(timestampLabel)
        horizontalStack.addArrangedSubview(unreadCountLabel)

        contentView.addSubview(horizontalStack)

        NSLayoutConstraint.activate([
            profileImageView.widthAnchor.constraint(equalToConstant: 52),
            profileImageView.heightAnchor.constraint(equalToConstant: 52),

            unreadCountLabel.widthAnchor.constraint(greaterThanOrEqualToConstant: 20),
            unreadCountLabel.heightAnchor.constraint(equalToConstant: 20),

            horizontalStack.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 12),
            horizontalStack.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            horizontalStack.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16),
            horizontalStack.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -12)
        ])
    }

    // MARK: Configure
    func configure(with item: ChatDisplayItem) {
        nameLabel.text = item.otherUserName
        lastMessageLabel.text = item.chat.lastMessage ?? "No messages yet"
        timestampLabel.text = item.formattedTime

        if item.unreadCount > 0 {
            unreadCountLabel.isHidden = false
            unreadCountLabel.text = "\(item.unreadCount)"
        } else {
            unreadCountLabel.isHidden = true
        }
    }

}
