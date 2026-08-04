package net.nekocurit.i4399.game_sdk.entity

import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.Serializable
import net.nekocurit.i4399.game_sdk.I4399GameSDKAPI
import net.nekocurit.i4399.game_sdk.utils.I4399GameSDKForm
import net.nekocurit.i4399.game_sdk.utils.decodeForms

@Serializable
data class I4399GameSDKOauthSession(
    val forms: I4399GameSDKForm,
) {
    companion object {
        val I4399GameSDKAPI.sessionNotNull
            get() = requireNotNull(session) { "session 尚未初始化"}
        suspend fun fromOauthRespond(respond: HttpResponse) = I4399GameSDKOauthSession(respond.decodeForms())
        fun fromRawHtml(raw: String) = I4399GameSDKOauthSession(raw.decodeForms())
    }
}