package net.nekocurit.x19.data.friend

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.x19.api.deleteFriend
import net.nekocurit.x19.api.setFriendMark
import net.nekocurit.x19.data.X19AuthEntity

/**
 * @param mark 备注
 * @param channel 登陆渠道 可以从此判断是官服还是渠道服账号
 */
@Serializable
data class X19Friend(
    @SerialName("uid")
    val id: ULong,
    @SerialName("nickname")
    val name: String,
    val mark: String,
    val signature: String,
    @SerialName("login_channel")
    val channel: String,
    @SerialName("headImage")
    val avatarUrl: String,
    @SerialName("frame_id")
    val frameUrl: String
): X19AuthEntity() {
    suspend fun deleteFriend() { api.deleteFriend(id) }
    suspend fun setFriendMark(mark: String) { api.setFriendMark(id, mark) }
}