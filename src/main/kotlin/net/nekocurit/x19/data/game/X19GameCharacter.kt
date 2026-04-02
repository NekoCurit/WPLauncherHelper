package net.nekocurit.x19.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.utils.serializer.InstantLongSSerializer
import net.nekocurit.x19.api.applyDeleteNetworkServerCharacter
import net.nekocurit.x19.api.requestDeleteNetworkServerCharacter
import net.nekocurit.x19.data.X19Entity
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

@Serializable
data class X19GameCharacter(
    @SerialName("entity_id")
    val id: ULong,
    val name: String,
    @SerialName("create_time")
    @Serializable(with = InstantLongSSerializer::class)
    val createAt: Instant,
    @SerialName("expire_time")
    @Serializable(with = InstantLongSSerializer::class)
    val expireAt: Instant,
    @SerialName("game_id")
    val gameId: ULong,
    @SerialName("game_type")
    val gameType: UInt
): X19Entity() {

    /**
     * 是否处于等待删除状态
     */
    val isPendingDelete
        get() = expireAt.toEpochMilliseconds() != 0L

    /**
     * 是否可以立刻删除此角色
     */
    val canDelete
        get() = isPendingDelete && expireAt + 30.days < Clock.System.now()

    /**
     * 请求删除角色
     * 请求后需要等待 30 天才能执行 `applyDelete` 删除角色
     */
    suspend fun requestDelete() { api.requestDeleteNetworkServerCharacter(id) }
    /**
     * 删除角色 需先请求删除
      */
    suspend fun applyDelete() { api.applyDeleteNetworkServerCharacter(id) }

}