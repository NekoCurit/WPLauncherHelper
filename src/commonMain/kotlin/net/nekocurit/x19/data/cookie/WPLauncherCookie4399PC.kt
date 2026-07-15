package net.nekocurit.x19.data.cookie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import net.nekocurit.utils.nextString
import net.nekocurit.utils.json
import kotlin.random.Random

@Suppress("SpellCheckingInspection")
@Serializable
data class WPLauncherCookie4399PC(
    @SerialName("aim_info")
    val aimInfo: String = """{"aim":"127.0.0.1","country":"CN","tz":"+0800","tzid":""}""",
    @SerialName("realname")
    val realName: String = """{"realname_type":2}""",
    @SerialName("gameid")
    val gamId: String = "x19",
    @SerialName("app_channel")
    val appChannel: String = "4399pc",
    @SerialName("login_channel")
    val loginChannel: String = "4399pc",
    @SerialName("platform")
    val platform: String = "pc",
    @SerialName("sdk_version")
    val sdkVersion: String = "1.0.0",
    @SerialName("sdkuid")
    val uid: String,
    @SerialName("sessionid")
    val session: String,
    val timestamp: String = "",
    @SerialName("userid")
    val userId: String = "",
    @SerialName("udid")
    val userDeviceId: String = Random.nextString(16),
    /**
     * 下为兼容性字段
     */
    @SerialName("client_login_sn")
    val clientLoginSn: String = Random.nextString(16),
    @SerialName("deviceid")
    val deviceId: String = Random.nextString(16),
): AbstractWPLauncherCookie() {
    constructor(uid: ULong, state: String, time: String, user: String): this(
        uid = uid.toString(),
        session = state,
        timestamp = time,
        userId = user,
    )

    override fun getUdId() = userDeviceId
    override fun toCookie(): JsonObject = json.encodeToJsonElement(this).jsonObject
}