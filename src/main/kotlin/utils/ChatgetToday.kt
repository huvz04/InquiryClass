package io.huvz.utils

import io.huvz.InquiryClass
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object ChatgetToday: SimpleCommand(InquiryClass,"getClassList","今日课表",description = "获取全部的课表")  {
    @Handler
    suspend fun handlerlist(sender: CommandSender, className: String) {
        var str = StringBuilder()
        try {
            val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            val namelist = arrayOf("一","二","三","四","五","六","日")
            // 执行异步操作，将结果保存在result中
            val result = suspendCoroutine<MyClassList?> { continuation ->
                JHclient.getClassLesson(className) { result ->
                    if (result != null) {
                        val adjustedToday = (today - 2 + 7) % 7 + 1 // 将周日调整为7
                        str.append("现在是第${imaTime.getcurrentWeek()}周的星期${namelist[adjustedToday - 1]}\n查询到${className}的课表:\n")
                        val resultMap: Map<Long, List<MyClass>> = result.kbList.groupBy { it.xqj.toLong() }
                        if (resultMap[adjustedToday.toLong()] != null) {
                            str.append("${resultMap[adjustedToday.toLong()]?.get(0)?.xqjmc}:\n")
                            for (it1 in resultMap[adjustedToday.toLong()]!!) {
                                str.append("${it1.kcmc}——${it1.xm}\n${it1.zcd}(${it1.jc});\n")
                            }
                        } else {
                            str.append("今天没有课程安排！")
                        }
                    } else {
                        str.append("没有找到这个班级哦！请尝试 计算机211；电信（本）212")
                    }
                    continuation.resume(result)
                }
            }
        } catch (e: Exception) {
            str.append("发生错误：${e.message}")
        }

        coroutineScope {
            launch {
                sender.subject?.sendMessage(str.toString())
            }
        }
    }
}