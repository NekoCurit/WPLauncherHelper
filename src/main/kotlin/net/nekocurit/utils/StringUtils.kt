package net.nekocurit.utils

import korlibs.crypto.md5

fun String.md5() = this.toByteArray().md5().toString()