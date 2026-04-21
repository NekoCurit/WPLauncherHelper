package net.nekocurit.x19.data.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.utils.serializer.InstantLongSSerializer
import net.nekocurit.x19.api.getComment
import net.nekocurit.x19.api.getItemComments
import net.nekocurit.x19.api.getUser
import net.nekocurit.x19.api.likeComment
import net.nekocurit.x19.api.sendItemComment
import net.nekocurit.x19.data.X19Entity
import kotlin.time.Instant

/**
 * 用户对组件评论数据
 *
 * @param content 实际内容
 * @param createAt 创建时间
 * @param senderId 发送者Id
 * @param masterId 该评论所属父评论Id
 * @param replyId 该评论回复的评论Id
 */
@Serializable
data class X19Comment(
    @SerialName("entity_id")
    val entityId: ULong,
    val content: String,
    @SerialName("create_time")
    @Serializable(with = InstantLongSSerializer::class)
    val createAt: Instant,
    @SerialName("user_id")
    val senderId: ULong,
    @SerialName("master_id")
    val masterId: ULong,
    @SerialName("reply_id")
    val replyId: ULong,
    @SerialName("total_like")
    val totalLike: ULong,
): X19Entity() {
    /**
     * 组件Id
     */
    var itemId: ULong = 0UL

    suspend fun getSender() = api.getUser(senderId)

    suspend fun getMaster() = masterId
        .takeIf { it != 0UL }
        ?.let { api.getComment(it) }

    suspend fun getReply() = replyId
        .takeIf { it != 0UL }
        ?.let { api.getComment(it) }

    suspend fun like() { api.likeComment(senderId, itemId) }
    suspend fun getSubComments(page: Int = 0) = api.getItemComments(itemId, entityId, page)
    suspend fun reply(message: String) = api.sendItemComment(itemId, message, masterId.takeIf { it != 0UL} ?: entityId)
}