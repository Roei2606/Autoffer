// swift-tools-version: 6.1
import PackageDescription

let package = Package(
    name: "CoreModelsSDK",
    platforms: [.iOS(.v16)],
    products: [
        .library(name: "CoreModelsSDK", targets: ["CoreModelsSDK"])
    ],
    dependencies: [],
    targets: [
        .target(
            name: "CoreModelsSDK",
            path: "Sources/CoreModelsSDK"
        ),
        .testTarget(
            name: "CoreModelsSDKTests",
            dependencies: ["CoreModelsSDK"],
            path: "Tests/CoreModelsSDKTests"
        )
    ]
)
