// swift-tools-version: 6.1
import PackageDescription

let package = Package(
    name: "UsersSDK",
    platforms: [.iOS(.v16)],
    products: [
        .library(name: "UsersSDK", targets: ["UsersSDK"])
    ],
    dependencies: [
        // לוקאלי (התאם נתיבים לפי המבנה אצלך):
        .package(path: "../CoreModelsSDK"),
        .package(path: "../RSocketSDK")
        
    ],
    targets: [
        .target(
            name: "UsersSDK",
            dependencies: [
                .product(name: "CoreModelsSDK", package: "CoreModelsSDK"),
                .product(name: "RSocketSDK",   package: "RSocketSDK")
            ],
            path: "Sources"
        ),
        .testTarget(
            name: "UsersSDKTests",
            dependencies: ["UsersSDK"]
        )
    ]
)
