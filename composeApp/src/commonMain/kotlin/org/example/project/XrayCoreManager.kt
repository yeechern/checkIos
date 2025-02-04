package org.example.project

import kotlinx.coroutines.flow.Flow

expect class XrayCoreManager {
    suspend fun startXray(): Flow<XrayEvent>
    fun stopXray()
    suspend fun startProxySettings()
    suspend fun stopProxySettings()
}