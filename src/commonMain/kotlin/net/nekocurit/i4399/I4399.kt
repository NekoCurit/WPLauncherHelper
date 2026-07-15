package net.nekocurit.i4399

import io.ktor.client.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import net.nekocurit.i4399.data.I4399Profile
import net.nekocurit.i4399.data.Request4399SetIdCardAndRealName
import net.nekocurit.i4399.state.State4399Captcha
import kotlin.time.Clock

class I4399 {
    val client = HttpClient {
        install(HttpCookies)
    }

    /**
     * 登陆账号
     *
     * @param username 用户名
     * @param password 密码
     * @param ocr 如果遇到图像识别挑战则调用此处获取结果
     * @param doRealName 如果账号实名过期则调用此处获取新的证件
     */
    suspend fun login(
        username: String,
        password: String,
        ocr: suspend (ByteArray) -> String,
        doRealName: suspend () -> Pair<String, String>
    ) {
        // 调用此请求以获取 phlogact 与 USESSIONID
        client.get("https://ptlogin.4399.com/ptlogin/loginFrame.do")

        val captcha = client.get("https://ptlogin.4399.com/ptlogin/verify.do?username=$username&appId=www_home&t=${Clock.System.now()}&inputWidth=iptw2&v=1")
            .bodyAsText()
            .let { Regex("""javascript:UniLoginChangPIC\('(.*?)'\)""").find(it)?.groupValues?.get(1) }
            ?.let { session ->
                val img = client.get("https://ptlogin.4399.com/ptlogin/captcha.do?captchaId=$session").bodyAsBytes()
                return@let State4399Captcha(session, ocr(img))
            }

        client.submitForm(
            url = "https://ptlogin.4399.com/ptlogin/login.do?v=1",
            formParameters = Parameters.build {
                append("appId", "www_home")
                append("sec", "1")
                append("autoLogin", "on")

                append("password", I4399EncryptUtils.encrypt(password))
                append("username", username)

                captcha?.also { captcha ->
                    append("sessionId", captcha.session)
                    append("inputCaptcha", captcha.code)
                }
            }
        )
            .also {
                val response = it.bodyAsText()

                @Suppress("SpellCheckingInspection")
                if (response.contains("needVerifyIdcard = false")) {
                    // 无实名自动完成实名
                    if (response.contains("needVerifyIdcard = true")) {
                        val realName = doRealName()

                        client.submitForm(
                            url = "https://ptlogin.4399.com/ptlogin/setIdcardAndRealname.do",
                            formParameters = Request4399SetIdCardAndRealName(realName.first, realName.second).toParameters()
                        )
                    }
                } else {
                    if (response == "请稍后再试~") error("请求频率过快")
                    Regex("""<div[^>]*id="Msg"[^>]*>(.*?)</div>""")
                        .find(response)
                        ?.also { group -> error(group.groupValues[1]) }
                    Regex(""" eventHandles.__errorCallback\('(.*?)'\);""")
                        .find(response)
                        ?.also { group -> error(group.groupValues[1]) }

                    println(response)
                }
            }
    }

    /**
     * 获取当前登录账号的 4399 资料
     */
    suspend fun getProfile() = I4399Profile(client.get("https://u.4399.com/profile/index.html").bodyAsText())

    /**
     * 更新 4399 资料
     *
     * @param nick 昵称
     * @param sex 性别 false=男 true=女
     * @param year 生日/年
     * @param month 生日/月
     * @param day 生日/日
     * @param province 所在省
     * @param city 所在城市
     */
    suspend fun updateProfile(nick: String, sex: Boolean = false, year: UInt = 2000U, month: UInt = 11U, day: UInt = 11U, province: String = "北京", city: String = "东城") {
        require(month in 1U..12U) { "Month should be between 1 and 12" }
        require(day in 1U..31U) { "Day should be between 1 and 31" }

        val hash = client.get("https://u.4399.com/profile/modify.html")
            .bodyAsText()
            .let { Regex("""<input\s+type="hidden"\s+name="__HASH__"\s+value="([^"]+)"""").find(it)?.groupValues[1] }
            ?: error("no hash")

        println(hash)
        client.cookies("https://u.4399.com").forEach {
            println(it.name + "  " + it.value)
        }

        client.submitForm(
            url = "https://u.4399.com/profile/modify-save.html",
            formParameters = Parameters.build {
                append("__HASH__", hash)
                append("birthday", "$year-$month-$day")
                append("nick", nick)
                append("sex", if (sex) "2" else "1")
                append("bir_year", year.toString())
                append("bir_month", month.toString())
                append("bir_day", day.toString())
                append("local_province", province)
                append("local_city", city)
                append("qq", "")
            }
        )
    }

    /**
     * 登出账号
     */
    suspend fun logout() {
        client.get("https://u.4399.com/signout.html")
    }
}