package net.nekocurit.mpay.utils

import io.ktor.utils.io.core.*
import korlibs.crypto.AES
import korlibs.crypto.CipherMode.Companion.ECB
import korlibs.crypto.CipherPadding.Companion.PKCS7Padding
import korlibs.crypto.with
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.mpay.entity.MpayDevice
import net.nekocurit.utils.json
import net.nekocurit.utils.md5

object MpayLoginEncrypt {

    fun encryptLoginParams(email: String, password: String, device: MpayDevice) = Parameters(email, password.md5(), device.uniqueId)
        .let { json.encodeToString(it) }
        .let { input ->
            AES(device.key)
                .with(ECB, PKCS7Padding)
                .encrypt(input.toByteArray())
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