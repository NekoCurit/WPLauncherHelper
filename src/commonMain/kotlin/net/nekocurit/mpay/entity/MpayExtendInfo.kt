package net.nekocurit.mpay.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MpayExtendInfo(
    @SerialName("src_jf_game_id")
    val gameId: String,
    @SerialName("src_app_channel")
    val channel: String,
    @SerialName("from_game_id")
    val fromGameId: String,
    @Suppress("SpellCheckingInspection")
    @SerialName("src_udid")
    val userDeviceId: String,
    @SerialName("src_sdk_version")
    val sdkVersion: String,
    @SerialName("src_pay_channel")
    val payChannel: String,
    @SerialName("src_client_ip")
    val ip: String,
    @SerialName("extra_unisdk_data")
    val extra: String
)