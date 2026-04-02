package net.nekocurit.x19.data.online_room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.x19.api.onlineRoomJoin
import net.nekocurit.x19.data.X19Entity
import net.nekocurit.x19.extensions.buyItemIfNotHave

@Serializable
data class X19OnlineRoomDetail(
    @SerialName("entity_id")
    val id: ULong,
    @SerialName("res_id")
    val itemId: ULong,
    @SerialName("room_name")
    val name: String,
    @SerialName("max_count")
    val maxMembers: Int,
    @SerialName("member_uids")
    val members: List<String>,
    @SerialName("owner_id")
    val owner: ULong,
): X19Entity() {
    suspend fun join(password: String = "") = api
        .apply { buyItemIfNotHave(itemId) }
        .onlineRoomJoin(id ,password)
}