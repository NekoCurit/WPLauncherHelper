package net.nekocurit.x19.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.decodeFromJsonElement
import net.nekocurit.utils.json
import net.nekocurit.x19.WPLauncherAccountAPI

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

    inline fun <reified T: X19Entity> decode(api: WPLauncherAccountAPI, block: T.() -> Unit = {}) = json
        .decodeFromJsonElement<List<T>>(entities)
        .onEach {
            it.api = api
            block(it)
        }
}