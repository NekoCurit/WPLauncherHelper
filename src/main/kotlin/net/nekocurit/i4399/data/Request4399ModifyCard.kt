package net.nekocurit.i4399.data

import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Request4399ModifyCard(
    @SerialName("__HASH__")
    val hash: String,
    val birthday: String = "2024-01-26",
    val nick: String,
    val sex: String = "1",
    @SerialName("bir_year")
    val birthdayYear: String = "2024",
    @SerialName("bir_month")
    val birthdayMonth: String = "1",
    @SerialName("bir_day")
    val birthdayDay: String = "26",
    @SerialName("local_province")
    val locationProvince: String = "北京",
    @SerialName("local_city")
    val locationCity: String = "东城",
    val qq: String = ""
) {

    fun toParameters() = Parameters.build {
        append("__HASH__", hash)
        append("birthday", birthday)
        append("nick", nick)
        append("sex", sex)
        append("bir_year", birthdayYear)
        append("bir_month", birthdayMonth)
        append("bir_day", birthdayDay)
        append("local_province", locationProvince)
        append("local_city", locationCity)
        append("qq", qq)
    }
}