package net.nekocurit.x19.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

@Serializable
data class ResponseX19BaseMulti(
    val code: Int,
    val message: String = "",
    val details: String = "",
    val entities: JsonElement = JsonNull
) {
    val isOk
        get() = code == 0

    fun throwOnNotOk() = apply {
        if (!isOk) error(message)
    }
}