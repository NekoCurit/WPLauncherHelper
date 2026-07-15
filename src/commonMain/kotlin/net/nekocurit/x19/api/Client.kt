package net.nekocurit.x19.api

import io.ktor.client.call.*
import net.nekocurit.x19.WPLauncherAPI
import net.nekocurit.x19.data.ResponseX19BaseMulti
import net.nekocurit.x19.data.client.X19ClientVersion

/**
 * 获取版本列表
 * 注: 存在两个明显不是 mc 版本的版本号 (`100.0.0`, `200.0.0`), 初步推测是 be 版
 */
suspend fun WPLauncherAPI.getMCVersions() = get(
    path = "/mc-version",
)
    .body<ResponseX19BaseMulti>()
    .throwOnNotOk()
    .decodeNoAuth<X19ClientVersion>()