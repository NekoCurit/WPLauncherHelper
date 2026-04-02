package net.nekocurit.i4399.x19

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import net.nekocurit.i4399.x19.data.Request4399X19Authorize
import net.nekocurit.i4399.x19.data.Request4399X19Oauth
import net.nekocurit.i4399.x19.data.Response4399X19Done
import net.nekocurit.i4399.x19.data.Response4399X19Oauth
import net.nekocurit.x19.data.cookie.WPLauncherCookie4399Com

object I4399GameAPI {

    suspend fun login(username: String, password: String, onCaptcha: suspend (ByteArray) -> String, sign: String = ""): WPLauncherCookie4399Com {
        val client = HttpClient(Java) {
            install(HttpCookies)
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
        }

        // 此接口返回的是 json  但是 Content-Type 不对
        val loginUrl = client.submitForm(
            url = "https://m.4399api.com/openapiv2/oauth.html",
            formParameters = Request4399X19Oauth(username, sign).toParameters()
        ).body<Response4399X19Oauth>().result.loginUrl

        val loginResponse = client.get(loginUrl).bodyAsText()

        // 验证码处理
        val captcha = client.submitForm(
            url = "https://ptlogin.4399.com/oauth2/authorize.do?channel=&sdk=op",
            formParameters = Request4399X19Authorize(loginResponse, "", "").toParameters()
        )
            .bodyAsText()
            .let { Regex("""javascript:UniLoginChangPIC\('(.*?)',\s*false\)""").find(it) }
            ?.groupValues[1]
            ?.let { id ->
                Pair(id, onCaptcha(client.get("https://ptlogin.4399.com/ptlogin/captcha.do?captchaId=$id").bodyAsBytes()))
            }

        // 最终登录
        return client.submitForm(
            url = "https://ptlogin.4399.com/oauth2/loginAndAuthorize.do?channel=&sdk=op&sdk_version=3.12.2.503",
            formParameters = Request4399X19Authorize(loginResponse, username, password, captcha).toParameters()
        )
            .let { response ->
                when (response.status) {
                    HttpStatusCode.OK -> response.bodyAsText()
                        .let { text ->
                            Regex("""<p\s+class="warning_tips\s+global_ico"\s+id="login_err_msg">\s*(.*?)\s*</p>""")
                                .find(text)
                                ?.groupValues[1]
                                ?.also { error(it) }

                            error("未知错误")
                        }
                    HttpStatusCode.Found -> client.get(response.headers[HttpHeaders.Location]!!)
                    else -> error("状态异常")
                }
            }
            .body<Response4399X19Done>()
            .checkState()
            .let { WPLauncherCookie4399Com(it.result.uid, it.result.state) }
    }

}