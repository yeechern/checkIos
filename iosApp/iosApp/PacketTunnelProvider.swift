//Swift code - PacketTunnelProvider.swift
import NetworkExtension
import Foundation

class PacketTunnelProvider: NEPacketTunnelProvider {

    override func startTunnel(options: [String : NSObject]?, completionHandler: @escaping (Error?) -> Void) {
        // 1. Access the shared Kotlin module
        let kotlinSharedModule = XrayApp() // Replace KotlinSharedModule with your actual class
        var proxyConfig: ProxyConfig? = nil
        var inbound: Inbound? = nil
        // Load config from xray api (if applicable)
        // proxyConfig = kotlinSharedModule.fetchProxyConfigFromXray()

        // Load config from bundled config.json
        let configPath = "composeResources/files/config.json"
        let fileManager = FileManager.default
        if fileManager.fileExists(atPath: configPath) {
            let fileContent: String = kotlinSharedModule.readFile(path: configPath)
            inbound = kotlinSharedModule.extractInbound(configPath:configPath) //Call extractInbound() from KMP
        }
        //if no inbound.
        guard let inboundListen = inbound?.listen,
              let inboundPort = inbound?.port
              else
        {
            let errorDescription = "Failed to load proxy configuration."
            NSLog("Error: \(errorDescription)")

            let userInfo = [NSLocalizedDescriptionKey: errorDescription]
            let error = NSError(domain: "YourAppDomain", code: 1, userInfo: userInfo)
            completionHandler(error)
            return
        }

        // 2. Configure the proxy settings
        let proxySettings = NEProxySettings()
        proxySettings.httpServer = NEProxyServer(address: inboundListen, port: Int(inboundPort))
        proxySettings.httpsServer = NEProxyServer(address: inboundListen, port: Int(inboundPort))
        proxySettings.excludeSimpleHostnames = true
        proxySettings.matchAllDomains = true // Or set specific domains

        let tunnelNetworkSettings = NEPacketTunnelNetworkSettings(tunnelRemoteAddress: "127.0.0.1")
        tunnelNetworkSettings.proxySettings = proxySettings

        // 3. Apply the settings
        setTunnelNetworkSettings(tunnelNetworkSettings) { error in
            if let error = error {
                NSLog("Failed to set tunnel network settings: \(error.localizedDescription)")
                completionHandler(error)
            } else {
                NSLog("Tunnel successfully configured")
                completionHandler(nil)
            }
        }
    }

    override func stopTunnel(with reason: NEProviderStopReason, completionHandler: @escaping () -> Void) {
        // Clean up resources and disconnect
        NSLog("Stopping tunnel with reason: \(reason)")
        completionHandler()
    }
}