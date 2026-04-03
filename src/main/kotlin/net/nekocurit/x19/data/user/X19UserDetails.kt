package net.nekocurit.x19.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import net.nekocurit.utils.json
import kotlin.time.Instant

@Serializable
data class X19UserDetails(
    val nickname: String,
    @SerialName("headImage")
    val avatarUrl: String,
    @SerialName("uid")
    val id: ULong,
    /**
     * 0=离线
     * 1=电脑在线
     * 2=手机在线
     */
    @SerialName("online_pcpe")
    val online: UInt,
    @SerialName("tLogout")
    private val lastOnlineT: Long,
    @SerialName("online_status")
    private val rawOnlineStatus: String,
    @SerialName("game_info")
    private val rawGameInfo: JsonElement
) {

    val statusPc
        get() = rawOnlineStatus
            .takeIf { it.isNotBlank() }
            ?.let { json.decodeFromString<OnlineStatus>(it) }
            ?.hint

    val statusPe
        get() = runCatching {
            json.decodeFromJsonElement<OnlinePe>(rawGameInfo)
        }
            .getOrNull()

    val lastOnline
        get() = Instant.fromEpochMilliseconds(lastOnlineT * 1_000)

    @Serializable
    data class OnlinePe(
        @SerialName("game-type")
        val type: UInt,
        @SerialName("game-id")
        val id: ULong,
        @SerialName("game-info")
        private val rawInfo: String
    ) {

        val info
            get() = json.decodeFromString<Info>(rawInfo)

        @Serializable
        data class Info(
            @SerialName("room_name")
            val roomName: String,
            @SerialName("title_image_url")
            val titleImageUrl: String,
            @SerialName("res_name")
            val resName: String,
        )
    }

    @Serializable
    data class OnlinePc(
        @SerialName("game_name")
        val name: String,
        @SerialName("game_id")
        val rawId: String,
        @SerialName("game_type")
        val type: UInt,
        @SerialName("host_id")
        val host: String
    ) {
        val id
            get() = rawId.toULong()
        val hostId
            get() = host.toULong()
    }

    @Serializable
    data class OnlineStatus(
        val status: Int,
        @SerialName("hint")
        private val rawHint: String
    ) {

        val hint
            get() = rawHint
                .takeIf { it.isNotBlank() }
                ?.let { json.decodeFromString<OnlinePc>(it) }
    }
}