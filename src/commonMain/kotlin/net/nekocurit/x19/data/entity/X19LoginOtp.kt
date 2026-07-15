package net.nekocurit.x19.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromJsonElement
import net.nekocurit.utils.json
import net.nekocurit.x19.data.ResponseX19Base

@Serializable
data class X19LoginOtp(
    val otp: Int,
    @SerialName("otp_token")
    val otpToken: String,
    val aid: ULong
) {
    companion object {
        fun ResponseX19Base.asX19LoginOtp() = json.decodeFromJsonElement<X19LoginOtp>(this.entity)
    }
}