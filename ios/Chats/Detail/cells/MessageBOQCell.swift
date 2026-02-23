import UIKit
import CoreModelsSDK
import ChatSDK

final class MessageBOQCell: UITableViewCell {
    static let reuseId = "BoqMessageCell"

    var onViewPdf: (() -> Void)?
    var onAccept: (() -> Void)?
    var onReject: (() -> Void)?

    private let projectLabel = UILabel()
    private let pdfButton = UIButton(type: .system)
    private let acceptButton = UIButton(type: .system)
    private let rejectButton = UIButton(type: .system)

    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        selectionStyle = .none

        projectLabel.font = .boldSystemFont(ofSize: 14)
        projectLabel.text = "Project: ..."

        pdfButton.setTitle("View BOQ PDF", for: .normal)
        acceptButton.setTitle("Accept", for: .normal)
        rejectButton.setTitle("Reject", for: .normal)

        let stack = UIStackView(arrangedSubviews: [projectLabel, pdfButton, acceptButton, rejectButton])
        stack.axis = .vertical
        stack.spacing = 8
        contentView.addSubview(stack)
        stack.translatesAutoresizingMaskIntoConstraints = false

        NSLayoutConstraint.activate([
            stack.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 8),
            stack.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -8),
            stack.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            stack.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16)
        ])

        pdfButton.addTarget(self, action: #selector(viewPdfTapped), for: .touchUpInside)
        acceptButton.addTarget(self, action: #selector(acceptTapped), for: .touchUpInside)
        rejectButton.addTarget(self, action: #selector(rejectTapped), for: .touchUpInside)
    }

    required init?(coder: NSCoder) { fatalError() }

    func configure(with msg: Message) {
        projectLabel.text = "Project: \(msg.metadata?["projectAddress"] ?? "N/A")"
    }

    @objc private func viewPdfTapped() { onViewPdf?() }
    @objc private func acceptTapped() { onAccept?() }
    @objc private func rejectTapped() { onReject?() }
}
