package net.nekocurit.cipher

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.MessageDigest

class Skip32Cipher(val key: ByteArray = "SaintSteve".toByteArray()) {

    init {
        require(key.size == KEY_SIZE) { "Key must be $KEY_SIZE bytes." }
    }

    companion object {
        const val KEY_SIZE = 10
        val F_TABLE = intArrayOf(
            163, 215, 9, 131, 248, 72, 246, 244, 179, 33, 21, 120, 153, 177, 175, 249,
            231, 45, 77, 138, 206, 76, 202, 46, 82, 149, 217, 30, 78, 56, 68, 40,
            10, 223, 2, 160, 23, 241, 96, 104, 18, 183, 122, 195, 233, 250, 61, 83,
            150, 132, 107, 186, 242, 99, 154, 25, 124, 174, 229, 245, 247, 22, 106, 162,
            57, 182, 123, 15, 193, 147, 129, 27, 238, 180, 26, 234, 208, 145, 47, 184,
            85, 185, 218, 133, 63, 65, 191, 224, 90, 88, 128, 95, 102, 11, 216, 144,
            53, 213, 192, 167, 51, 6, 101, 105, 69, 0, 148, 86, 109, 152, 155, 118,
            151, 252, 178, 194, 176, 254, 219, 32, 225, 235, 214, 228, 221, 71, 74, 29,
            66, 237, 158, 110, 73, 60, 205, 67, 39, 210, 7, 212, 222, 199, 103, 24,
            137, 203, 48, 31, 141, 198, 143, 170, 200, 116, 220, 201, 93, 92, 49, 164,
            112, 136, 97, 44, 159, 13, 43, 135, 80, 130, 84, 100, 38, 125, 3, 64,
            52, 75, 28, 115, 209, 196, 253, 59, 204, 251, 127, 171, 230, 62, 91, 165,
            173, 4, 35, 156, 20, 81, 34, 240, 41, 121, 113, 126, 255, 140, 14, 226,
            12, 239, 188, 114, 117, 111, 55, 161, 236, 211, 142, 98, 139, 134, 16, 232,
            8, 119, 17, 190, 146, 79, 36, 197, 50, 54, 157, 207, 243, 166, 187, 172,
            94, 108, 169, 19, 87, 37, 181, 227, 189, 168, 58, 1, 5, 89, 42, 70
        )
    }

    fun ByteArray.roundG(k: Int, w: Int): Int {
        val num1 = w shr 8
        val num2 = w and 0xFF
        val num3 = (F_TABLE[num2 xor (this[(4 * k) % 10].toInt() and 0xFF)] xor num1)
        val num4 = (F_TABLE[num3 xor (this[(4 * k + 1) % 10].toInt() and 0xFF)] xor num2)
        val num5 = (F_TABLE[num4 xor (this[(4 * k + 2) % 10].toInt() and 0xFF)] xor num3)
        val num6 = (F_TABLE[num5 xor (this[(4 * k + 3) % 10].toInt() and 0xFF)] xor num4)

        return (num5 shl 8) or num6
    }

    fun IntArray.skip32(encrypt: Boolean) = apply {
        val step = if (encrypt) 1 else -1
        var k = if (encrypt) 0 else 23

        var w1 = (this[0] shl 8) or this[1]
        var w2 = (this[2] shl 8) or this[3]

        repeat(12) {
            w2 = w2 xor key.roundG(k, w1) xor k
            k += step
            w1 = w1 xor key.roundG(k, w2) xor k
            k += step
        }

        this[0] = w2 shr 8
        this[1] = w2 and 0xFF
        this[2] = w1 shr 8
        this[3] = w1 and 0xFF
    }

    fun encrypt(value: Int) = intArrayOf(value shr 24 and 0xFF, value shr 16 and 0xFF, value shr 8 and 0xFF, value and 0xFF)
        .skip32(true)
        .let { (it[0] shl 24) or (it[1] shl 16) or (it[2] shl 8) or it[3] }

    fun decrypt(value: Int) = intArrayOf(value shr 24 and 0xFF, value shr 16 and 0xFF, value shr 8 and 0xFF, value and 0xFF)
        .skip32(false)
        .let { (it[0] shl 24) or (it[1] shl 16) or (it[2] shl 8) or it[3] }

    fun generateRoleUuid(name: String, id: ULong) = MessageDigest
        .getInstance("MD5")
        .digest((name.toByteArray()))
        .apply {
            val encrypted = encrypt(id.toInt())

            this[12] = (encrypted shr 24).toByte()
            this[13] = (encrypted shr 16).toByte()
            this[14] = (encrypted shr 8).toByte()
            this[15] = encrypted.toByte()

            this[6] = ((this[6].toInt() and 0x0F) or 0x40).toByte()
            this[8] = ((this[8].toInt() and 0x3F) or 0x80).toByte()
        }
        .toHexString()

    fun computeUserIdFromUuid(uuid: String) = uuid
        .replace("-", "")
        .apply { require(length == 32) { "长度不符" } }
        .hexToByteArray()
        .let { ByteBuffer.wrap(it, 12, 4) }
        .order(ByteOrder.LITTLE_ENDIAN)
        .let { decrypt(it.int) }
        .toULong()

}