import UIKit
import CoreModelsSDK
import ChatSDK

/// Adapter שמחבר בין [Message] ל־UITableView
final class MessageAdapter: NSObject, UITableViewDataSource, UITableViewDelegate {

    private var messages: [Message] = []
        private let currentUserId: String

        init(currentUserId: String) {
            self.currentUserId = currentUserId
        }

        // פונקציה לעדכון הודעות
        func update(messages: [Message]) {
            self.messages = messages
        }

        // פונקציה שמחזירה הודעה בודדת
        func message(at index: Int) -> Message {
            return messages[index]
        }

        // פונקציה שמחזירה את מספר ההודעות
        func count() -> Int {
            return messages.count
        }

        // MARK: - UITableViewDataSource
        func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
            return messages.count
        }


    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let msg = messages[indexPath.row]

        switch msg.type {
        case "TEXT":
            guard let cell = tableView.dequeueReusableCell(
                withIdentifier: ChatMessageCell.reuseId,
                for: indexPath
            ) as? ChatMessageCell else {
                return UITableViewCell()
            }
            cell.configure(with: msg, currentUserId: currentUserId)
            return cell

        case "BOQ_REQUEST":
            guard let cell = tableView.dequeueReusableCell(
                withIdentifier: MessageBOQCell.reuseId,
                for: indexPath
            ) as? MessageBOQCell else {
                return UITableViewCell()
            }
            cell.configure(with: msg)
            cell.onViewPdf = { print("📄 View BOQ PDF tapped") }
            cell.onAccept = { print("✅ Accept tapped") }
            cell.onReject = { print("❌ Reject tapped") }
            return cell

        case "QUOTE":
            guard let cell = tableView.dequeueReusableCell(
                withIdentifier: MessageQuoteCell.reuseId,
                for: indexPath
            ) as? MessageQuoteCell else {
                return UITableViewCell()
            }
            cell.configure(with: msg)
            cell.onViewPdf = { print("📄 View Quote PDF tapped") }
            return cell

        default:
            let cell = UITableViewCell()
            cell.textLabel?.text = msg.content ?? "Unsupported message"
            return cell
        }
    }

    // MARK: - UITableViewDelegate

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        UITableView.automaticDimension
    }
}
