package io.huvz.utils

import HttpUtils
import MyData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jsoup.Jsoup
import java.math.BigInteger
import java.security.KeyFactory
import java.security.spec.RSAKeyGenParameterSpec
import java.security.spec.RSAPublicKeySpec
import java.util.*
import javax.crypto.Cipher

val sessionHttpClient = HttpClient(OkHttp) {
    install(Logging) {
        level = LogLevel.ALL
    }
    install(HttpTimeout)
}

fun main() {
    runBlocking {
        RzUtil().initial()
    }
}
class RzUtil {
    private val UserAgent ="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0";
    suspend fun initial() {
        println("----------------initial----------------")
        val requestUrl = "https://rz.jhc.cn/zfca/login"

        val request = sessionHttpClient.get(requestUrl) {
            mapOf("service" to "https://jwglxt.jhc.cn/sso/zfiotlogin")
        }
        runBlocking {
            try {
                val responseBody = request.body<String>()
                val document = Jsoup.parse(responseBody)
                val execution = document.select("input[name=execution]").attr("value")
                getPubKey(execution)
            } catch (e: Throwable) {
                println("【异常】: $e")
            }
        }
    }

    suspend fun getPubKey(execution: String) {
        println("----------------getPubKey----------------")
        val requestUrl = "https://rz.jhc.cn/zfca/v2/getPubKey"

        val request  = sessionHttpClient.get(requestUrl)

        runBlocking {
            try {
                val responseBody = request.body<String>()
                val json = Json.decodeFromString<JsonObject>(responseBody)
                val modulus = json["modulus"]?.jsonPrimitive?.content ?: ""
                val exponent = json["exponent"]?.jsonPrimitive?.content ?: ""
                login(execution, modulus, exponent)
            } catch (error: Throwable) {
                println("An error occurred in getPubKey because: $error")
            }
        }
    }

    suspend fun login(execution: String, modulus: String, exponent: String) {
        println("----------------login----------------")
        val requestUrl = "https://rz.jhc.cn/zfca/login"

//        val httpClient = HttpClient {
//            install(Logging) {
//                level = LogLevel.ALL
//            }
//            install(HttpTimeout)
//        }

        val formData = Parameters.build {
            append("_eventId", "submit")
            append("authcode", "")
            append("execution", execution)
            append("password", encryptPwd(classConfig.password, exponent, modulus))
            append("username", classConfig.username.toString())
        }

        val response = sessionHttpClient.submitForm(
            url = requestUrl,
            formParameters = formData
        ) {
            method = HttpMethod.Post
            header(HttpHeaders.UserAgent, UserAgent)
        }

        try {
            if (response.status.value == 200) {
                val responseBody = response.bodyAsText()
                println("----------------登陆成功----------------")
                //println(response.headers.get(""))
                println(response.bodyAsText());
            } else {
                println("An error occurred during login. Status code: $response")
            }
        } catch (error: Throwable) {
            println("An error occurred during login because: $error")
        }
//        finally {
//            sessionHttpClient.close()
//        }
    }

    fun encryptPwd(password: String, exponent: String, modulus: String): String {
        // 加密解密前先使用 BigInteger 进行十六进制转化
        val exponentHex = BigInteger(exponent, 16)
        val modulusHex = BigInteger(modulus, 16)

        // 检查公钥长度是否满足要求
        val keyLengthInBits = modulusHex.bitLength()
        if (keyLengthInBits < 512) {
            throw IllegalArgumentException("RSA keys must be at least 512 bits long")
        }

        val keyFactory = KeyFactory.getInstance("RSA")
        val publicSpec = RSAPublicKeySpec(modulusHex- BigInteger.ONE, exponentHex)

        val publicKey = keyFactory.generatePublic(publicSpec)

        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        val reversedPwd = password.reversed()
        val encryptedBytes = cipher.doFinal(reversedPwd.toByteArray())

        return Base64.getEncoder().encodeToString(encryptedBytes)
    }
}

