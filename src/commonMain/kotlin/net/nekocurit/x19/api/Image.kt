package net.nekocurit.x19.api

import io.ktor.client.call.body
import net.nekocurit.x19.WPLauncherAccountAPI
import net.nekocurit.x19.data.ResponseX19Base
import net.nekocurit.x19.data.image.X19ImageUpload

/**
 * 请求图片上传凭证
 */
suspend fun WPLauncherAccountAPI.requestImageUpload() = postWithAuth(
    path = "/image-upload-token",
    body = """{"token":null,"url":null,"verify":false,"entity_id":null}"""
)
    .body<ResponseX19Base>()
    .throwOnNotOk()
    .decode<X19ImageUpload>(this)