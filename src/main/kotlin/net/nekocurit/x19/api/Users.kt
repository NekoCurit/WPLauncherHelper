package net.nekocurit.x19.api

import io.ktor.client.call.*
import net.nekocurit.x19.WPLauncherAccountAPI
import net.nekocurit.x19.data.ResponseX19Base
import net.nekocurit.x19.data.ResponseX19BaseMulti
import net.nekocurit.x19.data.user.X19User

suspend fun WPLauncherAccountAPI.getUser(id: ULong) = postWithAuth(
    path = "/user/query/search-by-uid",
    body = """{"user_id":"$id"}"""
)
    .body<ResponseX19Base>()
    .throwOnNotOk()
    .decode<X19User>(this)

suspend fun WPLauncherAccountAPI.getUser(ids: Collection<ULong>) = postWithAuth(
    path = "/user/query/search-by-ids",
    body = """{"entity_ids":[${ids.joinToString(",")}]}"""
)
    .body<ResponseX19BaseMulti>()
    .throwOnNotOk()
    .decode<X19User>(this)

/**
 * 搜索陌生人
 *
 * @param input 名称或者邮箱
 * @param isMail 是否为邮箱搜索
 */
suspend fun WPLauncherAccountAPI.searchFriends(input: String, isMail: Boolean) = postWithAuth("/user-search-friend", """{"name_or_mail":"$input","mail_flag":$isMail}""")
    .body<ResponseX19BaseMulti>()
    .throwOnNotOk()
    .decode<X19User>(this)

/**
 * 主动添加好友
 *
 * @param id 用户id
 * @param message 消息
 * @param selfNick 自身名称 通过`getSelfDetail`获取
 */
suspend fun WPLauncherAccountAPI.sendFriendRequest(id: ULong, message: String = "", selfNick: String = cacheName) = postWithAuth(
    path = "/user-apply-friend",
    body = """{"fid":$id,"comment":"$selfNick","message":"$message"}"""
)
    .body<ResponseX19Base>()
    .throwOnNotOk()