package net.nekocurit.x19.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.x19.data.X19Entity

@Serializable
data class X19SelfDetail(
    val name: String,
    @SerialName("avatar_image_url")
    val avatarUrl: String,
    val level: ULong,
    @SerialName("is_vip")
    val isVip: Boolean,
    val score: Long
): X19Entity()