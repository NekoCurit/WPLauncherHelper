package net.nekocurit.x19.data.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.x19.data.X19AuthEntity

@Serializable
data class X19ItemTitleImage(
    @SerialName("item_id")
    val itemId: ULong,
    @SerialName("title_image_url")
    val url: String
): X19AuthEntity()