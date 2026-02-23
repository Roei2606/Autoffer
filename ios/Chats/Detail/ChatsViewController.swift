import UIKit
import CoreModelsSDK
import ChatSDK
import UsersSDK
import Lottie

final class ChatsViewController: UIViewController {
    
    private let chatManager = ChatManager()
    private let userManager = UserManager.shared
    private var items: [ChatDisplayItem] = []
    
    private let tableView = UITableView()
    private let noChatsLabel: UILabel = {
        let label = UILabel()
        label.text = "No chats"
        label.textAlignment = .center
        label.textColor = .gray
        label.isHidden = true
        return label
    }()
    private let loadingAnimation: LottieAnimationView = {
        let anim = LottieAnimationView(name: "loading_animation")
        anim.contentMode = .scaleAspectFit
        anim.loopMode = .loop
        anim.isHidden = true
        return anim
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        title = "Chats"
        view.backgroundColor = UIColor(white: 0.96, alpha: 1.0)
        
        setupTable()
        setupLayout()
        loadChats()
    }
    
    private func setupTable() {
        tableView.register(ChatListCell.self, forCellReuseIdentifier: "ChatCell")
        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none
    }
    
    private func setupLayout() {
        view.addSubview(tableView)
        view.addSubview(noChatsLabel)
        view.addSubview(loadingAnimation)
        
        tableView.translatesAutoresizingMaskIntoConstraints = false
        noChatsLabel.translatesAutoresizingMaskIntoConstraints = false
        loadingAnimation.translatesAutoresizingMaskIntoConstraints = false
        
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            
            noChatsLabel.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            noChatsLabel.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            
            loadingAnimation.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            loadingAnimation.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            loadingAnimation.widthAnchor.constraint(equalToConstant: 150),
            loadingAnimation.heightAnchor.constraint(equalToConstant: 150)
        ])
    }
    
    private func loadChats() {
        Task {
            showLoading(true)
            let currentUserId = await (SessionManager.shared.currentUserId ?? "")
            let hasChats = await chatManager.hasChats(userId: currentUserId)
            
            if !hasChats {
                showLoading(false)
                noChatsLabel.isHidden = false
                tableView.isHidden = true
                return
            }
            
            do {
                let chats = try await chatManager.getUserChats(userId: currentUserId, page: 0, size: 50)
                var results: [ChatDisplayItem] = []
                for chat in chats {
                    guard let otherUserId = chat.participants?.first(where: { $0 != currentUserId }) else { continue }
                    if let user = try? await userManager.getById(otherUserId) {
                        let formatted = ChatViewModel.formatTimestamp(chat.lastMessageTimestamp)
                        results.append(ChatDisplayItem(
                            chat: chat,
                            otherUserName: user.firstName ?? "Unknown",
                            otherUserId: otherUserId,
                            formattedTime: formatted,
                            unreadCount: 0
                        ))
                    }
                }
                self.items = results
                tableView.reloadData()
            } catch {
                print("❌ Failed to load chats: \(error)")
            }
            showLoading(false)
        }
    }

    
    private func showLoading(_ show: Bool) {
        loadingAnimation.isHidden = !show
        if show { loadingAnimation.play() } else { loadingAnimation.stop() }
    }
}

extension ChatsViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        items.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "ChatCell", for: indexPath) as? ChatListCell else {
            return UITableViewCell()
        }
        let item = items[indexPath.row]
        cell.configure(with: item)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let item = items[indexPath.row]
        let vc = ChatViewController(chat: item.chat, otherUserId: item.otherUserId)
        navigationController?.pushViewController(vc, animated: true)
    }
}
