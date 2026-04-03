package net.nekocurit.x19.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.x19.api.sendFriendRequest
import net.nekocurit.x19.data.X19Entity

@Serializable
data class X19User(
    @SerialName("entity_id")
    val id: ULong,
    val name: String,
    @SerialName("avatar_image_url")
    val avatarUrl: String,
    @SerialName("frame_id")
    val frameUrl: String
): X19Entity() {
    suspend fun sendFriendRequest(message: String = "消息") { api.sendFriendRequest(id, message) }
}