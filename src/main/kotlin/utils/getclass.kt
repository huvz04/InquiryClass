package io.huvz.utils

import io.huvz.InquiryClass
import io.huvz.utils.classConfig.password
import io.huvz.utils.classConfig.username
import io.huvz.utils.classdata.classList
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import java.io.File
import java.nio.charset.Charset

object getclass  : SimpleCommand(InquiryClass,"getClass","获取课表",description = "获取全部的课表") {

    @Handler
    fun handlerlist(sender: CommandSender?, classname:String) {


    }
    fun test(classname:String) {
        //实例化




    }

}