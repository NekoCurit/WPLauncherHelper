@file:Suppress("SpellCheckingInspection")

package net.nekocurit.x19.data.cloud_save

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.utils.serializer.InstantLongSSerializer
import net.nekocurit.x19.data.X19Entity
import kotlin.time.Instant

@Serializable
data class X19CloudSave(
    @SerialName("entity_id")
    val id: ULong,
    val name: String,
    @SerialName("user_id")
    val userId: ULong,
    val others: String,
    @SerialName("partinfo")
    val part: List<Part>,
    @SerialName("update_time")
    @Serializable(with = InstantLongSSerializer::class)
    val updateAt: Instant,
    val version: UInt,
    @SerialName("vip_only")
    val vipOnly: Boolean,
): X19Entity() {
    @Serializable
    data class Part(
        val md5: String,
        val size: Int
    )
}