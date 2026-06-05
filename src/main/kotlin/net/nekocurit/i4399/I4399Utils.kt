package net.nekocurit.i4399

import io.ktor.client.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.HttpHeaders
import net.nekocurit.i4399.data.Request4399Register
import net.nekocurit.i4399.state.State4399Captcha

object I4399Utils {

    suspend fun register(
        username: String,
        password: String,
        personal: Pair<String, String>,
        ocr: suspend (ByteArray) -> String
    ) {
        val client = HttpClient {
            install(HttpCookies)
            defaultRequest {
                header(HttpHeaders.UserAgent, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) $username 0.0.0.0")
            }
        }

        val captcha = client.get("https://ptlogin.4399.com/ptlogin/regFrame.do?regMode=reg_normal&postLoginHandler=default&bizId=&redirectUrl=&displayMode=popup&css=%2F%2Fwww.4399.com%2Fcss%2F4399_index_skin.css&appId=www_home&gameId=&noEmail=false&regIdcard=false&autoLogin=true&cid=&aid=&ref=&level=4&mainDivId=popup_reg_div&includeFcmInfo=false&externalLogin=qq&fcmFakeValidate=true&expandFcmInput=true&userNameLabel=4399%E7%94%A8%E6%88%B7%E5%90%8D&userNameTip=%E8%AF%B7%E8%BE%93%E5%85%A54399%E7%94%A8%E6%88%B7%E5%90%8D&welcomeTip=%E6%AC%A2%E8%BF%8E%E5%9B%9E%E5%88%B04399&v=1770017834851&iframeId=popup_reg_frame")
            .bodyAsText()
            .let { Regex("""javascript:UniLoginChangPIC\('(.*?)'\)""").find(it)?.groupValues?.get(1) }
            ?.let { session ->
                val img = client.get("https://ptlogin.4399.com/ptlogin/captcha.do?captchaId=$session").bodyAsBytes()
                return@let State4399Captcha(session, ocr(img))
            }

        client.submitForm(
            url = "https://ptlogin.4399.com/ptlogin/register.do",
            formParameters = Request4399Register(username, password, personal, captcha).toParameters()
        )
            .bodyAsText()
            .also { text ->
                Regex("""<div[^>]*id="Msg"[^>]*class="login_hor login_err_tip"[^>]*>\s*(.*?)\s*</div>""")
                    .find(text)
                    ?.groupValues[1]
                    ?.also { error(it) }
            }
    }


}