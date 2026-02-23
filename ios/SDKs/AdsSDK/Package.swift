// swift-tools-version: 6.1
import PackageDescription

let package = Package(
    name: "AdsSDK",
    platforms: [.iOS(.v16)],
    products: [
        .library(name: "AdsSDK", targets: ["AdsSDK"])
    ],
    dependencies: [
        .package(name: "CoreModelsSDK", path: "../CoreModelsSDK"),
        .package(name: "RSocketSDK",    path: "../RSocketSDK")
    ],
    targets: [
        .target(
            name: "AdsSDK",
            dependencies: [
                "CoreModelsSDK",
                "RSocketSDK"
            ],
            path: "Sources/AdsSDK"
        ),
        .testTarget(
            name: "AdsSDKTests",
            dependencies: ["AdsSDK"],
            path: "Tests/AdsSDKTests"
        )
    ]
)
