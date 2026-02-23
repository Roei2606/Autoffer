import Foundation
import CoreModelsSDK
import ChatSDK
import UsersSDK

struct ChatDisplayItem {
    let chat: Chat
    let otherUserName: String
    let otherUserId: String
    let formattedTime: String
    let unreadCount: Int
}

@MainActor
final class ChatViewModel: ObservableObject {
    @Published var chats: [ChatDisplayItem] = []
    @Published var isLoading = false
    @Published var hasChats = true

    private let chatManager = ChatManager()
    private let userManager = UserManager.shared

    func loadChats() async {
        isLoading = true
        defer { isLoading = false }

        let currentUserId = await SessionManager.shared.currentUserId ?? ""
        do {
            let has = await chatManager.hasChats(userId: currentUserId)
            hasChats = has
            if !has { return }

            let fetchedChats = try await chatManager.getUserChats(
                userId: currentUserId,
                page: 0,
                size: 50
            )

            var result: [ChatDisplayItem] = []
            for chat in fetchedChats {
                guard let participants = chat.participants,
                      let otherUserId = participants.first(where: { $0 != currentUserId }) else { continue }

                if let user = try? await userManager.getById(otherUserId) {
                    let formatted = Self.formatTimestamp(chat.lastMessageTimestamp)
                    let unread = try await chatManager.getUnreadCount(chatId: chat.id ?? "", userId: currentUserId)

                    let safeName = user.firstName ?? "Unknown"
                    result.append(ChatDisplayItem(
                        chat: chat,
                        otherUserName: safeName,
                        otherUserId: otherUserId,
                        formattedTime: formatted,
                        unreadCount: unread
                    ))
                }
            }

            self.chats = result
        } catch {
            print("⚠️ Failed to load chats: \(error)")
            hasChats = false
        }
    }

    static func formatTimestamp(_ ts: String?) -> String {
        guard let ts = ts, !ts.isEmpty else { return "" }
        let input = DateFormatter()
        input.dateFormat = "yyyy-MM-dd'T'HH:mm:ss"
        if let date = input.date(from: ts) {
            let output = DateFormatter()
            output.dateFormat = "HH:mm"
            return output.string(from: date)
        }
        return ""
    }
}
