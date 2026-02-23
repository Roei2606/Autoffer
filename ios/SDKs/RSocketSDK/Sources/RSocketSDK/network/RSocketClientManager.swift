import Foundation

public actor RSocketClientManager {
    public static let shared = RSocketClientManager()

    private var client: RSocketClient?
    private var connecting = false

    private init() {}

    public var isConnected: Bool {
        if let c = client, !c.isDisposed { return true }
        return false
    }

    // 🔑 חיבור לשרת (משתמש ב־DefaultRSocketClient עם כתובת מקובעת בפנים)
    @discardableResult
    public func connect() async -> Bool {
        if isConnected { return true }
        if connecting { return false }
        connecting = true
        defer { connecting = false }

        let c = DefaultRSocketClient()
        self.client = c
        print("✅ HTTP client ready")
        return true
    }

    // מחזיר Client מוכן
    public func getOrConnect() async throws -> RSocketClient {
        if isConnected { return client! }
        let ok = await connect()
        if !ok { throw RSocketError.notConnected }
        return client!
    }

    public func disconnect() {
        self.client = nil
    }
}


