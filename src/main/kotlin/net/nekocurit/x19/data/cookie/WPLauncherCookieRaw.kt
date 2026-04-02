package net.nekocurit.x19.data.cookie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import net.nekocurit.utils.json

data class WPLauncherCookieRaw(
    var raw: String
): AbstractWPLauncherCookie() {

    @Suppress("SpellCheckingInspection")
    @Serializable
    data class WrappedCookie(
        @SerialName("sauth_json")
        val real: String
    )

    init {
        @Suppress("SpellCheckingInspection")
        if (raw.contains("sauth_json")) {
            raw = json.decodeFromString<WrappedCookie>(raw).real
        }
    }

    override fun getUdId() = "0721"
    override fun toCookie(): JsonObject = json.parseToJsonElement(raw).jsonObject
}