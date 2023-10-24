package io.huvz.node
// 导入依赖
import io.ktor.client.*
import io.ktor.client.request.*
import org.jsoup.Jsoup
import kotlinx.serialization.json.Json
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
class app {


    // 函数实现
    fun encryptPwd(password: String, exponent: String, modulus: String): String {
        // 加密逻辑
        return "加密逻辑"
    }

    fun getNum(className: String): String {
        // 获取编号逻辑
        return "编号逻辑"
    }

    // 主类
    class JhcInquiry(val username: String, val password: String, options: Map<String, Any> = mapOf()) {

        var xnm = "2023"
        var xqm = "3"

        init {
            // 初始化属性
        }

        // 初始化方法
        suspend fun initial() {
            val response = client.get("https://rz.jhc.cn/zfca/login?service=https://jwglxt.jhc.cn/sso/zfiotlogin")
            val doc = Jsoup.parse(response.body())
            execution = doc.select("input[name=execution]").attr("value")
            getPubKey()
        }

        // 获取公钥
        suspend fun getPubKey() {
            val response = client.get("https://rz.jhc.cn/zfca/v2/getPubKey")
            val json = Json.parseToJsonElement(response.body()).jsonObject
            modulus = json["modulus"].toString()
            exponent = json["exponent"].toString()
            login()
        }

        // 登录
        suspend fun login() {
            val formData = mapOf(
                "username" to username,
                "password" to encryptPwd(password, exponent, modulus),
                "execution" to execution
            )
            client.submitForm("https://rz.jhc.cn/zfca/login", formData)
        }

        // 查询课程
        suspend fun queryCourse(className: String) = coroutineScope {

            val term = async {
                // 获取学期
                getNum(className).slice(0..1)
            }

            val classInfo = async {
                // 获取班级信息
            }

            val response = client.submitForm(url, formData)
            Json.parseToJsonElement(response)
        }

    }
}