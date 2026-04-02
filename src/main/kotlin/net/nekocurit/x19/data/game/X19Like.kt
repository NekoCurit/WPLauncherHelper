package net.nekocurit.x19.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromJsonElement
import net.nekocurit.utils.json
import net.nekocurit.x19.data.ResponseX19Base

@Serializable
data class X19Like(
    @SerialName("entity_id")
    val id: ULong,
    @SerialName("item_id")
    val itemId: ULong,
    @SerialName("has_like")
    private val rawHasLike: Int
) {
    val like
        get() = rawHasLike
            .takeIf { it != 0 }
            ?.let { it == 1 }

    companion object {
        fun ResponseX19Base.asX19Like() = json.decodeFromJsonElement<X19Like>(this.entity)
    }
}