package net.nekocurit.x19.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromJsonElement
import net.nekocurit.utils.json
import net.nekocurit.x19.data.ResponseX19Base

/**
 * @param id 这里不是启动器显示的服务器号, 而是内部 id
 */
@Suppress("SpellCheckingInspection")
@Serializable
data class X19RentalServerJoinInfo(
    @SerialName("entity_id")
    val id: ULong,
    @SerialName("isp_enable")
    val ispEnable: Boolean,
    @SerialName("mcserver_host")
    val host: String,
    @SerialName("mcserver_port")
    val port: Int,
) {

    val address
        get() = "$host:$port"

    companion object {
        fun ResponseX19Base.asX19RentalServerJoinInfo() = json.decodeFromJsonElement<X19RentalServerJoinInfo>(this.entity)
    }
}