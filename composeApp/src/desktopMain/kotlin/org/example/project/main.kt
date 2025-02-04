package org.example.project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMP Xray",
        state = androidx.compose.ui.window.rememberWindowState(
            width = 800.dp,
            height = 1000.dp
        )
    ) {
        val xrayCoreManager = remember { XrayCoreManager() }
        var isConnected by remember { mutableStateOf(false) }
        var xrayOutput by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()

        fun startXray(){
            coroutineScope.launch {
                xrayCoreManager.startXray().catch{
                    xrayOutput = "Error: "+it.message
                    isConnected = false
                }.collect { event ->
                    when(event){
                        is XrayEvent.Started -> {
                            isConnected = true
                            xrayOutput = "Xray started"
                            xrayCoreManager.startProxySettings()
                        }
                        is XrayEvent.Output -> {
                            xrayOutput += "\n" + event.message
                        }
                        is XrayEvent.Error -> {
                            xrayOutput = "Error: " + event.message
                            isConnected = false
                        }
                        is XrayEvent.Exit -> {
                            xrayOutput += "\n Xray exited code : " + event.code
                            isConnected = false
                        }
                        is XrayEvent.Stopped -> {
                            xrayCoreManager.stopProxySettings()
                        }
                    }
                }
            }
        }

        fun stopXray(){
            xrayCoreManager.stopXray()
            isConnected = false
            coroutineScope.launch {
                xrayCoreManager.stopProxySettings()
            }
            xrayOutput = "Xray stopped"

        }

        XrayApp(
            onStartStopClick = {
                if (it) {
                    startXray()
                }else{
                    stopXray()
                }

            },
            onConfigChangeClick = {},
            isConnected = isConnected,
            xrayOutput = xrayOutput
        )
    }
}