package net.nekocurit.x19.data.skin

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.nekocurit.x19.data.X19Entity

@Serializable
data class X19Skin(
    @SerialName("user_id")
    val userId: ULong,
    @SerialName("game_type")
    val gameType: UInt,
    @SerialName("skin_type")
    val skinType: UInt,
    @SerialName("skin_id")
    val skinId: ULong,
    @SerialName("skin_mode")
    @Serializable(with = SkinMode.Serializer::class)
    val skinMode: SkinMode,
    @SerialName("client_type")
    val clientType: String
): X19Entity() {
    enum class SkinMode {
        DEFAULT,
        SLIM;
        object Serializer: KSerializer<SkinMode> {
            override val descriptor = PrimitiveSerialDescriptor("SkinMode", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: SkinMode) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = SkinMode.entries[decoder.decodeInt()]
        }
    }
}