package net.nekocurit.i4399.data

import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.i4399.I4399EncryptUtils

@Serializable
data class Request4399SetIdCardAndRealName(
    val needValidate: Boolean = true,
    @SerialName("realname")
    val encryptedRealName: String,
    @SerialName("idcard")
    val encryptedIdCard: String,
    val appid: String = "www_home",
    val sec: Int = 1,
    val bizId: String = "",
    val gameId: String = "",
    val isReg: Boolean = false
) {
    constructor(name: String, card: String) : this(
        encryptedRealName = I4399EncryptUtils.encrypt(name),
        encryptedIdCard = I4399EncryptUtils.encrypt(card),
    )

    fun toParameters() = Parameters.build {
        append("needValidate", needValidate.toString())

        append("realname", encryptedRealName)
        append("idcard", encryptedIdCard)

        append("appid", appid)
        append("sec", sec.toString())
        append("bizId", bizId)
        append("gameId", gameId)

        append("isReg", isReg.toString())
    }
}