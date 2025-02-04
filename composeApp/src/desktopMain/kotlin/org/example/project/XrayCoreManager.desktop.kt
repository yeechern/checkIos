package org.example.project


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

actual class XrayCoreManager {

    private var xrayProcess: Process? = null
    private val configPath = "src/commonMain/composeResources/files/config.json"
    private val xrayPath = "src/commonMain/composeResources/files/xray.exe"


    actual suspend fun startXray(): Flow<XrayEvent> = flow {
        val xrayFile = File(xrayPath)
        if (!xrayFile.exists()) {
            emit(XrayEvent.Error("Xray executable not found at $xrayPath"))
            return@flow
        }
        val configFile = File(configPath)
        if (!configFile.exists()) {
            emit(XrayEvent.Error("Config file not found at $configPath"))
            return@flow
        }

        try {
            val config = extractInbound(configPath)
            val address = config.listen
            val port = config.port

            val processBuilder = ProcessBuilder(xrayPath, "run", "--config", configPath)
            xrayProcess = processBuilder.start()
            emit(XrayEvent.Started)


            val reader = BufferedReader(InputStreamReader(xrayProcess?.inputStream))
            var line: String?
            while (true) {
                line = reader.readLine()
                if (line != null) {
                    emit(XrayEvent.Output(line))
                }
                if (xrayProcess?.isAlive == false){
                    break
                }
            }
            val exitCode = xrayProcess?.waitFor()
            emit(XrayEvent.Exit(exitCode?.toString() ?: ""))
            xrayProcess = null
            emit(XrayEvent.Stopped)

        } catch (e: Exception) {
            emit(XrayEvent.Error(e.message ?: "Unknown error"))
            xrayProcess = null
        }
    }.flowOn(Dispatchers.IO)


    actual fun stopXray() {
        xrayProcess?.destroy()
        xrayProcess = null
    }

    actual suspend fun startProxySettings() {
        setProxySettings(true)
    }

    actual suspend fun stopProxySettings() {
        setProxySettings(false)
    }

    //Write the powershell to set Proxy setting
    private suspend fun setProxySettings(enabled: Boolean) {
        withContext(Dispatchers.IO){
            val inbound = extractInbound(configPath)
            val address = inbound.listen
            val port = inbound.port
            val ps = ProcessBuilder("powershell.exe", "-NoLogo", "-NoProfile", "-Command", "-").start()
            val commands = if (enabled){
                listOf(
                    """Set-ItemProperty -Path 'HKCU:\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings' -Name ProxyServer -Value "$address:$port" """,
                    """Set-ItemProperty -Path 'HKCU:\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings' -Name ProxyEnable -Value 1"""
                )
            } else {
                listOf(
                    """Set-ItemProperty -Path 'HKCU:\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings' -Name ProxyServer -Value "$address:$port" """,
                    """Set-ItemProperty -Path 'HKCU:\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings' -Name ProxyEnable -Value 0"""
                )
            }
            val writer = ps.outputStream.bufferedWriter()
            commands.forEach { command ->
                writer.write(command)
                writer.newLine()
            }
            writer.close()
            ps.waitFor()
        }

    }
}