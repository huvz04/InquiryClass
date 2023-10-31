package io.huvz

import io.huvz.utils.getclass
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object InquiryClass : KotlinPlugin(
    JvmPluginDescription(
        id = "io.huvz.InquiryClass",
        name = "InquiryClass",
        version = "0.1.0",
    ) {
        author("huvz")
        info("""一个获取本校课表的bot插件""")
    }
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
        getclass.test("计算机211")
    }
}