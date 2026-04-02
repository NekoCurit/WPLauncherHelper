package net.nekocurit.x19.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromJsonElement
import net.nekocurit.utils.json
import net.nekocurit.x19.data.ResponseX19Base
import net.nekocurit.x19.data.ResponseX19BaseMulti
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

@Serializable
data class X19Role(
    @SerialName("entity_id")
    val id: ULong,
    val name: String,
    @SerialName("create_time")
    private val rawCreateTime: Long,
    @SerialName("expire_time")
    private val rawExpireTime: Long
) {
    val createTime
        get() = Instant.fromEpochMilliseconds(rawCreateTime * 1000)

    /**
     * 是否处于等待删除状态
     */
    val isPendingDelete
        get() = rawExpireTime > 0

    /**
     * 是否可以立刻删除此角色
     */
    val canDelete
        get() = expireTime
            ?.let { it + 30.days < Clock.System.now() }
            ?: false

    /**
     * 角色等待删除时间
     */
    val expireTime
        get() = if (isPendingDelete) Instant.fromEpochMilliseconds(rawExpireTime) else null

    companion object {
        fun ResponseX19Base.asX19Role() = json.decodeFromJsonElement<X19Role>(this.entity)
        fun ResponseX19BaseMulti.asX19Role() = json.decodeFromJsonElement<List<X19Role>>(this.entities)
    }

}