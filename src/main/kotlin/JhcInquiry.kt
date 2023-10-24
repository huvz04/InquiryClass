package io.huvz.pojo
import org.graalvm.polyglot.*;


fun main(){
    val context = Context.create()
    val jsFile = "E:\\Project\\InquiryClass\\src\\main\\kotlin\\node\\test.js"

    // 通过 eval 方法执行 JavaScript 文件
    val result = context.eval("js", jsFile)

    // 处理 JavaScript 文件的返回结果
    println(result)
}
   class JhcInquiry (val name: String){
    }

