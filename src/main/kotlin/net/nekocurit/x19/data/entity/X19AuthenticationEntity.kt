package net.nekocurit.x19.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromJsonElement
import net.nekocurit.utils.json
import net.nekocurit.x19.WPLauncherAccountAPI
import net.nekocurit.x19.data.ResponseX19Base

@Serializable
data class X19AuthenticationEntity(
    @SerialName("entity_id")
    val entityId: ULong,
    val aid: ULong = 0UL,
    val token: String,
    val sead: String = "",
    @SerialName("verify_status")
    val verifyStatus: Int = 2,
    @SerialName("hasGmail")
    val hasGmail: Boolean = false,
    @SerialName("is_register")
    val isNew: Boolean = false,
) {

    fun toAccountAPI() = WPLauncherAccountAPI(this)

    companion object {
        fun ResponseX19Base.asX19AuthenticationEntity() = json.decodeFromJsonElement<X19AuthenticationEntity>(this.entity)
    }
}