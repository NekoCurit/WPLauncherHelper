package net.nekocurit.x19

import korlibs.crypto.AES
import korlibs.crypto.CipherMode.Companion.CBC
import korlibs.crypto.CipherPadding.Companion.NoPadding
import korlibs.crypto.with
import net.nekocurit.utils.md5
import net.nekocurit.utils.nextString
import kotlin.io.encoding.Base64
import kotlin.random.Random

object NetEaseEncryptUtils {

    @Suppress("SpellCheckingInspection")
    const val SALT = "0eGsBkhl"

    fun computeDynamicToken(token: String, path: String, body: String): String {
        val salt = "${token.md5()}$body$SALT/${path.removePrefix("/")}".md5()
        var binary = salt
            .toByteArray()
            .joinToString("") {
                String.format("%8s", Integer.toBinaryString(it.toInt() and 0xFF))
                    .replace(' ', '0')
            }
        binary = binary.substring(6) + binary.substring(0, 6)

        return salt.toByteArray()
            .apply {
                for (i in 0 until 12) {
                    var b: Byte = 0
                    var num = i * 8 + 7

                    for (j in 0 until 8) {
                        if (binary[num] == '1') {
                            b = (b.toInt() or (1 shl (j and 0x1F))).toByte()
                        }
                        num--
                    }

                    this[i] = (this[i].toInt() xor b.toInt()).toByte()
                }
            }
            .copyOfRange(0, 12)
            .let { Base64.encode(it) }
            .plus("1")
            .replace('+', 'm')
            .replace('/', 'o')
    }

    @Suppress("SpellCheckingInspection")
    val keys = arrayOf(
        "MK6mipwmOUedplb6".toByteArray(Charsets.US_ASCII),
        "OtEylfId6dyhrfdn".toByteArray(Charsets.US_ASCII),
        "VNbhn5mvUaQaeOo9".toByteArray(Charsets.US_ASCII),
        "bIEoQGQYjKd02U0J".toByteArray(Charsets.US_ASCII),
        "fuaJrPwaH2cfXXLP".toByteArray(Charsets.US_ASCII),
        "LEkdyiroouKQ4XN1".toByteArray(Charsets.US_ASCII),
        "jM1h27H4UROu427W".toByteArray(Charsets.US_ASCII),
        "DhReQada7gZybTDk".toByteArray(Charsets.US_ASCII),
        "ZGXfpSTYUvcdKqdY".toByteArray(Charsets.US_ASCII),
        "AZwKf7MWZrJpGR5W".toByteArray(Charsets.US_ASCII),
        "amuvbcHw38TcSyPU".toByteArray(Charsets.US_ASCII),
        "SI4QotspbjhyFdT0".toByteArray(Charsets.US_ASCII),
        "VP4dhjKnDGlSJtbB".toByteArray(Charsets.US_ASCII),
        "UXDZx4KhZywQ2tcn".toByteArray(Charsets.US_ASCII),
        "NIK73ZNvNqzva4kd".toByteArray(Charsets.US_ASCII),
        "WeiW7qU766Q1YQZI".toByteArray(Charsets.US_ASCII)
    )

    fun httpEncrypt(body: String) = Random.nextInt(keys.size)
        .let { index ->
            val input = body.toByteArray() + Random.nextString(16).toByteArray(Charsets.US_ASCII)

            val iv = Random.nextBytes(16)
            val encrypted = AES(keys[index])
                .with(CBC, NoPadding, iv)
                .encrypt(input + ByteArray((16 - input.size % 16) % 16))

            return@let iv + encrypted + byteArrayOf((index shl 4 or 2).toByte())
        }

    fun httpDecrypt(encode: ByteArray) = AES(keys[(encode.last().toInt() shr 4) and 0xF])
        .with(CBC, NoPadding, encode.copyOfRange(0, 16))
        .decrypt(encode, 16, encode.size - 17)
        .let { decrypted ->
            decrypted
                .indexOfLast { it != 0.toByte() }
                .takeIf { it >= 15 }
                ?.let { decrypted.copyOfRange(0, it - 15).decodeToString() }
                ?: error("invalid decrypted data")
        }
}