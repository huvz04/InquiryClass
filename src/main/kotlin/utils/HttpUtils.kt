import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json



data class MyData(val value: Int)
class HttpUtils {
    var baseUrl = "https://www.baidu.com"
    val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
            connectTimeoutMillis = 5000
        }
        install(DefaultRequest) {
            url { baseUrl }
        }
    }

    fun close() {
        httpClient.close()
    }

    inline fun <reified T> get(url: String, params: Map<String, String> = emptyMap()): Flow<T> {
        return flow {
            val response = httpClient.get(url) {
                params.forEach { parameter(it.key, it.value) }
            }
            val result = response.body<T>()
            emit(result)
        }.catch { throwable: Throwable ->
            throw throwable
        }.onCompletion { cause ->
            close()
        }.flowOn(Dispatchers.IO)
    }


    inline fun <reified T> post(url: String, params: Map<String, String> = emptyMap()): Flow<T> {
        return flow {
            val response = httpClient.post(url) {
                params.forEach { parameter(it.key, it.value) }
            }
            val result = response.body<T>()
            emit(result)
        }.catch { throwable: Throwable ->
            throw throwable
        }.onCompletion { cause ->
            close()
        }.flowOn(Dispatchers.IO)
    }
    inline fun <reified T> get(url: String, crossinline params:()-> Map<String, String>): Flow<T> {
        return flow {
            val queryParams = params()
            val response = httpClient.get(url) {
                queryParams.forEach { parameter(it.key, it.value) }
            }
            val result = response.body<T>()
            emit(result)
        }.catch { throwable: Throwable ->
            throw throwable
        }.onCompletion { cause ->
            close()
        }.flowOn(Dispatchers.IO)
    }

}
