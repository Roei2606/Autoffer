import Foundation

/// שולח ping ל-Gateway ומחזיר את תשובת הטקסט
public enum PingSender {
    public static func sendPing() async -> Result<String, Error> {
        do {
            // GET http://<gateway>/api/ping
            let url = GatewayConfig.apiBase.appendingPathComponent("ping")
            var req = URLRequest(url: url)
            req.httpMethod = "GET"
            req.setValue("text/plain", forHTTPHeaderField: "Accept")

            let (data, resp) = try await URLSession.shared.data(for: req)
            guard let http = resp as? HTTPURLResponse, (200..<300).contains(http.statusCode) else {
                let status = (resp as? HTTPURLResponse)?.statusCode ?? -1
                return .failure(HTTPTransportError.requestFailed(status: status, body: data))
            }

            let reply = String(data: data, encoding: .utf8) ?? ""
            return .success(reply)
        } catch {
            return .failure(error)
        }
    }
}
