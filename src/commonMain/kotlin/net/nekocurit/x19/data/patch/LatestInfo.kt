package net.nekocurit.x19.data.patch

import kotlinx.serialization.Serializable

@Serializable
data class LatestInfo(
    val version: String,
    val url: String,
    val md5: String
)