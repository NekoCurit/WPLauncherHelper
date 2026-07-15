package net.nekocurit.mpay.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.x19.data.cookie.WPLauncherCookieMpay

@Serializable
data class MpayUser(
    @SerialName("is_channel_adult")
    val isChannelAudit: Boolean,
    @SerialName("need_mask")
    val needMask: Boolean,
    @SerialName("ext_access_token")
    val extAccessToken: String,
    @SerialName("display_username")
    val displayName: String,
    @SerialName("client_username")
    val username: String,
    val nickname: String,
    val token: String,
    val id: String,
    @SerialName("pc_ext_info")
    val extendInfo: MpayExtendInfo
) {
    fun toCookie(device: MpayDevice) = WPLauncherCookieMpay(this, device)
}