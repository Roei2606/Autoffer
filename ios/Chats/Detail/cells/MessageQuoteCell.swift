import UIKit
import CoreModelsSDK
import ChatSDK

final class MessageQuoteCell: UITableViewCell {
    static let reuseId = "QuoteMessageCell"

    var onViewPdf: (() -> Void)?

    private let textLabel2 = UILabel()
    private let pdfButton = UIButton(type: .system)

    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        selectionStyle = .none

        textLabel2.text = "A quote has been sent"
        textLabel2.font = .systemFont(ofSize: 14)

        pdfButton.setTitle("View Quote PDF", for: .normal)

        let stack = UIStackView(arrangedSubviews: [textLabel2, pdfButton])
        stack.axis = .horizontal
        stack.spacing = 12
        contentView.addSubview(stack)
        stack.translatesAutoresizingMaskIntoConstraints = false

        NSLayoutConstraint.activate([
            stack.topAnchor.constraint(equalTo: contentView.topAnchor, constant: 8),
            stack.bottomAnchor.constraint(equalTo: contentView.bottomAnchor, constant: -8),
            stack.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 16),
            stack.trailingAnchor.constraint(equalTo: contentView.trailingAnchor, constant: -16)
        ])

        pdfButton.addTarget(self, action: #selector(viewPdfTapped), for: .touchUpInside)
    }

    required init?(coder: NSCoder) { fatalError() }

    func configure(with msg: Message) {
        // אפשר להשתמש ב־metadata אם תרצה להציג פרטים נוספים
    }

    @objc private func viewPdfTapped() { onViewPdf?() }
}
