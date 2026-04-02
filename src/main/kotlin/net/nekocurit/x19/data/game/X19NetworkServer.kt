package net.nekocurit.x19.data.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromJsonElement
import net.nekocurit.utils.json
import net.nekocurit.x19.data.ResponseX19Base
import kotlin.time.Instant

@Serializable
data class X19NetworkServer(
    val name: String,
    @SerialName("brief_image_urls")
    val wallpaperUrls: List<String>,
    @SerialName("entity_id")
    private val rawId: String,
    @SerialName("brief_summary")
    val simpleDescription: String,
    @SerialName("detail_description")
    val fullDescription: String,
    @SerialName("developer_name")
    private val rawDeveloperName: String,
    @SerialName("developer_urs")
    private val rawDeveloperEmail: String,
    @SerialName("download_num")
    val totalDownload: ULong,
    @SerialName("edit_time")
    private val rawEditAt: Long,
    @SerialName("publish_time")
    private val rawPublishAt: Long,
    @SerialName("like_num")
    val totalLikes: ULong,
    @SerialName("recommend_tag")
    val recommendTag: String,
    @SerialName("recommend_total")
    val recommendTotal: ULong,
    @SerialName("recommend_ratio")
    val recommendRatio: Double,
    @SerialName("network_tag")
    val networkTag: String,
    @SerialName("online_count")
    val currentOnline: ULong,
    @SerialName("tag_list")
    val otherTags: List<Tag>
) {

    val id
        get() = rawId.toULong()

    val developer
        get() = Developer(rawDeveloperName, rawDeveloperEmail)

    val publishAt
        get() = Instant.fromEpochMilliseconds(rawPublishAt * 1000)

    val editAt
        get() = Instant.fromEpochMilliseconds(rawEditAt * 1000)

    @Serializable
    data class Developer(
        val name: String,
        val email: String
    )

    @Serializable
    data class Tag(
        val tid: Int,
        @Suppress("SpellCheckingInspection")
        @SerialName("itype")
        val type: Int,
        val name: String
    )


    companion object {
        fun ResponseX19Base.asX19NetworkServer() = json.decodeFromJsonElement<X19NetworkServer>(this.entity)
    }
}