package io.huvz.utils

import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Source



object JsBindings {



    fun encryptPwd(password:String, exponent:String?, modulus:String?):String{
        val context = Context.newBuilder("js")
            .build()
        val jsSourceCode = """
        console.log("JsBuilder is Ok! the RSA will start!!");
    """.trimIndent()
        context.eval("js", jsSourceCode)

        val rsaFile = this::class.java.getResource("rsa.js")
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