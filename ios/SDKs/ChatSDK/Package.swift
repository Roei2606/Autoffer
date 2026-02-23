// swift-tools-version: 6.1
import PackageDescription

let package = Package(
    name: "ChatSDK",
    platforms: [
        .iOS(.v16)
    ],
    products: [
        .library(name: "ChatSDK", targets: ["ChatSDK"])
    ],
    dependencies: [
        .package(name: "RSocketSDK",    path: "../RSocketSDK"),
        .package(name: "CoreModelsSDK", path: "../CoreModelsSDK")
    ],
    targets: [
        .target(
            name: "ChatSDK",
            dependencies: [
                "RSocketSDK",
                "CoreModelsSDK"
            ],
            path: "Sources/ChatSDK"
        ),
        .testTarget(
            name: "ChatSDKTests",
            dependencies: ["ChatSDK"],
            path: "Tests/ChatSDKTests"
        )
    ]
)
