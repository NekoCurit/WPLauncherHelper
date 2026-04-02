package net.nekocurit.x19.data.online_room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.x19.data.X19Entity

@Serializable
data class X19OnlineRoomJoinInfo(
    @SerialName("server_host")
    val host: String,
    @SerialName("server_port")
    val port: Int
): X19Entity() {
    val address
        get() = "$host:$port"
}