package net.nekocurit.utils

import kotlinx.serialization.json.Json

val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}