package io.huvz.utils

import io.huvz.utils.JsBindings.encryptPwd
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException

class JhcInquiry(private val username: String, private val password: String) {





    // 解析
    val UserAgent =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0";
    private val xnm = imaTime.getcurrentYear().toString()
    private val xqm = imaTime.getSemesterNumber().toString()
    private var execution: String? = null
    private var modulus: String? = null
    private var exponent: String? = null
    private var loginStatus = false
    var cookieg1 = Cookiejar1()
    val client = OkHttpClient.Builder()
        .cookieJar(cookieg1)
        .followRedirects(true)
        .build()

    //需要手动实例化才会登录
    fun initial(onInitializationComplete: (Boolean) -> Unit) {
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
                        getPubKey(){
                            onInitializationComplete(true)
                        }
                    }
                } catch (e: Exception) {
                    println("An error occurred in initial because: $e")
                }
            }
        })
    }

    private fun getPubKey(callback: (Boolean) -> Unit) {
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
                        login(){
                            callback(true)
                        }

                    }
                } catch (e: Exception) {
                    println("An error occurred in getPubKey because: $e")
                }
            }
        })
    }
    private fun login(callback: (Boolean) -> Unit) {
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
                        callback(true)
                    } catch (e: Exception) {
                        println("An error occurred in login because: $e")
                    }
                }
            })
        }

    }

     fun queryCourse(zs:String,njdmid: String,zyhid: String,bhid: String, callback: (String?) -> Unit) {



         var result : String? = null;
            val formBody = FormBody.Builder()
                .add("zs",zs)
                .add("xnm", xnm)
                .add("xqm", xqm)
                .add("njdm_id", njdmid)
                .add("zyh_id", zyhid)
                .add("bh_id", bhid)
                .add("tjkbzdm", "1")
                .add("tjkbzxsdm", "0")
                .build()

         val request = Request.Builder()
            .url("https://jwglxt.jhc.cn/jwglxt/kbdy/bjkbdy_cxBjKb.html?gnmkdm=N214505")
            .post(formBody)
            .build()

         client.newCall(request)
             .enqueue(object : Callback {
             override fun onFailure(call: Call, e: IOException)
             {
                 //initial()
                 println("An error occurred in queryCourse because: $e")
             }
             override fun onResponse(call: Call, response: Response) {
                 try {
                     val body = response.body?.string()
                     if (body != null) {

                         result = body
                     };
                     callback(result)
                 } catch (e: Exception) {
                     //initial()
                     println("An error occurred in queryCourse because: $e")
                }
            }
        })
 }

}

class Cookiejar1 : CookieJar {
    private val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host]?.addAll(cookies) ?: cookieStore.put(url.host, cookies.toMutableList())
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        //printCookies()
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