package org.example.project

sealed class XrayEvent {
    object Started: XrayEvent()
    data class Output(val message: String): XrayEvent()
    data class Error(val message: String): XrayEvent()
    data class Exit(val code: String): XrayEvent()
    object Stopped: XrayEvent()
}