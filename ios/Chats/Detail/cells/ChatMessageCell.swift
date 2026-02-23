import UIKit
import CoreModelsSDK
import ChatSDK

final class ChatMessageCell: UITableViewCell {
    static let reuseId = "ChatMessageCell"

    private let messageLabel = UILabel()
    private let bubble = UIView()

    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        selectionStyle = .none
        messageLabel.numberOfLines = 0
        bubble.layer.cornerRadius = 16
        contentView.addSubview(bubble)
        bubble.addSubview(messageLabel)

        bubble.translatesAutoresizingMaskIntoConstraints = false
        messageLabel.translatesAutoresizingMaskIntoConstraints = false
    }

    required init?(coder: NSCoder) { fatalError() }

    func configure(with msg: Message, currentUserId: String) {
        messageLabel.text = msg.content
        let isMine = msg.senderId == currentUserId
        bubble.backgroundColor = isMine ? UIColor.systemBlue.withAlphaComponent(0.2) : UIColor.systemGray5

        NSLayoutConstraint.deactivate(bubble.constraints)
        NSLayoutConstraint.deactivate(messageLabel.constraints)

        NSLayoutConstraint.activate([
            messageLabel.topAnchor.constraint(equalTo: bubble.topAnchor, constant: 8),
            messageLabel.bottomAnchor.constraint(equalTo: bubble.bottomAnchor, constant: -8),
            messageLabel.leadingAnchor.constraint(equalTo: bubble.leadingAnchor, constant: 12),
            messageLabel.trailingAnchor.constraint(equalTo: bubble.trailingAnchor, constant: -12),

            bubble.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 4),
            bubble.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -4),
            isMine ?
                bubble.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16) :
                bubble.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16)
        ])
    }
}
