// swift-tools-version: 6.1
import PackageDescription

let package = Package(
    name: "ProjectsSDK",
    platforms: [
        .iOS(.v16)
    ],
    products: [
        .library(name: "ProjectsSDK", targets: ["ProjectsSDK"])
    ],
    dependencies: [
        .package(name: "CoreModelsSDK", path: "../CoreModelsSDK"),
        .package(name: "RSocketSDK",    path: "../RSocketSDK")
    ],
    targets: [
        .target(
            name: "ProjectsSDK",
            dependencies: [
                "CoreModelsSDK",
                "RSocketSDK"
            ],
            path: "Sources/ProjectsSDK"
        ),
        .testTarget(
            name: "ProjectsSDKTests",
            dependencies: ["ProjectsSDK"],
            path: "Tests/ProjectsSDKTests"
        )
    ]
)
