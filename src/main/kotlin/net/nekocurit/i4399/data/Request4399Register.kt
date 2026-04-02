package net.nekocurit.i4399.data

import io.ktor.http.*
import net.nekocurit.i4399.I4399EncryptUtils
import net.nekocurit.i4399.state.State4399Captcha

data class Request4399Register(
    val username: String,
    val password: String,
    val personal: Pair<String, String>,
    val captcha: State4399Captcha? = null
) {

    fun toParameters() = Parameters.build {
        append("postLoginHandler", "default")
        append("displayMode", "popup")
        append("bizId", "")
        append("appId", "www_home")
        append("gameId", "")
        append("cid", "")
        append("externalLogin", "qq")
        append("aid", "")
        append("ref", "")
        append("css", "//www.4399.com/css/4399_index_skin.css")
        append("redirectUrl", "")
        append("regMode", "reg_normal")
        append("regIdcard", "true")
        append("noEmail", "")
        append("crossDomainIFrame", "")
        append("crossDomainUrl", "")
        append("mainDivId", "popup_reg_div")
        append("showRegInfo", "true")
        append("includeFcmInfo", "false")
        append("expandFcmInput", "true")
        append("fcmFakeValidate", "false")
        append("realnameValidate", "true")
        append("userNameLabel", "4399用户名")
        append("level", "4")
        append("sec", "1")
        append("password", I4399EncryptUtils.encrypt(password))
        append("passwordveri", I4399EncryptUtils.encrypt(password))
        append("iframeId", "popup_reg_frame")
        append("realname", I4399EncryptUtils.encrypt(personal.first))
        append("idcard", I4399EncryptUtils.encrypt(personal.second))
        append("username", username)
        append("email", "")
        append("reg_eula_agree", "on")
        captcha?.also { captcha ->
            append("sessionId", captcha.session)
            append("inputCaptcha", captcha.code)
        }
    }
}