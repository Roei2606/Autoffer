// swift-tools-version: 6.1
import PackageDescription

let package = Package(
    name: "LocalProjectSDK",
    platforms: [
        .iOS(.v16)
    ],
    products: [
        .library(
            name: "LocalProjectSDK",
            targets: ["LocalProjectSDK"]
        ),
    ],
    dependencies: [
        .package(name: "ProjectsSDK", path: "../ProjectsSDK") // 👈 add dependency
    ],
    targets: [
        .target(
            name: "LocalProjectSDK",
            dependencies: [
                "ProjectsSDK"   // 👈 link dependency here
            ],
            path: "Sources/LocalProjectSDK"
        ),
        .testTarget(
            name: "LocalProjectSDKTests",
            dependencies: ["LocalProjectSDK"],
            path: "Tests/LocalProjectSDKTests"
        )
    ]
)

