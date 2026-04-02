package net.nekocurit.x19.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromJsonElement
import net.nekocurit.utils.json
import net.nekocurit.x19.data.ResponseX19Base
import net.nekocurit.x19.data.ResponseX19BaseMulti

/**
 * @param id 这里不是启动器显示的服务器号, 而是内部 id
 * @param version 游戏版本 例如 `1.12.2`
 * @param name 对外显示的服务器号
 */
@Serializable
data class X19RentalServer(
    @SerialName("entity_id")
    val id: ULong,
    @SerialName("image_url")
    val avatarUrl: String,
    @SerialName("mc_version")
    val version: String,
    val name: String,
    @SerialName("owner_id")
    val ownerId: ULong,
    @SerialName("player_count")
    val playerCount: UInt,
    val pvp: Boolean,
    @SerialName("server_name")
    val displayName: String,
    @SerialName("server_type")
    val type: String,
    @SerialName("has_pwd")
    private val rawHasPassword: Int
) {

    val hasPassword
        get() = rawHasPassword == 1

    companion object {
        fun ResponseX19Base.asX19RentalServer() = json.decodeFromJsonElement<X19RentalServer>(this.entity)
        fun ResponseX19BaseMulti.asX19RentalServer() = json.decodeFromJsonElement<List<X19RentalServer>>(this.entities)
    }
}