import NetworkExtension

class NetworkExtensionManager {
    let providerBundleIdentifier = "org.example.project.PacketTunnel" //Important: Your Packet Tunnel Extension Bundle ID

    func loadAndEnableProvider() {
        NEPacketTunnelProviderManager.loadAllFromPreferences { (managers, error) in
            if let error = error {
                NSLog("Error loading Network Extension: \(error.localizedDescription)")
                return
            }

            let manager: NEPacketTunnelProviderManager

            if let existingManager = managers?.first {
                manager = existingManager
            } else {
                manager = NEPacketTunnelProviderManager()
                manager.localizedDescription = "KMP Xray Proxy"
                manager.providerConfiguration = [:] // Add configuration if needed
                manager.protocolConfiguration.providerBundleIdentifier = self.providerBundleIdentifier
            }

            manager.isEnabled = true
            manager.saveToPreferences { (error) in
                if let error = error {
                    NSLog("Error saving Network Extension preferences: \(error.localizedDescription)")
                    return
                }

                self.startNetworkExtension()
            }
        }
    }

    func disableProvider() {
        NEPacketTunnelProviderManager.loadAllFromPreferences { (managers, error) in
            if let error = error {
                NSLog("Error loading Network Extension: \(error.localizedDescription)")
                return
            }

            guard let manager = managers?.first else {
                return
            }

            manager.isEnabled = false
            manager.saveToPreferences { (error) in
                if let error = error {
                    NSLog("Error disabling Network Extension: \(error.localizedDescription)")
                    return
                }
            }
        }
    }

    func startNetworkExtension() {
        NEPacketTunnelProviderManager.loadAllFromPreferences { (managers, error) in
            if let error = error {
                NSLog("Error loading Network Extension: \(error.localizedDescription)")
                return
            }

            guard let manager = managers?.first else {
                NSLog("No manager found")
                return
            }

            do {
                try manager.connection.startVPNTunnel()
                NSLog("Starting tunnel")

            } catch {
                NSLog("Failed to start tunnel \(error)")
            }

        }
    }
}