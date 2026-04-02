package net.nekocurit.x19.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromJsonElement
import net.nekocurit.utils.json
import net.nekocurit.x19.data.ResponseX19Base

@Serializable
data class X19NetworkServerJoinInfo(
    @SerialName("entity_id")
    val id: ULong,
    @SerialName("isp_enable")
    val ispEnable: Boolean,
    @SerialName("ip")
    val host: String,
    val port: Int
) {

    val address
        get() = "$host:$port"

    companion object {
        fun ResponseX19Base.asX19NetworkServerJoinInfo() = json.decodeFromJsonElement<X19NetworkServerJoinInfo>(this.entity)
    }
}