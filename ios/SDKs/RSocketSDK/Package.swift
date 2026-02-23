// swift-tools-version: 6.0
import PackageDescription

let package = Package(
    name: "RSocketSDK",                 // אפשר להשאיר כך לעת עתה
    platforms: [.iOS(.v16)],
    products: [
        .library(name: "RSocketSDK", targets: ["RSocketSDK"])
    ],
    dependencies: [
        // ⛔️ אין צורך יותר ב-RSocket/Swift-NIO — כל הקריאות יהיו HTTP עם URLSession
    ],
    targets: [
        .target(
            name: "RSocketSDK",
            dependencies: [],           // ← ריק
            path: "Sources"             // השאר כפי שהוא אם המקור תחת Sources/
        ),
        .testTarget(
            name: "RSocketSDKTests",
            dependencies: ["RSocketSDK"]
        )
    ]
)
