package net.nekocurit.i4399.x19.data

import io.ktor.http.Parameters

data class Request4399X19Oauth(
    val username: String,
    val device: String = ""
) {

    fun toParameters() = Parameters.build {
        append("usernames", username)
        append("top_bar", "1")
        append("state", "")
        append("device", """{"GAME_KEY":"115716","GAME_VERSION":"3.6.29.285648","BID":"com.netease.mc.m4399","DEVICE_IDENTIFIER":"$device"}""")
    }
}