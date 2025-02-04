import SwiftUI

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    var kotlinSharedModule = KotlinSharedModule()
    @State private var isConnected: Bool = false
    @State private var xrayOutput: String = ""

    var body: some Scene {
        WindowGroup {
            ComposeView(
                onStartStopClick: { startStopClicked in
                    if (startStopClicked) {
                       startXray()
                    } else {
                        stopXray()
                    }
                },
                onConfigChangeClick: {},
                isConnected: isConnected,
                xrayOutput: xrayOutput
            )
            .ignoresSafeArea(.all)
        }
    }

    func startXray() {
       Task { //Use Task for async calls in SwiftUI
           kotlinSharedModule.xrayCoreManager.startXray().catch { error in
               xrayOutput = "Error: \(error.localizedDescription)"
               isConnected = false
           }.collect { event in
               switch event {
               case .started:
                   isConnected = true
                   xrayOutput = "Xray started"
                   //Start Proxy Setting will call the NETunnelProvider
               case .output(let message):
                   xrayOutput += "\n" + message
               case .error(let message):
                   xrayOutput = "Error: \(message)"
                   isConnected = false
               case .exit(let code):
                   xrayOutput += "\n Xray exited code : \(code)"
                   isConnected = false
               case .stopped:
                   isConnected = false
               default:
                   break
               }
           }
       }
   }

    func stopXray() {
        kotlinSharedModule.xrayCoreManager.stopXray()
        isConnected = false
        xrayOutput = "Xray stopped"
    }

}

struct ComposeView: UIViewControllerRepresentable {
    let onStartStopClick: (Bool) -> Void
    let onConfigChangeClick: () -> Void
    let isConnected: Bool
    let xrayOutput: String

    func makeUIViewController(context: Context) -> UIViewController {
        return KMPViewController(onStartStopClick: onStartStopClick, onConfigChangeClick: onConfigChangeClick, isConnected: isConnected, xrayOutput: xrayOutput)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        (uiViewController as! KMPViewController).update(onStartStopClick: onStartStopClick, onConfigChangeClick: onConfigChangeClick, isConnected: isConnected, xrayOutput: xrayOutput)
    }
}

private class KMPViewController : UIViewController {
    private var composeView: ComposeUIViewController? = nil

    private var onStartStopClick: (Bool) -> Void
    private var onConfigChangeClick: () -> Void
    private var isConnected: Bool
    private var xrayOutput: String

    init(onStartStopClick: @escaping (Bool) -> Void, onConfigChangeClick: @escaping () -> Void, isConnected: Bool, xrayOutput: String) {
        self.onStartStopClick = onStartStopClick
        self.onConfigChangeClick = onConfigChangeClick
        self.isConnected = isConnected
        self.xrayOutput = xrayOutput
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        let composeUIViewController = org.example.project.MainViewControllerKt.MainViewController(
            onStartStopClick: onStartStopClick,
            onConfigChangeClick: onConfigChangeClick,
            isConnected: isConnected,
            xrayOutput: xrayOutput
        )
        addChild(composeUIViewController)
        view.addSubview(composeUIViewController.view)
        composeUIViewController.didMove(toParent: self)
        composeView = composeUIViewController
    }

    func update(onStartStopClick: @escaping (Bool) -> Void, onConfigChangeClick: @escaping () -> Void, isConnected: Bool, xrayOutput: String) {
        self.onStartStopClick = onStartStopClick
        self.onConfigChangeClick = onConfigChangeClick
        self.isConnected = isConnected
        self.xrayOutput = xrayOutput

        //Update state to composeView
        MainViewControllerKt.MainViewController(
            onStartStopClick: onStartStopClick,
            onConfigChangeClick: onConfigChangeClick,
            isConnected: isConnected,
            xrayOutput: xrayOutput
        )

    }
}