package net.nekocurit.i4399

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object I4399EncryptUtils {
    fun encrypt(text: String): String {
        val password = "lzYW5qaXVqa".toByteArray()
        val salt = ByteArray(8).also { SecureRandom().nextBytes(it) }

        val keyIv = ByteArray(48)
        var prev = ByteArray(0)
        var offset = 0

        while (offset < 48) {
            val hash = MessageDigest.getInstance("MD5").digest(prev + password + salt)
            val len = minOf(hash.size, 48 - offset)
            System.arraycopy(hash, 0, keyIv, offset, len)
            offset += len
            prev = hash
        }

        val key = keyIv.copyOfRange(0, 32)
        val iv = keyIv.copyOfRange(32, 48)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))

        val encrypted = cipher.doFinal(text.toByteArray())

        val out = "Salted__".toByteArray() + salt + encrypted
        return Base64.getEncoder().encodeToString(out)
    }
}