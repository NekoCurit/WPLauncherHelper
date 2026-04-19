package net.nekocurit.utils

import kotlin.random.Random

fun Random.nextString(length: Int, charsets: String = "0123456789abcde") = (1..length)
    .map { charsets.random(this) }
    .joinToString("")

fun Random.nextMacAddress(separator: String = ":", uppercase: Boolean = true) = ByteArray(6)
    .also { mac ->
        nextBytes(mac)

        mac[0] = (mac[0].toInt() and 0xFE).toByte()
        mac[0] = (mac[0].toInt() or 0x02).toByte()
    }
    .joinToString(separator) {
        String.format(if (uppercase) "%02X" else "%02x", it.toInt() and 0xFF)
    }