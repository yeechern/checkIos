package org.example.project

import kotlinx.coroutines.flow.Flow

actual class XrayCoreManager {
    actual suspend fun startXray(): Flow<XrayEvent> {
        TODO("Not yet implemented")
    }

    actual fun stopXray() {
    }

    actual suspend fun startProxySettings() {
    }

    actual suspend fun stopProxySettings() {
    }
}