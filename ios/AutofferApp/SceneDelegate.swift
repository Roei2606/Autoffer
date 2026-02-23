import UIKit
import UsersSDK


final class SceneDelegate: UIResponder, UIWindowSceneDelegate {
    var window: UIWindow?

    func scene(_ scene: UIScene,
               willConnectTo session: UISceneSession,
               options connectionOptions: UIScene.ConnectionOptions) {

        guard let windowScene = scene as? UIWindowScene else { return }

        // חלון ו־placeholder זמני (אפשר splash)
        let window = UIWindow(windowScene: windowScene)
        let placeholder = UIViewController()
        placeholder.view.backgroundColor = AppColors.bg
        window.rootViewController = placeholder
        window.makeKeyAndVisible()
        self.window = window

        // ניווט – התאמה לעיצוב שלך
        let navAppearance = UINavigationBarAppearance()
        navAppearance.configureWithOpaqueBackground()
        navAppearance.backgroundColor = AppColors.bg
        navAppearance.titleTextAttributes = [.foregroundColor: UIColor.white]
        navAppearance.shadowColor = .clear

        // Bootstrap לסביבה → ואז קובעים Root Controller
        Task { @MainActor in
            // DEBUG עם כתובת לוקאלית? פשוט עדכן את ה־SessionManager לפני הקונפיגורציה:
            #if DEBUG
            #if targetEnvironment(simulator)
            let devBase = URL(string: "http://127.0.0.1:8090/api")!
            #else
            let devBase = URL(string: "http://192.168.1.137:8090/api")!
            #endif
            await SessionManager.shared.updateBaseURL(devBase)
            #endif
            
            await AppEnvironment.shared.configure()


            // קריאת currentUser מה-actor (דורש await)
            var isLoggedIn = false
            if let sess = AppEnvironment.shared.session {
                isLoggedIn = await (sess.currentUser != nil)
            }

            let rootVC: UIViewController = isLoggedIn
                ? MainTabBarController()
                : LoginViewController()

            let nav = UINavigationController(rootViewController: rootVC)
            nav.navigationBar.standardAppearance = navAppearance
            nav.navigationBar.scrollEdgeAppearance = navAppearance
            nav.navigationBar.compactAppearance = navAppearance
            nav.navigationBar.tintColor = .white
            nav.navigationBar.barStyle = .black
            nav.navigationBar.prefersLargeTitles = false
            rootVC.navigationItem.backButtonTitle = ""

            UIView.transition(with: window, duration: 0.25, options: .transitionCrossDissolve) {
                window.rootViewController = nav
            }
        }
    }

    func sceneDidDisconnect(_ scene: UIScene) {}
    func sceneDidBecomeActive(_ scene: UIScene) {}
    func sceneWillResignActive(_ scene: UIScene) {}
    func sceneWillEnterForeground(_ scene: UIScene) {}
    func sceneDidEnterBackground(_ scene: UIScene) {}
}
