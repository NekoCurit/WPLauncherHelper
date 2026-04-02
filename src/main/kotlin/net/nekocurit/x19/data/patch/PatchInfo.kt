package net.nekocurit.x19.data.patch

import kotlinx.serialization.Serializable

@Serializable
data class PatchInfo(
    val md5: String,
    val url: String
)