package net.nekocurit.x19.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromJsonElement
import net.nekocurit.utils.json
import net.nekocurit.x19.data.ResponseX19Base

@Serializable
data class X19Purchase(
    @SerialName("buy_type")
    val bugType: Int,
    @SerialName("entity_id")
    val orderId: ULong,
) {
    companion object {
        fun ResponseX19Base.asX19Purchase() = json.decodeFromJsonElement<X19Purchase>(this.entity)
    }
}