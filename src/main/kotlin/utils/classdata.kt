package io.huvz.utils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.decodeFromStream

object classdata {
    val classList = this::class.java.getResource("classList.json")
}
// 班级信息
@Serializable
data class ClassInfo(
    @SerialName("bj")
    val bj: String,
    @SerialName("zyh_id")
    val zyh_id: String,
    @SerialName("bh_id")
    val bh_id: String
)

// 解析的结果
@Serializable
data class JsonResult(
    val classes: List<ClassInfo>
)