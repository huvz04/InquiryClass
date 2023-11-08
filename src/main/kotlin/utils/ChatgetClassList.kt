package io.huvz.utils

import io.huvz.InquiryClass
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand

object ChatgetClassList: SimpleCommand(InquiryClass,"getClassList","班级列表",description = "获取全部的课表")  {
    @Handler
    suspend fun handlerlist(sender: CommandSender) {
        val jsonResult = classdata.getList()
        var listString : List<String> = listOf()
        if(jsonResult!=null)
            listString = jsonResult.classes.stream().map { it.bj }.toList()
        var msg = "找不到列表";if(listString.isNotEmpty())msg= listString.toString()
        sender.subject?.sendMessage(msg)
    }


}