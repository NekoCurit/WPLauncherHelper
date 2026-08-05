package net.nekocurit.x19.api

import io.ktor.client.call.*
import net.nekocurit.utils.json
import net.nekocurit.x19.WPLauncherAccountAPI
import net.nekocurit.x19.data.ResponseX19Base
import net.nekocurit.x19.data.status.X19UserStatus

/**
 * 更新在线状态
 *
 * @param status 状态
 */
suspend fun WPLauncherAccountAPI.updateStatus(status: X19UserStatus) = postWithAuth(
    path = "/user-change-status",
    body = """{"status_json":"${json.encodeToString(status)}","entity_id":null}"""
)
    .body<ResponseX19Base>()
    .throwOnNotOk()
    .let { }
