import Foundation

public extension RSocketError {
    static func from(_ error: Error) -> RSocketError {
        if let http = error as? HTTPTransportError {
            switch http {
            case .requestFailed(let status, let body):
                return .requestFailed(status: status, body: body)
            case .mappingFailed(let msg):
                return .mappingFailed(msg)
            }
        }
        if error is DecodingError {
            return .decodeFailed
        }
        if let urlErr = error as? URLError {
            return .serverError(urlErr.localizedDescription)
        }
        return .serverError(String(describing: error))
    }
}
