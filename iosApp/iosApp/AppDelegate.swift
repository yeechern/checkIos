import UIKit
import NetworkExtension

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    let networkExtensionManager = NetworkExtensionManager() //Create an instance of network extension manager.

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        networkExtensionManager.loadAndEnableProvider() //Load and Enable when launched.
        return true
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
        networkExtensionManager.disableProvider() //Disable when terminate.
    }
}