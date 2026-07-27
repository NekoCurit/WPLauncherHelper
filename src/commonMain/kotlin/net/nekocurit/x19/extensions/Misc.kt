package net.nekocurit.x19.extensions

import io.ktor.client.call.*
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import net.nekocurit.x19.WPLauncherAPI
import net.nekocurit.x19.data.ResponseX19Base
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

/**
 * 获取服务端时间
 */
suspend fun WPLauncherAPI.getServerTime() = get(
    path = "/server-time",
)
    .body<ResponseX19Base>()
    .throwOnNotOk()
    .entity.jsonObject["current"]!!.jsonPrimitive.long
    .let { Instant.fromEpochSeconds(it) + 8.hours }