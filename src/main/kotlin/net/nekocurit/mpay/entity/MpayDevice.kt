package net.nekocurit.mpay.entity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class MpayDevice(
    var uniqueId: String = "",
    val id: String,
    @Serializable(with = KeySerializer::class)
    val key: ByteArray
) {
    object KeySerializer: KSerializer<ByteArray> {
        override val descriptor = PrimitiveSerialDescriptor("Key", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: ByteArray) = encoder.encodeString(value.toHexString())
        override fun deserialize(decoder: Decoder) = decoder.decodeString().hexToByteArray()
    }

    override fun equals(other: Any?) = other is MpayDevice && hashCode() == other.hashCode()
    override fun hashCode() = id.hashCode()
}
