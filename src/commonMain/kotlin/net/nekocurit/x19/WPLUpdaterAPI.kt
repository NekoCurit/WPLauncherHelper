package net.nekocurit.x19

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import net.nekocurit.utils.json
import net.nekocurit.x19.data.patch.LatestInfo
import net.nekocurit.x19.data.patch.PatchInfo
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

object WPLUpdaterAPI {

    var cache = Pair(Instant.DISTANT_PAST, LatestInfo("", "", ""))

    suspend fun get(disableCache: Boolean = false) = cache
        .takeIf { it.first > Clock.System.now() && !disableCache }
        ?.second
        ?: fetch()
            .also {
                cache = Clock.System.now() + 1.hours to it
            }

    suspend fun fetch() = WPLauncherAPI.client.get("https://x19.update.netease.com/pl/x19_java_patchlist")
        .bodyAsText().removeSuffix(",\n").let { "{$it}" }
        .let { body ->
            json.decodeFromString<Map<String, PatchInfo>>(body)
        }
        .let { versions ->
            val latest = versions.keys.last()
            val patch = versions.getValue(latest)

            LatestInfo(latest, patch.url, patch.md5)
        }


}