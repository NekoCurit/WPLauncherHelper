package net.nekocurit.mpay.data

import kotlinx.serialization.Serializable
import net.nekocurit.mpay.entity.MpayDevice

@Serializable
data class RespondMpayDeviceRegister(
    val device: MpayDevice
)
