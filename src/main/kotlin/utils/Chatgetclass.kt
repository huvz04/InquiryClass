package io.huvz.utils

import io.huvz.InquiryClass
import io.huvz.utils.JHclient.Companion.getClassLesson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object Chatgetclass  : SimpleCommand(InquiryClass,"weekClass","本周课表",description = "获取全部的课表") {

    @Handler
    suspend fun handlerlist(sender: CommandSender, className: String) {
        var str = StringBuilder()
        try {
            // 执行异步操作，将结果保存在result中
            val result = suspendCoroutine<MyClassList?> { continuation ->
                getClassLesson(className) { result ->
                    //continuation.resume(result)
                    if (result != null) {
                        str.append("现在是第${imaTime.getcurrentWeek()}周\n查询到${className}的课表:\n")
                        val resultMap: Map<Long, List<MyClass>> = result.kbList.groupBy { it.xqj.toLong() }
                        for (i in 0L..7L)
                            if (resultMap[i] != null)
                                if (resultMap[i]?.get(0) != null) {
                                    str.append("${resultMap[i]?.get(0)?.xqjmc}:\n")
                                    for (it1 in resultMap[i]!!) {
                                        str.append("${it1.kcmc}——${it1.xm}\n${it1.zcd}(${it1.jc});\n")
                                    }
                                }
                    }
                    else{
                        str.append("没有找到这个班级哦！请尝试 计算机211；电信（本）212")
                    }
                    continuation.resume(result)
                }
            }

        } catch (e: Exception) {
            str.append("发生错误：${e.message}")
        }

        //println(str)

        //在getClass初始化完毕后调用函数
        coroutineScope {
            launch {
                sender.subject?.sendMessage(str.toString())
            }
        }
    }

    fun extractFirstTwoDigits(input: String): String? {
        val regex = Regex("\\d+")
        val matchResult = regex.find(input)
        return matchResult?.value?.take(2)
    }

}


