package net.nekocurit.x19.data.image

import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.x19.data.X19Entity

/**
 * @param token 上传凭据
 * @param url 上传端点 **这个不是图片直链**
 */
@Suppress("SpellCheckingInspection")
@Serializable
data class X19ImageUpload(
    val token: String,
    val url: String
): X19Entity() {

    @Serializable
    data class ImageMetadata(
        @SerialName("fsize")
        val fileSize: ULong,
        val md5: String,
        val mime: String,
        @SerialName("picSize")
        val size: List<UInt>,
        val url: String
    )

    /**
     * 上传图片 理论同一凭证可重复使用
     *
     * 上传后会进入机器审核 (返回的url需要等待几秒才能使用)
     *
     * **使用时请遵循中国大陆相关法律法规, 不要上传违规图片**
     *
     * @param image 图片数据
     */
    suspend fun upload(image: ByteArray) = api.client.post(url) {
        header(HttpHeaders.Authorization, token)
        setBody(image)
    }
        .body<ImageMetadata>()
}