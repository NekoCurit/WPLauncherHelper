package net.nekocurit.x19.data.online_room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.x19.api.onlineRoomCurrentJoinInfo
import net.nekocurit.x19.api.onlineRoomGetMembers
import net.nekocurit.x19.api.onlineRoomLeave
import net.nekocurit.x19.data.X19Entity

@Serializable
data class X19OnlineRoom(
    @SerialName("room_id")
    val id: ULong
): X19Entity() {
    suspend fun getMembers() = api.onlineRoomGetMembers(id)
    suspend fun getJoinInfo() = api.onlineRoomCurrentJoinInfo()
    suspend fun leave() = api.onlineRoomLeave(id)
}