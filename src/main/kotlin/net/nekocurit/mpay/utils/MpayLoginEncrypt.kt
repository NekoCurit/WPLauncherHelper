package net.nekocurit.mpay.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.mpay.entity.MpayDevice
import net.nekocurit.utils.encodeMd5
import net.nekocurit.utils.json
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object MpayLoginEncrypt {

    fun encryptLoginParams(email: String, password: String, device: MpayDevice) = Parameters(email, password.encodeMd5(), device.uniqueId)
        .let { json.encodeToString(it) }
        .let { input ->
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            val secretKey = SecretKeySpec(device.key, "AES")

            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            cipher.doFinal(input.toByteArray())
        }
        .toHexString()

    @Serializable
    data class Parameters(
        val username: String,
        val password: String,
        @SerialName("unique_id")
        val id: String = ""
    )


}