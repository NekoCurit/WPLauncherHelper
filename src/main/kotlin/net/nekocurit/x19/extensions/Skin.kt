package net.nekocurit.x19.extensions

import net.nekocurit.cipher.Skip32Cipher
import net.nekocurit.x19.WPLauncherAccountAPI
import net.nekocurit.x19.api.getSkin

val cipher = Skip32Cipher()

/**
 * 根据游戏内玩家 UUID 获取玩家皮肤
 *
 * @param id UUID().toString()
 */
suspend fun WPLauncherAccountAPI.getSkinFromUUID(id: String) = getSkin(cipher.computeUserIdFromUuid(id))