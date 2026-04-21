package net.nekocurit.x19.extensions

import net.nekocurit.x19.WPLauncherAccountAPI
import net.nekocurit.x19.api.getItemComments
import net.nekocurit.x19.api.getTopItemComments
import net.nekocurit.x19.data.item.X19Comment


/**
 * 根据顶量获取组件评论 增强版
 *
 * @param id 组件Id
 * @param page 当前页码
 * @param length 每页长度
 */
suspend fun WPLauncherAccountAPI.getTopItemComments(id: ULong, page: Int = 0, length: Int = 5) = mutableListOf<X19Comment>()
    .apply {
        val start = page * length
        val end = start + length

        val queryIndex = start - start % 5
        val queryPageStart = queryIndex / 5

        repeat(((end - start + 4) / 5) + if (start % 5 == 0) 0 else 1) { i ->
            getTopItemComments(id, queryPageStart + i).forEachIndexed { j, comment ->
                if (i == 0 && queryIndex + j < start) return@forEachIndexed
                if (size == length) return@apply

                add(comment)
            }
        }
    }

/**
 * 获取组件评论 增强版
 *
 * @param id 组件Id
 * @param masterId 如果不为 0 则获取指定评论下的子评论
 * @param page 当前页码
 * @param length 每页长度
 */
suspend fun WPLauncherAccountAPI.getItemComments(id: ULong, masterId: ULong = 0UL, page: Int = 0, length: Int = 5) = mutableListOf<X19Comment>()
    .apply {
        val start = page * length
        val end = start + length

        val queryIndex = start - start % 5
        val queryPageStart = queryIndex / 5

        repeat(((end - start + 4) / 5) + if (start % 5 == 0) 0 else 1) { i ->
            getItemComments(id, masterId, queryPageStart + i).forEachIndexed { j, comment ->
                if (i == 0 && queryIndex + j < start) return@forEachIndexed
                if (size == length) return@apply

                add(comment)
            }
        }
    }