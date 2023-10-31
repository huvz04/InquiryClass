package io.huvz.utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.*
import org.jsoup.Jsoup
import java.util.*

fun main() {
    RzUtil().initial()
}

class RzUtil {
    private val UserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36"
    val cookieManager = CookieManager()
    private val client = OkHttpClient.Builder()
        .followRedirects(true)
        .cookieJar(cookieManager)
        .build()

    fun initial() {
        println("----------------initial----------------")
        val requestUrl = "https://rz.jhc.cn/zfca/login"

        val request = Request.Builder()
            .url(requestUrl)
            .header("User-Agent", UserAgent)
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string()
            val document = responseBody?.let { Jsoup.parse(it) }
            val execution = document?.select("input[name=execution]")?.attr("value")
            execution?.let { getPubKey(it) }
        }
    }

    fun getPubKey(execution: String) {
        println("----------------getPubKey----------------")
        val requestUrl = "https://rz.jhc.cn/zfca/v2/getPubKey"

        val request = Request.Builder()
            .url(requestUrl)
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string()
            println(responseBody)
            val json = responseBody?.let { Json.decodeFromString<JsonObject>(it) }
            val modulus = json?.get("modulus")?.jsonPrimitive?.content ?: ""
            val exponent = json?.get("exponent")?.jsonPrimitive?.content ?: ""
            login(execution, modulus, exponent)
        }
    }

    fun login(execution: String, modulus: String, exponent: String) {
        println("----------------login----------------")
        val requestUrl = "https://rz.jhc.cn/zfca/login"
        val pwd = ""
            //encryptedString( modulus ,exponent,classConfig.password);
        val requestBody = FormBody.Builder()
                .add("_eventId", "submit")
                .add("authcode", "")
                .add("execution", execution)
                .add("password", pwd )
                .add("username", classConfig.username)
                .build()


        val request = Request.Builder()
            .url(requestUrl)
            .header("User-Agent", UserAgent)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                // 打印保存的 Cookies
                cookieManager.printCookies()
                println("----------------登陆成功----------------")
                println(responseBody)
            } else {
                println("An error occurred during login. Status code: ${response.code}")
            }
        }
    }
}

/**
 * Cookie管理
 */
class CookieManager : CookieJar {
    private val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host]?.addAll(cookies) ?: cookieStore.put(url.host, cookies.toMutableList())
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }

    fun printCookies() {
        for ((host, cookies) in cookieStore) {
            println("Host: $host")
            for (cookie in cookies) {
                println("Cookie: ${cookie.name}=${cookie.value}")
            }
        }
    }
}