package net.nekocurit.i4399.x19.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response4399X19Oauth(
    val code: Int,
    val result: Result,
    val message: String
) {
    @Serializable
    data class Result(
        @SerialName("login_url")
        val loginUrl: String,
        @SerialName("login_url_backup")
        val loginUrlBackup: String,
        @SerialName("login_url_phone")
        val loginUrlPhone: String
    )
}