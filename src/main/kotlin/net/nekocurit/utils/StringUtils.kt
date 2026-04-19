package net.nekocurit.utils

import java.security.MessageDigest

fun String.encodeMd5() = MessageDigest.getInstance("MD5")
    .digest(toByteArray())
    .toHexString()