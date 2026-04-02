package net.nekocurit.i4399.x19

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import net.nekocurit.x19.data.cookie.WPLauncherCookie4399PC
import kotlin.time.Clock

@Suppress("SpellCheckingInspection")
object I4399PCGameAPI {

    /**
     * 该接口已于 2026.1.1 弃用
     * 仅老账号可以登录
     */
    suspend fun login(username: String, password: String, onCaptcha: suspend (ByteArray) -> String): WPLauncherCookie4399PC {
        val client = HttpClient(Java) {
            install(HttpCookies)
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
        }

        val now = Clock.System.now().toEpochMilliseconds()

        // 验证码预处理
        val session = client.get("https://ptlogin.4399.com/ptlogin/verify.do?username=${username}&appId=kid_wdsj&t=${now}&inputWidth=iptw2&v=1").bodyAsText()
            .takeIf { it != "0" }
            ?.let { Regex("captchaId=(.+?)'").find(it) }
            ?.groupValues
            ?.get(1)
            ?.let { session -> session to onCaptcha(client.get("https://ptlogin.4399.com/ptlogin/captcha.do?captchaId=$session").bodyAsBytes()) }

        val randTime = client.submitForm(
            url = "https://ptlogin.4399.com/ptlogin/login.do?v=1",
            formParameters = Parameters.build {
                append("loginFrom", "uframe")
                append("postLoginHandler", "default")
                append("layoutSelfAdapting", "true")
                append("externalLogin", "qq")
                append("displayMode", "popup")
                append("layout", "vertical")
                append("bizId", "2100001792")
                append("appId", "kid_wdsj")
                append("gameId", "wd")
                append("css", "http://microgame.5054399.net/v2/resource/cssSdk/default/login.css")
                append("redirectUrl", "")
                append("sessionId", session?.first ?: "")
                append("mainDivId", "popup_login_div")
                append("includeFcmInfo", "false")
                append("level", "8")
                append("regLevel", "8")
                append("userNameLabel", "4399%E7%94%A8%E6%88%B7%E5%90%8D&")
                append("userNameTip", "%E8%AF%B7%E8%BE%93%E5%85%A54399%E7%94%A8%E6%88%B7%E5%90%8D&")
                append("welcomeTip", "%E6%AC%A2%E8%BF%8E%E5%9B%9E%E5%88%B04399")
                append("sec", "1")
                append("password", password)
                append("username", username)
                append("inputCaptcha", session?.second ?: "")
            }
        )
            .bodyAsText()
            .let { text ->
                if (text.contains("验证码错误")) error("验证码错误")
                if (text.contains("密码错误")) error("密码错误")
                if (text.contains("用户不存在")) error("用户不存在")

                Regex("""parent.timestamp = "(.+?)"""").find(text)?.groupValues?.get(1)
            }
            ?: error("获取登录状态失败")

        val finishUrl = client.get(buildUrl {
            protocol = URLProtocol.HTTPS
            host = "ptlogin.4399.com"
            path("ptlogin", "checkKidLoginUserCookie.do")

            parameters.apply {
                val gameUrl = buildUrl {
                    protocol = URLProtocol.HTTP
                    host = "cdn.h5wan.4399sj.com"
                    path("microterminal-h5-frame")

                    parameters.apply {
                        append("game_id", "500352")
                        append("rand_time", randTime)
                    }
                }.toString()

                append("appId", "kid_wdsj")
                append("gameUrl", gameUrl)
                append("rand_time", randTime)
                append("nick", "null")
                append("onLineStart", "false")
                append("show", "1")
                append("isCrossDomain", "1")
                append("retUrl", buildUrl {
                    protocol = URLProtocol.HTTP
                    host = "ptlogin.4399.com"
                    encodedPath = "/resource/ucenter.html"
                    parameters.apply {
                        append("action", "login")
                        append("appId", "kid_wdsj")
                        append("loginLevel", "8")
                        append("regLevel", "8")
                        append("bizId", "2100001792")
                        append("externalLogin", "qq")
                        append("qrLogin", "true")
                        append("layout", "vertical")
                        append("level", "101")
                        @Suppress("HttpUrlsUsage")
                        append("css", "http://microgame.5054399.net/v2/resource/cssSdk/default/login.css")
                        append("v", "2018_11_26_16")
                        append("postLoginHandler", "redirect")
                        append("checkLoginUserCookie", "true")
                        append("redirectUrl", gameUrl)
                    }
                }.toString())
            }
        })
            .headers["location"]
            ?.let { URLBuilder(it) }
            ?: error("获取重定向URL失败")


        if (finishUrl.host != "cdn.h5wan.4399sj.com") error("重定向URL与预期不符")
        val sig = finishUrl.parameters["sig"] ?: error("获取最终登录信息失败(sig)")
        val uid = finishUrl.parameters["uid"]?.toULongOrNull() ?: error("获取最终登录信息失败(uid)")
        val time = finishUrl.parameters["time"] ?: error("获取最终登录信息失败(time)")
        val validateState = finishUrl.parameters["validateState"] ?: error("获取最终登录信息失败(validateState)")


        val token = client.get(buildUrl {
            protocol = URLProtocol.HTTPS
            host = "microgame.5054399.net"
            path("v2", "service", "sdk", "info")

            parameters.apply {
                append("callback", "")
                append("queryStr",  ParametersBuilder().apply {
                    append("game_id", "500352")
                    append("nick", "null")
                    append("sig", sig)
                    append("uid", uid.toString())
                    append("fcm", "0")
                    append("show", "1")
                    append("isCrossDomain", "1")
                    append("rand_time", randTime)
                    append("ptusertype", "4399")
                    append("time", time)
                    append("validateState", validateState)
                    append("username", username.lowercase())
                }.build().formUrlEncode())
                append("_", time)
            }
        })
            .bodyAsText()
            .let {
                Regex("""&token=(.+?)"""").find(it)?.groupValues?.get(1)
                    ?: error(Regex(""""msg":"(.+?)"""").find(it)?.groupValues?.get(1) ?: "未知错误")
            }

        return WPLauncherCookie4399PC(uid, token, time, username.lowercase())
    }

}