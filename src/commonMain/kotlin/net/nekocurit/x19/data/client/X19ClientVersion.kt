package net.nekocurit.x19.data.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.x19.data.X19AuthEntity

@Serializable
data class X19ClientVersion(
    @SerialName("entity_id")
    val id: ULong,
    val name: String
): X19AuthEntity()