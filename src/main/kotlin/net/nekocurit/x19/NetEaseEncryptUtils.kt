package net.nekocurit.x19

import net.nekocurit.utils.nextString
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.random.Random

object NetEaseEncryptUtils {

    @Suppress("SpellCheckingInspection")
    const val SALT = "0eGsBkhl"

    fun computeDynamicToken(token: String, path: String, body: String): String {
        val salt = "${token.md5()}$body$SALT/${path.removePrefix("/")}".md5()
        var binary = salt.toByteArray().toBinary()
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

    fun String.md5() = this.toByteArray().md5()
    fun ByteArray.md5() = MessageDigest.getInstance("MD5")
        .digest(this)
        .toHexString()

    fun ByteArray.toBinary(): String =
        joinToString("") {
            String.format("%8s", Integer.toBinaryString(it.toInt() and 0xFF))
                .replace(' ', '0')
        }

    @Suppress("SpellCheckingInspection")
    val keys = arrayOf(
        "MK6mipwmOUedplb6".toByteArray(StandardCharsets.US_ASCII),
        "OtEylfId6dyhrfdn".toByteArray(StandardCharsets.US_ASCII),
        "VNbhn5mvUaQaeOo9".toByteArray(StandardCharsets.US_ASCII),
        "bIEoQGQYjKd02U0J".toByteArray(StandardCharsets.US_ASCII),
        "fuaJrPwaH2cfXXLP".toByteArray(StandardCharsets.US_ASCII),
        "LEkdyiroouKQ4XN1".toByteArray(StandardCharsets.US_ASCII),
        "jM1h27H4UROu427W".toByteArray(StandardCharsets.US_ASCII),
        "DhReQada7gZybTDk".toByteArray(StandardCharsets.US_ASCII),
        "ZGXfpSTYUvcdKqdY".toByteArray(StandardCharsets.US_ASCII),
        "AZwKf7MWZrJpGR5W".toByteArray(StandardCharsets.US_ASCII),
        "amuvbcHw38TcSyPU".toByteArray(StandardCharsets.US_ASCII),
        "SI4QotspbjhyFdT0".toByteArray(StandardCharsets.US_ASCII),
        "VP4dhjKnDGlSJtbB".toByteArray(StandardCharsets.US_ASCII),
        "UXDZx4KhZywQ2tcn".toByteArray(StandardCharsets.US_ASCII),
        "NIK73ZNvNqzva4kd".toByteArray(StandardCharsets.US_ASCII),
        "WeiW7qU766Q1YQZI".toByteArray(StandardCharsets.US_ASCII)
    )

    val algorithm
        get() = Cipher.getInstance("AES/CBC/NoPadding")!!

    fun httpEncrypt(body: String) = (Random.nextInt(keys.size) to body.toByteArray())
        .let { (index, bytes) ->
            ByteArrayOutputStream(16 - bytes.size % 16 + bytes.size + 33)
                .apply {
                    val array = Random.nextBytes(16)
                    write(array)

                    // 由于 java 加密不支持 PaddingMode.Zeros 特性  需要手动补充零
                    CipherOutputStream(
                        this,
                        algorithm
                            .apply {
                                init(Cipher.ENCRYPT_MODE, SecretKeySpec(keys[index], "AES"), IvParameterSpec(array))
                            }
                    ).use { es ->
                        ByteArrayOutputStream()
                            .use { bs ->
                                bs.write(bytes)
                                bs.write(Random.nextString(16).toByteArray(Charsets.US_ASCII))

                                bs.toByteArray()
                            }
                            .let { input ->
                                val padLength = (16 - (input.size % 16)) % 16
                                if (padLength == 0) input else input + ByteArray(padLength) { 0 }
                            }
                            .also { es.write(it) }
                    }

                    write((index shl 4 or 2).toByte().toInt())
                }
                .toByteArray()!!
        }

    fun httpDecrypt(encode: ByteArray) = algorithm
        .apply {
            init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(keys[(encode.last().toInt() shr 4) and 0xF], "AES"),
                IvParameterSpec(encode.copyOfRange(0, 16))
            )
        }
        .doFinal(encode.copyOfRange(16, encode.size - 1)) // 去IV + 尾部 + 最后 key 字节
        // 去除尾部无效数据
        .let { decrypted ->
            decrypted
                .indexOfLast { it != 0.toByte() }
                .takeIf { it >= 15 }
                ?.let { decrypted.copyOfRange(0, it - 15).decodeToString() }
                ?: error("invalid decrypted data")
        }
}