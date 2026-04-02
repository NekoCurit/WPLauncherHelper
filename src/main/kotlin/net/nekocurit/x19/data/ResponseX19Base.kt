package net.nekocurit.x19.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ResponseX19Base(
    val code: Int,
    val message: String = "",
    val details: String = "",
    val entity: JsonElement
) {
    val isOk
        get() = code == 0

    fun throwOnNotOk() = apply {
        if (!isOk) error(message)
    }
}