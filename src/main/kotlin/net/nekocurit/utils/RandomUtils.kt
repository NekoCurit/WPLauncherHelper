package net.nekocurit.utils

import kotlin.random.Random

fun Random.nextString(length: Int, charsets: String = "0123456789abcde") = (1..length)
    .map { charsets.random(this) }
    .joinToString("")