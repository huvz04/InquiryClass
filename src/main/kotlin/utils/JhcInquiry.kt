package io.huvz.utils

import io.huvz.utils.JsBindings.encryptPwd
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException
import java.net.CookiePolicy
import java.net.HttpCookie

class JhcInquiry(private val username: String, private val password: String, private val options: Map<String, Any> = emptyMap()) {

    val UserAgent =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0";

    private val xnm = "2023"
    private val xqm = "3"
    private var execution: String? = null
    private var modulus: String? = null
    private var exponent: String? = null
    private var loginStatus = false
    var cookieg1 = Cookiejar1()
    val client = OkHttpClient.Builder()
        .cookieJar(cookieg1)
        .followRedirects(true)
        .build()
    init {

        initial()
    }

    private fun initial() {
        println("----------------initial----------------")

        val request = Request.Builder()

            .url("https://rz.jhc.cn/zfca/login?service=https://jwglxt.jhc.cn/sso/zfiotlogin")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("An error occurred in initial because: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val body = response.body?.string()
                    body?.let {
                        val doc = Jsoup.parse(it)
                        execution = doc.select("input[name=execution]").attr("value")
                        getPubKey()
                    }
                } catch (e: Exception) {
                    println("An error occurred in initial because: $e")
                }
            }
        })
    }

    private fun getPubKey() {
        println("----------------getPubKey----------------")
        val request = Request.Builder()
            .url("https://rz.jhc.cn/zfca/v2/getPubKey")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("An error occurred in getPubKey because: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val body = response.body?.string()
                    body.let {
                        val json = it?.let { it1 -> Json.decodeFromString<JsonObject>(it1) }
                        modulus = json?.get("modulus")?.jsonPrimitive?.content ?: ""
                        exponent = json?.get("exponent")?.jsonPrimitive?.content ?: ""
                        cookieg1.printCookies()
                        login()
                    }
                } catch (e: Exception) {
                    println("An error occurred in getPubKey because: $e")
                }
            }
        })
    }

    private fun login() {
        println("----------------login----------------")
        val formBody = execution?.let {
            FormBody.Builder()

                .add("username", username)
                .add("password", encryptPwd(password, exponent, modulus))
                .add("execution", it)
                .add("_eventId", "submit")
                .add("authcode", "")
                .build()


        }
        val request = formBody?.let {
            Request.Builder()
                .url("https://rz.jhc.cn/zfca/login")
                .post(it)
                .header("User-Agent", UserAgent)
                .build()
        }

        request?.let {
            client.newCall(it).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("An error occurred in login because: $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string()
                        loginStatus = true
                        cookieg1.printCookies()
                        println(body)
                    } catch (e: Exception) {
                        println("An error occurred in login because: $e")
                    }
                }
            })
        }

    }
}

class Cookiejar1 : CookieJar {
    private val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        println("=============新的Cookies入栈=============")
        cookieStore[url.host]?.addAll(cookies) ?: cookieStore.put(url.host, cookies.toMutableList())
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        println("=============新的Cookies加载=============")
        printCookies()
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