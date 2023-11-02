package io.huvz.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object classdata {
    val classList = this::class.java.getResource("classList.json")

    fun getList(): JsonResult? {
        val bytes = classdata.classList?.readBytes();
        val jsonString = bytes?.toString(Charsets.UTF_8)
        return  jsonString?.let { Json.decodeFromString(it) }
    }
}



// 班级信息
@Serializable
data class ClassInfo(
    @SerialName("bj")
    val bj : String,
    @SerialName("zyh_id")
    val zyh_id : String,
    @SerialName("bh_id")
    val bh_id : String
)

// 解析的结果
@Serializable
data class JsonResult(
    val classes: List<ClassInfo>
)
@Serializable
data class MyClassList(
    @SerialName("kblx") var kblx :Long,
    @SerialName("sfxsd") var sfxsd  : String?="",
    @SerialName("kbList") var kbList :List<MyClass>  = listOf<MyClass>()
)

@Serializable
data class MyClass(
    //@SerialName("qtkcgs") var qtkcgs: String="",
    @SerialName("cd_id") var cdId : String="", // 场地ID
    @SerialName("cdmc")var cdmc : String="",  // 场地名称
    @SerialName("jc") var jc : String ="", // 节次
    @SerialName("jcs") var jcs: String="", // 节次数
    @SerialName("kcmc") var kcmc: String="", // 课程名称
    @SerialName("rsdzjs") var rsdzjs: Int = 0, // 任课教师数
    @SerialName("xf")  var xf: String="", // 学分
    @SerialName("xm")  var xm: String="", // 姓名
    @SerialName("xnm")  var xnm: String="", // 学年码
    @SerialName("xqj")  var xqj: String="", // 星期几
    @SerialName("xqjmc")  var xqjmc: String="", // 星期几名称
    @SerialName("year")  var year: String="", // 年份
    @SerialName("zcd")  var zcd: String="", // 周次段

)

object imaTime{
    fun calculateWeeks(startDate: LocalDate, endDate: LocalDate): Long {
        val days = ChronoUnit.DAYS.between(startDate, endDate)
        val remainingDays = days % 7

        return days / 7 + if (remainingDays > 0) 1 else 0
    }
    fun getcurrentWeek():Int{
        // 计算当前周次
        val currentDate = LocalDate.now()
        val startDate: LocalDate
        val endDate: LocalDate
        val currentYear: Int
        val currentSemesterNumber: Int
        // 根据当前日期确定学年和学期
        if (currentDate.monthValue >= 9) {
            //第一个学期从9月1日开始计算
            currentYear = currentDate.year
            currentSemesterNumber = 3
            startDate = LocalDate.of(currentYear, 9, 1)
            endDate = LocalDate.of(currentYear + 1, 2, 28)
        } else {
            //第二个学期就从2月10日开始计算到8月31
            currentYear = currentDate.year - 1
            currentSemesterNumber = 12
            startDate = LocalDate.of(currentYear, 2, 10)
            endDate = LocalDate.of(currentYear, 8, 31)
        }
        val week = calculateWeeks(startDate, currentDate).toInt()

        return week
    }
    fun getcurrentYear():Int{
        // 计算当前周次
        val currentDate = LocalDate.now()
        val startDate: LocalDate
        val endDate: LocalDate
        val currentYear: Int
        val currentSemesterNumber: Int
        // 根据当前日期确定学年和学期
        if (currentDate.monthValue >= 9) {
            //第一个学期从9月1日开始计算
            currentYear = currentDate.year
        } else {
            //第二个学期就从2月10日开始计算到8月31
            currentYear = currentDate.year - 1
        }

        return currentYear
    }
    fun getSemesterNumber():Int{
        // 计算当前周次
        val currentDate = LocalDate.now()
        val startDate: LocalDate
        val endDate: LocalDate
        val currentYear: Int
        val currentSemesterNumber: Int
        // 根据当前日期确定学年和学期
        if (currentDate.monthValue >= 9) {
            //第一个学期从9月1日开始计算
            currentYear = currentDate.year
            currentSemesterNumber = 3
            startDate = LocalDate.of(currentYear, 9, 1)
            endDate = LocalDate.of(currentYear + 1, 2, 28)
        } else {
            //第二个学期就从2月10日开始计算到8月31
            currentYear = currentDate.year - 1
            currentSemesterNumber = 12
            startDate = LocalDate.of(currentYear, 2, 10)
            endDate = LocalDate.of(currentYear, 8, 31)
        }
        val week = ChronoUnit.WEEKS.between(startDate, currentDate).toInt()

        return currentSemesterNumber
    }
}