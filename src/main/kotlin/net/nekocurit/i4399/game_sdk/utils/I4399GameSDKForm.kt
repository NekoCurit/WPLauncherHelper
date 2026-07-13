package net.nekocurit.i4399.game_sdk.utils

import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import net.nekocurit.i4399.game_sdk.I4399GameSDKAPI
import net.nekocurit.i4399.x19.data.Request4399X19Authorize
import kotlin.collections.component1
import kotlin.collections.component2


typealias I4399GameSDKForm = MutableMap<String, String>

val REGEX = Regex("""<input(?=[^>]*type="hidden")(?=[^>]*name="([0-9a-zA-Z_]+)")(?=[^>]*value="([^"]*)")[^>]*>""")

suspend fun I4399GameSDKAPI.requestForms(action: Request4399X19Authorize.Action) = client.submitForm(
    url = "/oauth2/authorize.do?channel=&sdk=op",
    formParameters = Request4399X19Authorize(
        session = requireNotNull(session) { "会话信息尚未初始化(session = null)" },
        action = action
    ).toParameters()
).decodeForms()

suspend fun HttpResponse.decodeForms(
    substringBefore: String = """<label for="protocol">我已同意</label>"""
): I4399GameSDKForm = bodyAsText()
    .substringBefore(substringBefore)
    .let { REGEX.findAll(it) }
    .associate { it.groupValues[1] to it.groupValues[2] }
    .toMutableMap()

fun I4399GameSDKForm.toParameters() = Parameters.build { forEach { (key, value) -> append(key, value) } }
