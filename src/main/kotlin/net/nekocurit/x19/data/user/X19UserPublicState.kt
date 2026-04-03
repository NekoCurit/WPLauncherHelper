package net.nekocurit.x19.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.x19.api.likeUser
import net.nekocurit.x19.data.X19Entity

/**
 * @param totalPublicFollow 关注用户数
 * @param totalFriends 好友数
 * @param totalPublicUserFans 动态数
 * @param totalPurchaseComponent 累计购买组件数(模组 皮肤等)
 * @param totalPurchaseGame 累计购买游戏数(玩了多少个服务器)
 * @param videoViewCount 视频预览数 (我也不知道是什么)
 * @param totalLike 主页点赞数
 * @param totalView 主页访客数
 * @param hasLike 当前账号是否给该用户点赞过
 */
@Serializable
data class X19UserPublicState(
    @SerialName("entity_id")
    val id: ULong,
    @SerialName("my_public_follow_cnt")
    val totalPublicFollow: UInt,
    @SerialName("friend_cnt")
    val totalFriends: UInt,
    @SerialName("public_user_fans_cnt")
    val totalPublicUserFans: UInt,
    @SerialName("component_purchase_count")
    val totalPurchaseComponent: UInt,
    @SerialName("game_purchase_count")
    val totalPurchaseGame: UInt,
    @SerialName("video_view_count")
    val videoViewCount: UInt,
    @SerialName("personal_page_like_count")
    val totalLike: UInt,
    @SerialName("personal_page_view_count")
    val totalView: UInt,
    @SerialName("has_like")
    val hasLike: Boolean
): X19Entity() {
    /**
     * 为该用户点赞/取消点赞
     *
     * @param state true=点赞, false=取消
     */
    suspend fun like(state: Boolean = true) = api.likeUser(id, state)
}