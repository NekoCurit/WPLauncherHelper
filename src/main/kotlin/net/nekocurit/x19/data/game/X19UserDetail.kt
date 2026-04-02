package net.nekocurit.x19.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromJsonElement
import net.nekocurit.utils.json
import net.nekocurit.x19.data.ResponseX19Base

@Serializable
data class X19UserDetail(
    val name: String,
    @SerialName("avatar_image_url")
    val avatarUrl: String,
    val level: ULong,
    @SerialName("is_vip")
    val isVip: Boolean,
    val score: Long
) {

    companion object {
        fun ResponseX19Base.asX19UserDetail() = json.decodeFromJsonElement<X19UserDetail>(this.entity)
    }
}