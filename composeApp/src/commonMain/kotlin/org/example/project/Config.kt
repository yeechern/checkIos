package org.example.project
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@JsonIgnoreUnknownKeys
@Serializable
data class Config(
    val log: Log? = null,
    val inbounds: List<Inbound>? = null,
    val outbounds: List<Outbound>? = null,
    val routing: Routing? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class Log(
    val access: String? = null,
    val error: String? = null,
    val loglevel: String? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class Inbound(
    val tag: String? = null,
    val port: Int? = null,
    val listen: String? = null,
    val protocol: String? = null,
    val sniffing: Sniffing? = null,
    val settings: InboundSettings? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class Sniffing(
    val enabled: Boolean? = null,
    val destOverride: List<String>? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class InboundSettings(
    val auth: String? = null,
    val udp: Boolean? = null,
    val allowTransparent: Boolean? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class Outbound(
    val tag: String? = null,
    val protocol: String? = null,
    val settings: OutboundSettings? = null,
    val streamSettings: StreamSettings? = null,
    val mux: Mux? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class OutboundSettings(
    val vnext: List<VNext>? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class VNext(
    val address: String? = null,
    val port: Int? = null,
    val users: List<User>? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class User(
    val id: String? = null,
    val encryption: String? = null,
    val flow: String? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class StreamSettings(
    val network: String? = null,
    val security: String? = null,
    val realitySettings: RealitySettings? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class RealitySettings(
    val serverName: String? = null,
    val publicKey: String? = null,
    val shortId: String? = null,
    val fingerprint: String? = null,
    val spiderX: String? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class Mux(
    val enabled: Boolean? = null,
    val concurrency: Int? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class Routing(
    val domainStrategy: String? = null,
    val domainMatcher: String? = null,
    val rules: List<Rule>? = null
)

@JsonIgnoreUnknownKeys
@Serializable
data class Rule(
    val type: String? = null,
    val domain: List<String>? = null,
    val ip: List<String>? = null,
    val port: String? = null,
    val outboundTag: String? = null
)

fun extractInbound(configPath: String): Inbound? {
    val fileContents = readFile(configPath)
    val config = Json.decodeFromString<Config>(fileContents)
    var http: Inbound? = null
    var socks: Inbound? = null

    config.inbounds?.forEach { inbound ->
        when (inbound.protocol) {
            "http" -> http = inbound
            "socks" -> socks = inbound
            else -> println("Unsupported protocol: ${inbound.protocol}") // Do not throw exception here
        }
    }

    return http ?: socks
}