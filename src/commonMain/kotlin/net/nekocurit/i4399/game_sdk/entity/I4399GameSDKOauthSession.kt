package net.nekocurit.i4399.game_sdk.entity

import io.ktor.http.ParametersBuilder
import kotlinx.serialization.Serializable

@Serializable
data class I4399GameSDKOauthSession(
    val clientId: String,
    val ref: String,
    val bizId: String,
    val state: String,
    val redirectUrl: String
) {
    companion object {
        val CLIENT_ID = Regex("""<input type="hidden" name="client_id" value="(.*?)"/>""")
        val REF = Regex("""<input type="hidden" name="ref" value="(.*?)"/>""")
        val BIZ_ID = Regex("""<input type="hidden" name="bizId" value="(.*?)"/>""")
        val STATE = Regex("""<input type="hidden" name="state" value="(.*?)"/>""")
        val REDIRECT_URL = Regex("""<input type="hidden" name="redirect_uri" value="(.*?)"/>""")

        fun ParametersBuilder.appendSession(session: I4399GameSDKOauthSession) = apply {
            append("client_id", session.clientId)
            append("ref", session.ref)
            append("bizId", session.bizId)
            append("state", session.state)
            append("redirect_uri", session.redirectUrl)
        }
    }
    constructor(raw: String): this(
        clientId = CLIENT_ID.find(raw)?.groupValues[1]!!,
        ref = REF.find(raw)?.groupValues[1]!!,
        bizId = BIZ_ID.find(raw)?.groupValues[1]!!,
        state = STATE.find(raw)?.groupValues[1]!!,
        redirectUrl = REDIRECT_URL.find(raw)?.groupValues[1]!!,
    )
}