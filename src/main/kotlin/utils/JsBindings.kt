package io.huvz.utils

import kotlinx.coroutines.delay
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Source
import java.io.File

fun  main(){
    test("计算机213")
}
fun test(classname:String){
    //实例化
    var jhc = JhcInquiry(classConfig.username, classConfig.password)
    var j1: JsonObject?= null;
    val bytes = classdata.classList?.readBytes()
    var resultmsg: ClassInfo? =null
    var secondmsg: ClassInfo? =null;
    val jsonString = bytes?.toString(Charsets.UTF_8)
    val jsonResult: JsonResult? = jsonString?.let { Json.decodeFromString(it) }
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
        njid  = resultmsg.bh_id.substring(0,4)
    }
    else if(secondmsg!=null){
         njid = secondmsg.bh_id.substring(0,4)
        resultmsg = secondmsg;
    }
    else{
        resultmsg = ClassInfo( "计算机211", "0155","2021015501")
    }
    jhc.initial {
        success ->
        if(success){
                jhc.queryCourse(njid,resultmsg.zyh_id,resultmsg.bh_id)
                { result ->
                    // 处理查询结果
                    if (result != null) {
                        // 查询成功，处理返回的 JSON 对象

                        println("========查询接口=========");
                        println(result)
                        println("查询参数njid:${njid},zyh_id:${resultmsg.zyh_id},bh_id:${resultmsg.bh_id}")
                    } else {
                        // 查询失败
                        println("Failed to query course.")
                    }
                }


        }else{
            println("Login Fail")
        }

    }
}
object JsBindings {



    fun encryptPwd(password:String, exponent:String?, modulus:String?):String{
        val context = Context.newBuilder("js")
            .build()
        val jsSourceCode = """
        console.log("正在启动rsa加密中…………JS引擎正常");
    """.trimIndent()
        context.eval("js", jsSourceCode)

        val rsaFile = File("E:\\Project\\InquiryClass\\src\\main\\kotlin\\node\\utils\\rsa.js")
        val rsaSource = Source.newBuilder("js", rsaFile).build()
        context.eval(rsaSource)
        // 获取 JavaScript 函数并调用
        val greetFunction = context.getBindings("js").getMember("encryptPwd")
        val result1 = greetFunction.execute(password,exponent,modulus).asString()
        //println("调用函数的结果为:$result1")
        // 关闭上下文
        context.close()
        return result1;
    }

}