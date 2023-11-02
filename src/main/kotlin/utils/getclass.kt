package io.huvz.utils

import io.huvz.InquiryClass
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object getclass  : SimpleCommand(InquiryClass,"weekClass","本周课表",description = "获取全部的课表") {

    @Handler
    suspend fun handlerlist(sender: CommandSender, className: String) {
        var str = StringBuilder()
        try {
            // 执行异步操作，将结果保存在result中
            val result = suspendCoroutine<MyClassList?> { continuation ->
                getClass(className) { result ->
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
    private fun getClass(classname:String,CallBack : (MyClassList?) ->Unit){

        val json = Json {
            ignoreUnknownKeys = true // 添加忽略未知键的配置
        }

        //实例化
        var jhc = JhcInquiry(classConfig.username, classConfig.password)
        var j1: JsonObject?= null;

        var resultmsg: ClassInfo? =null
        var secondmsg: ClassInfo? =null;

        val jsonResult = classdata.getList()
        if(classname.length<=3) {
            CallBack(null);
        }
        val clas2 = classname.substring(0,2);
        if(jsonResult!=null){
            for( i  in jsonResult.classes)
            {
                if(classname.contains(i.bj))   resultmsg = i
                if(i.bj.startsWith(clas2)) secondmsg = i
            }
        }

        var njid = "2021"

        if(resultmsg!=null)
        {
            val resultid = extractFirstTwoDigits(resultmsg.bj)
            njid  = "20"+resultid
        }
        else if(secondmsg!=null){
            val resultid = extractFirstTwoDigits(secondmsg.bj)
            njid  = "20"+resultid
            resultmsg = secondmsg;
        }
        else{
//            val resultid = extractFirstTwoDigits("计算机221")
//            njid  = "20"+resultid
//            resultmsg= ClassInfo("计算机221","0155","E0741C9F9B317ECBE053D30F080A0B6F")
            CallBack(null)
        }

        jhc.initial { success ->
            if (success) {
                resultmsg?.let {
                    jhc.queryCourse(imaTime.getcurrentWeek().toString(), njid, it.zyh_id, it.bh_id)
                    { result ->
                        // 处理查询结果
                        if (result != null) {
                            // 查询成功，处理返回的 JSON 对象
                            val myclass: MyClassList = json.decodeFromString<MyClassList>(result)
                            CallBack(myclass)
                            return@queryCourse
                        } else {
                            // 查询失败
                            println("Failed to query course.")
                        }
                    }
                }


            } else {
                println("Login Fail")
            }

        }
    }
}


