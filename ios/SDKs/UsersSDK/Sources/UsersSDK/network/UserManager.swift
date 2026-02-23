// Sources/UsersSDK/network/UserManager.swift
import Foundation
import CoreModelsSDK

public actor UserManager {
    public nonisolated static let shared = UserManager()

    private let http: SessionManager
    public init(http: SessionManager = .shared) { self.http = http }

    @discardableResult
    public func login(_ req: LoginRequest) async throws -> User {
        try await http.post(UsersRoutes.login, body: req)
    }

    @discardableResult
    public func register(_ req: RegisterUserRequest) async throws -> User {
        try await http.post(UsersRoutes.register, body: req)
    }

    public func getAll() async throws -> [User] {
        try await http.get(UsersRoutes.all)
    }

    public func getById(_ id: String) async throws -> User {
        try await http.get(UsersRoutes.byId(id))
    }

    public func resetPassword(_ req: ResetPasswordRequest) async throws {
        try await http.postNoContent(UsersRoutes.resetPassword, body: req)
    }

    public func getByType(_ type: String) async throws -> [User] {
        try await http.get(UsersRoutes.byType(type))
    }
}
