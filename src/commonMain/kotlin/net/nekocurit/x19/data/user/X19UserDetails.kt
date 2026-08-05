package net.nekocurit.x19.data.user

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import net.nekocurit.utils.json
import net.nekocurit.utils.serializer.InstantLongSSerializer
import net.nekocurit.x19.data.X19AuthEntity
import net.nekocurit.x19.data.status.X19UserStatus
import kotlin.time.Instant

/**
 * @param onlineStatus 传统Java版在线信息
 * @param gameInfo 互通Bedrock版在线信息
 */
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
    @Serializable(with = InstantLongSSerializer::class)
    val lastOnline: Instant,
    @SerialName("online_status")
    @Serializable(with = OnlineStatusSerializer::class)
    val onlineStatus: X19UserStatus?,
    @SerialName("game_info")
    @Serializable(with = GameInfoSerializer::class)
    val gameInfo: GameInfo?
): X19AuthEntity() {

    @Serializable
    data class GameInfo(
        @SerialName("game-type")
        val type: UInt,
        @SerialName("game-id")
        val id: String,
        @SerialName("game-info")
        val info: String // 此字段过于复杂 不方便写解析
    )

    private object OnlineStatusSerializer: KSerializer<X19UserStatus?> {
        override val descriptor = PrimitiveSerialDescriptor("OnlineStatus", PrimitiveKind.STRING)
        override fun serialize(encoder: Encoder, value: X19UserStatus?) = encoder.encodeString(value?.let { value -> json.encodeToString(value) } ?: "")
        override fun deserialize(decoder: Decoder) = decoder.decodeString()
            .takeIf { it.isNotEmpty() }
            ?.let { Json.decodeFromString<X19UserStatus>(it) }
    }

    private object GameInfoSerializer: KSerializer<GameInfo?> {
        override val descriptor = PrimitiveSerialDescriptor("GameInfo", PrimitiveKind.STRING)
        override fun serialize(encoder: Encoder, value: GameInfo?) = (encoder as JsonEncoder).encodeJsonElement(json.encodeToJsonElement(value))
        override fun deserialize(decoder: Decoder) = (decoder as JsonDecoder).decodeJsonElement()
            .let { raw -> runCatching { json.decodeFromJsonElement<GameInfo>(raw) }.onFailure { it.printStackTrace() }.getOrNull() }
    }
}