package net.nekocurit.i4399.x19.data

import io.ktor.http.Parameters

data class Request4399X19Authorize(
    val oauthBody: String,
    val username: String,
    val password: String,
    val captcha: Pair<String, String>? = null
) {

    @Suppress("SpellCheckingInspection")
    fun toParameters() = Parameters.build {
        append("password", password)
        append("username", username)
        captcha?.also { captcha ->
            append("captcha", captcha.second)
            append("captcha_id", captcha.first)
        }
        append("response_type", "TOKEN")
        append("auth_action", "ORILOGIN")
        append("client_id", Regex("""<input type="hidden" name="client_id" value="(.*?)"/>""").find(oauthBody)?.groupValues[1]!!)
        append("ref", Regex("""<input type="hidden" name="ref" value="(.*?)"/>""").find(oauthBody)?.groupValues[1]!!)
        append("bizId", Regex("""<input type="hidden" name="bizId" value="(.*?)"/>""").find(oauthBody)?.groupValues[1]!!)
        append("state", Regex("""<input type="hidden" name="state" value="(.*?)"/>""").find(oauthBody)?.groupValues[1]!!)
        append("redirect_uri", Regex("""<input type="hidden" name="redirect_uri" value="(.*?)"/>""").find(oauthBody)?.groupValues[1]!!)
    }

}