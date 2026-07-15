package net.nekocurit.i4399.data

data class I4399Profile(
    val username: String,
    val nick: String?,
    val ipLocation: String
) {
    companion object {
        val REGEX_USERNAME = Regex("""<td\s+class="label">用户名\s*:</td>\s*<td\s+class="input">(.+)</td>""")
        val REGEX_NICK = Regex("""<td\s+class="label">昵称\s*:</td>\s*<td\s+class="input">(.+)</td>""")
        val REGEX_IP_LOCATION = Regex("""<div style="display: inline;float: right;color: #237DAF;">IP属地：(.+)</div>""")
    }

    constructor(source: String): this(
        username = REGEX_USERNAME.find(source)!!.groupValues[1],
        nick = REGEX_NICK.find(source)?.groupValues[1],
        ipLocation = REGEX_IP_LOCATION.find(source)?.groupValues[1] ?: "未知",
    )
}