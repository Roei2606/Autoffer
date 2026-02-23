import UIKit

final class MainTabBarController: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()
        configureAppearance()
        setupTabs()
    }

    // MARK: - Appearance
    private func configureAppearance() {
        view.backgroundColor = AppColors.bg

        // Tab bar (iOS 15+)
        let tabAppearance = UITabBarAppearance()
        tabAppearance.configureWithOpaqueBackground()
        tabAppearance.backgroundColor = AppColors.bg

        // Title colors
        let normalAttrs: [NSAttributedString.Key: Any] = [.foregroundColor: AppColors.hint]
        let selectedAttrs: [NSAttributedString.Key: Any] = [.foregroundColor: UIColor.white]
        tabAppearance.stackedLayoutAppearance.normal.titleTextAttributes = normalAttrs
        tabAppearance.stackedLayoutAppearance.selected.titleTextAttributes = selectedAttrs
        tabAppearance.inlineLayoutAppearance = tabAppearance.stackedLayoutAppearance
        tabAppearance.compactInlineLayoutAppearance = tabAppearance.stackedLayoutAppearance

        tabBar.standardAppearance = tabAppearance
        tabBar.scrollEdgeAppearance = tabAppearance
        tabBar.tintColor = .white
        tabBar.unselectedItemTintColor = AppColors.hint
        tabBar.isTranslucent = false

        // Navigation bar to match
        let navAppearance = UINavigationBarAppearance()
        navAppearance.configureWithOpaqueBackground()
        navAppearance.backgroundColor = AppColors.bg
        navAppearance.titleTextAttributes = [.foregroundColor: UIColor.white]
        navAppearance.largeTitleTextAttributes = [.foregroundColor: UIColor.white]
        navAppearance.shadowColor = .clear

        UINavigationBar.appearance().standardAppearance = navAppearance
        UINavigationBar.appearance().scrollEdgeAppearance = navAppearance
        UINavigationBar.appearance().compactAppearance = navAppearance
        UINavigationBar.appearance().tintColor = .white
        UINavigationBar.appearance().prefersLargeTitles = false
        UINavigationBar.appearance().barStyle = .black   // light status bar text
    }

    // MARK: - Tabs
    private func setupTabs() {
        // 🏠 Home
        let home = HomeViewController()
        home.title = "Home"
        home.tabBarItem = UITabBarItem(
            title: "Home",
            image: assetOrSymbol(named: "ic_home", fallback: "house"),
            tag: 0
        )

        // 💬 Chats
        let chats = ChatsViewController()
        chats.title = "Chats"
        chats.tabBarItem = UITabBarItem(
            title: "Chats",
            image: assetOrSymbol(named: "ic_chats", fallback: "bubble.left.and.bubble.right"),
            tag: 1
        )

        // ➕ New Project
        let newProject = NewProjectViewController()
        newProject.title = "New Project"
        newProject.tabBarItem = UITabBarItem(
            title: "New Project",
            image: assetOrSymbol(named: "ic_add_project", fallback: "plus.square.on.square"),
            tag: 2
        )

        // 📁 My Projects
        let myProjects = MyProjectsViewController()
        myProjects.title = "My Projects"
        myProjects.tabBarItem = UITabBarItem(
            title: "My Projects",
            image: assetOrSymbol(named: "ic_my_projects", fallback: "folder"),
            tag: 3
        )

        // 🏭 Factories (User Directory)
        let factories = UserDirectoryViewController()
        factories.title = "Factories"
        factories.tabBarItem = UITabBarItem(
            title: "Factories",
            image: assetOrSymbol(named: "ic_factories", fallback: "building.2"),
            tag: 4
        )

        viewControllers = [
            wrapInNav(home),
            wrapInNav(chats),
            wrapInNav(newProject),
            wrapInNav(myProjects),
            wrapInNav(factories)
        ]

        selectedIndex = 0
    }

    // MARK: - Helpers
    private func wrapInNav(_ root: UIViewController) -> UINavigationController {
        let nav = UINavigationController(rootViewController: root)
        root.navigationItem.backButtonTitle = "" // back arrow without text
        return nav
    }

    private func assetOrSymbol(named assetName: String, fallback systemName: String) -> UIImage? {
        if let img = UIImage(named: assetName) { return img }
        return UIImage(systemName: systemName)
    }
}
