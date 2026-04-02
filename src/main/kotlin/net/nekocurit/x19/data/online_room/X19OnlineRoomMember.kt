package net.nekocurit.x19.data.online_room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.nekocurit.x19.data.X19Entity

@Serializable
data class X19OnlineRoomMember(
    @SerialName("member_id")
    val id: ULong,
    val ident: UInt,
    @SerialName("recharge_benefit_level")
    val rechargeBenefitLevel: UInt,
): X19Entity() {
    val isAdmin
        get() = ident != 0U
}