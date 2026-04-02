package net.nekocurit.i4399.data

import io.ktor.http.*
import net.nekocurit.i4399.I4399EncryptUtils
import net.nekocurit.i4399.state.State4399Captcha

data class Request4399Login(
    val username: String,
    val password: String,
    val captcha: State4399Captcha? = null
) {

    fun toParameters() = Parameters.build {
        append("loginFrom", "uframe")
        append("postLoginHandler", "default")
        append("layoutSelfAdapting", "true")
        append("externalLogin", "qq")
        append("displayMode", "popup")
        append("layout", "vertical")

        append("bizId", "")
        append("appId", "www_home")
        append("gameId", "")
        append("css", "//www.4399.com/css/4399_index_skin.css")
        append("redirectUrl", "")
        append("mainDivId", "popup_login_div")

        append("includeFcmInfo", "false")
        append("level", "0")
        append("regLevel", "4")

        append("userNameLabel", "4399用户名")
        append("userNameTip", "请输入4399用户名")
        append("welcomeTip", "欢迎回到4399")

        append("sec", "1")
        append("password", I4399EncryptUtils.encrypt(password))
        append("iframeId", "popup_login_frame")
        append("username", username)

        captcha?.also { captcha ->
            append("sessionId", captcha.session)
            append("inputCaptcha", captcha.code)
        }
        append("autoLogin", "on")
    }
}