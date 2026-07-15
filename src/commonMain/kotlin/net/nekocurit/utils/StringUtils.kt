package net.nekocurit.utils

import io.ktor.utils.io.core.toByteArray
import korlibs.crypto.md5

fun String.md5() = this.toByteArray().md5().toString()