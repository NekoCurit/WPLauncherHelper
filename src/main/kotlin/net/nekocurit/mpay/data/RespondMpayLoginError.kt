package net.nekocurit.mpay.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RespondMpayLoginError(
    val reason: String,
    val code: Int,
    @SerialName("verify_url")
    val verifyUrl: String = ""
) {
    override fun toString() = "$reason${if (code == 1351) " $verifyUrl" else ""}"
}