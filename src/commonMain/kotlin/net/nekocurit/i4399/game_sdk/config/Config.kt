package net.nekocurit.i4399.game_sdk.config

data class Config(
    val gameKey: String,
    val gameVersion: String,
    val bid: String
) {
    fun generateDeviceRaw(device: String) = """{"GAME_KEY":"$gameKey","GAME_VERSION":"$gameVersion","BID":"$bid","DEVICE_IDENTIFIER":"$device"}"""
}