package net.nekocurit.mpay

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import net.nekocurit.mpay.data.RespondMpayDeviceRegister
import net.nekocurit.mpay.data.RespondMpayLogin
import net.nekocurit.mpay.data.RespondMpayLoginError
import net.nekocurit.mpay.entity.MpayDevice
import net.nekocurit.mpay.utils.MpayLoginEncrypt
import net.nekocurit.utils.json
import net.nekocurit.utils.nextMacAddress
import net.nekocurit.utils.nextString
import net.nekocurit.x19.WPLUpdaterAPI
import java.util.*
import kotlin.io.encoding.Base64
import kotlin.random.Random

class UniSdkMpay(val project: String = "aecfrxodyqaaaajp-g-x19", val version: String = runBlocking { WPLUpdaterAPI.get().version }) {

    val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
        defaultRequest {
            url("https://service.mkey.163.com")
        }
    }

    /**
     * 注册设备
     * 此接口调用频繁会风控 强烈建议缓存设备数据
     */
    suspend fun registerDevice(id: String = UUID.randomUUID().toString().replace("-", "")) = client
        .submitForm(
            url = "/mpay/games/$project/devices",
            formParameters = buildMpayParameters {
                append("unique_id", id)
                append("brand", "Microsoft")
                append("device_model", "pc_mode")
                append("device_name", "Desktop-${Random.nextString(6)}")
                append("device_type", "Computer")
                append("init_urs_device", "0")
                append("mac", Random.nextMacAddress())
                append("resolution", "1920x1080")
                append("system_name", "windows")
                append("system_version", "10.0.22621")
            }
        )
        .checkError()
        .body<RespondMpayDeviceRegister>()
        .device
        .apply { uniqueId = id }

    /**
     * 登陆
     *
     * @param device 设备标识
     * @param email 邮箱
     * @param password 密码
     */
    suspend fun login(device: MpayDevice, email: String, password: String) = client
        .submitForm(
            url = "/mpay/games/$project/devices/${device.id}/users",
            formParameters = buildMpayParameters {
                @Suppress("SpellCheckingInspection")
                append("opt_fields", "nickname,avatar,realname_status,mobile_bind_status,mask_related_mobile,related_login_status")
                append("params", MpayLoginEncrypt.encryptLoginParams(email, password, device))
                append("un", Base64.encode(email.toByteArray()))
            }
        )
        .checkError()
        .body<RespondMpayLogin>()
        .user

    @Suppress("SpellCheckingInspection")
    private fun buildMpayParameters(block: ParametersBuilder.() -> Unit) = Parameters.build {
        append("app_channel", "netease")
        append("app_mode", "2")
        append("app_type", "games")
        append("arch", "win_x64")
        append("cv", "c4.2.0")
        append("mcount_app_key", "EEkEEXLymcNjM42yLY3Bn6AO15aGy4yq")
        append("mcount_transaction_id", "0")
        append("process_id", "5555")
        append("sv", "10.0.22621")
        append("updater_cv", "c1.0.0")
        append("game_id", project)
        append("gv", version)

        block(this)
    }

    private suspend fun HttpResponse.checkError() = apply {
        if (!this.status.isSuccess()) error(body<RespondMpayLoginError>().toString())
    }


}