package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

@Composable
fun MainViewController(onStartStopClick: (Boolean) -> Unit,
                       onConfigChangeClick: () -> Unit,
                       isConnected: Boolean,
                       xrayOutput: String): UIViewController {
    val uiViewController = remember(onStartStopClick, onConfigChangeClick, isConnected, xrayOutput) {
        ComposeUIViewController {
            XrayApp(
                onStartStopClick = { startStopClicked ->
                    println("Start Stop Clicked: $startStopClicked")
                    onStartStopClick(startStopClicked)
                },
                onConfigChangeClick = onConfigChangeClick,
                isConnected = isConnected,
                xrayOutput = xrayOutput
            )
        }
    }
    return uiViewController
}