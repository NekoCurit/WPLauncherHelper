package net.nekocurit.x19.data.status

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import net.nekocurit.x19.data.X19Entity

@Serializable
data class X19UserStatus(
    @Serializable(with = Status.Serializer::class)
    val status: Status = Status.ONLINE,
    @Serializable(with = Hint.Serializer::class)
    val hint: Hint? = null
): X19Entity() {

    @Serializable
    data class Hint(
        @SerialName("game_name")
        val gameName: String,
        @SerialName("game_type")
        val gameType: UInt,
        @SerialName("game_id")
        val gameId: ULong,
        @SerialName("host_id")
        val hostId: ULong,
    ) {
        fun quickToJson() = """{"game_name":"$gameName","game_type":$gameType,"game_id":"$gameId","host_id":"$hostId"}"""
        object Serializer: KSerializer<Hint?> {
            override val descriptor = PrimitiveSerialDescriptor("Hint", PrimitiveKind.STRING)
            override fun serialize(encoder: Encoder, value: Hint?) = encoder.encodeString(value?.quickToJson() ?: "")
            override fun deserialize(decoder: Decoder) = decoder.decodeString()
                .takeIf { it.isNotEmpty() }
                ?.let { Json.decodeFromString<Hint>(it) }
        }
    }

    @Serializable
    enum class Status {
        /**
         * 离线
         */
        OFFLINE,
        /**
         * 在线
         */
        ONLINE,
        /**
         * 忙碌
         */
        BUSY,
        /**
         * 离开
         */
        LEAVE;
        object Serializer: KSerializer<Status> {
            override val descriptor = PrimitiveSerialDescriptor("Status", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: Status) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = Status.entries[decoder.decodeInt()]
        }
    }
}