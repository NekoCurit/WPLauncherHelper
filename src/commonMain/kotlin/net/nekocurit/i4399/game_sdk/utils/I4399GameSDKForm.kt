package net.nekocurit.i4399.game_sdk.utils

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import kotlin.collections.component1
import kotlin.collections.component2


typealias I4399GameSDKForm = MutableMap<String, String>

val REGEX = Regex("""<input(?=[^>]*type="hidden")(?=[^>]*name="([0-9a-zA-Z_]+)")(?=[^>]*value="([^"]*)")[^>]*>""")

suspend fun HttpResponse.decodeForms(
    substringBefore: String = """<label for="protocol">我已同意</label>"""
): I4399GameSDKForm = bodyAsText().decodeForms(substringBefore)

fun String.decodeForms(
    substringBefore: String = """<label for="protocol">我已同意</label>"""
): I4399GameSDKForm = this
    //.substringBefore(substringBefore)
    .let { REGEX.findAll(it) }
    .associate { it.groupValues[1] to it.groupValues[2] }
    .toMutableMap()

fun I4399GameSDKForm.toParameters() = Parameters.build { forEach { (key, value) -> append(key, value) } }
