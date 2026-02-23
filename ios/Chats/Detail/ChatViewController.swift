import UIKit
import CoreModelsSDK
import ChatSDK
import UsersSDK

final class ChatViewController: UIViewController {
    
    private let chat: Chat
    private let otherUserId: String
    private let chatManager = ChatManager()
    private let messageManager = MessageManager()
    
    private var messages: [Message] = []
    private var adapter: MessageAdapter!
    
    private let tableView = UITableView()
    private let inputField = UITextField()
    private let sendButton = UIButton(type: .system)
    
    init(chat: Chat, otherUserId: String) {
        self.chat = chat
        self.otherUserId = otherUserId
        super.init(nibName: nil, bundle: nil)
    }
    required init?(coder: NSCoder) { fatalError() }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        title = "Chat"
        view.backgroundColor = .white

        Task { @MainActor in
            let currentUserId = await SessionManager.shared.currentUserId ?? ""
            self.adapter = MessageAdapter(currentUserId: currentUserId)

            setupLayout()
            setupTable()
            loadMessages()
            startListening()
        }
    }

    
    private func setupLayout() {
        tableView.translatesAutoresizingMaskIntoConstraints = false
        inputField.translatesAutoresizingMaskIntoConstraints = false
        sendButton.translatesAutoresizingMaskIntoConstraints = false
        
        view.addSubview(tableView)
        view.addSubview(inputField)
        view.addSubview(sendButton)
        
        inputField.placeholder = "Type a message..."
        inputField.borderStyle = .roundedRect
        sendButton.setTitle("Send", for: .normal)
        
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            
            inputField.topAnchor.constraint(equalTo: tableView.bottomAnchor, constant: 8),
            inputField.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 8),
            inputField.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -8),
            
            sendButton.centerYAnchor.constraint(equalTo: inputField.centerYAnchor),
            sendButton.leadingAnchor.constraint(equalTo: inputField.trailingAnchor, constant: 8),
            sendButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -8),
            sendButton.widthAnchor.constraint(equalToConstant: 60)
        ])
        
        tableView.bottomAnchor.constraint(equalTo: inputField.topAnchor, constant: -8).isActive = true
        
        sendButton.addTarget(self, action: #selector(sendTapped), for: .touchUpInside)
    }
    
    private func setupTable() {
        tableView.dataSource = adapter
        tableView.delegate = adapter
        tableView.register(ChatMessageCell.self, forCellReuseIdentifier: ChatMessageCell.reuseId)
        tableView.register(MessageBOQCell.self, forCellReuseIdentifier: MessageBOQCell.reuseId)
        tableView.register(MessageQuoteCell.self, forCellReuseIdentifier: MessageQuoteCell.reuseId)
    }
    
    private func loadMessages() {
        Task {
            do {
                let msgs = try await chatManager.getChatMessages(chatId: chat.id ?? "", page: 0, size: 100)
                adapter.update(messages: msgs)
                tableView.reloadData()
                scrollToBottom()
            } catch {
                print("❌ Failed to load messages: \(error)")
            }
        }
    }
    
    private func startListening() {
        Task {
            do {
                let stream = try await chatManager.streamMessages(chatId: chat.id ?? "")
                for try await newMsg in stream {
                    messages.append(newMsg)
                    adapter.update(messages: messages)
                    tableView.reloadData()
                    scrollToBottom()
                }
            } catch {
                print("❌ Stream error: \(error)")
            }
        }
    }
    
    @objc private func sendTapped() {
        guard let text = inputField.text, !text.isEmpty else { return }

        Task {
            do {
                let currentUserId = await SessionManager.shared.currentUserId ?? ""
                let newMsg = Message(
                    chatId: chat.id,
                    senderId: currentUserId,
                    receiverId: otherUserId,
                    content: text,
                    timestamp: ISO8601DateFormatter().string(from: Date()),
                    type: "TEXT"
                )

                let sent = try await messageManager.sendMessage(newMsg)
                messages.append(sent)
                adapter.update(messages: messages)
                tableView.reloadData()
                scrollToBottom()
                inputField.text = ""
            } catch {
                print("❌ Failed to send: \(error)")
            }
        }
    }

    
    private func scrollToBottom() {
        guard !messages.isEmpty else { return }
        let last = IndexPath(row: messages.count - 1, section: 0)
        tableView.scrollToRow(at: last, at: .bottom, animated: true)
    }
}
