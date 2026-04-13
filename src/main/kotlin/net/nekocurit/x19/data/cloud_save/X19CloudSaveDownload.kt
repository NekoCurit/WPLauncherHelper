@file:Suppress("SpellCheckingInspection")

package net.nekocurit.x19.data.cloud_save

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.x19.data.X19Entity

@Serializable
data class X19CloudSaveDownload(
    @SerialName("entity_id")
    val id: ULong,
    @SerialName("partnum")
    val part: UInt,
    @SerialName("active_time")
    val activeTime: UInt,
    val md5: String,
    val url: String
): X19Entity()