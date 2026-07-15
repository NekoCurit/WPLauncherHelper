package net.nekocurit.i4399.x19.data

import io.ktor.http.Parameters
import net.nekocurit.i4399.game_sdk.entity.I4399GameSDKCaptcha
import net.nekocurit.i4399.game_sdk.entity.I4399GameSDKCaptcha.Companion.appendCaptcha
import net.nekocurit.i4399.game_sdk.entity.I4399GameSDKOauthSession
import net.nekocurit.i4399.game_sdk.entity.I4399GameSDKOauthSession.Companion.appendSession

data class Request4399X19Authorize(
    val session: I4399GameSDKOauthSession,
    val username: String = "",
    val password: String = "",
    val captcha: I4399GameSDKCaptcha? = null,
    val action: Action = Action.Login
) {

    enum class Action(val value: String) {
        @Suppress("SpellCheckingInspection")
        Login("ORILOGIN"),
        Register("register")
    }

    fun toParameters() = Parameters.build {
        append("password", password)
        append("username", username)
        appendCaptcha(captcha)
        append("response_type", "TOKEN")
        append("auth_action", action.value)
        appendSession(session)
    }

}