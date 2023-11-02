
import io.huvz.utils.MyClass
import org.jetbrains.skia.*
import org.jetbrains.skiko.SkiaLayer
import org.jetbrains.skiko.SkikoView
import javax.swing.JFrame
import javax.swing.SwingUtilities




fun main() {
    /**
     * 暂时搁置 TODO
     */
    val timetable = getSampleTimetable()

    val skiaLayer = SkiaLayer()
    skiaLayer.skikoView = object : SkikoView {
        override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
            drawTimetable(timetable, canvas)
        }
    }

    SwingUtilities.invokeLater {
        val frame = JFrame("课表")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane.add(skiaLayer)
        frame.setSize(1200, 800)
        frame.isVisible = true
    }
}

fun getSampleTimetable(): List<MyClass> {
    return listOf(
        MyClass(
            cdId = "01391",
            cdmc = "实验实训室",
            jc = "1-4节",
            jcs = "1-4",
            kcmc = "计算机视觉技术应用（OpenCV）",
            rsdzjs = 0,
            xf = "3.0",
            xm = "黄日辰",
            xnm = "2023",
            xqj = "3",
            xqjmc = "星期三",
            year = "49",
            zcd = "无",
        )
    )
}
fun drawTimetable(myClasses: List<MyClass>, canvas: Canvas) {
    val paint = Paint()
    paint.color = Color.BLACK

    val skFont = Font(Typeface.makeFromName("微软雅黑",FontStyle.NORMAL),16f)
    val textPaint = Paint().apply {
        color = Color.BLACK
    }

    val tableWidth = 7*150
    val firstWidth = 75
    val tableHeight = 14*70
    val cellWidth = tableWidth / 7
    val cellHeight = tableHeight / 14
    val startX = 10
    val startY = 10

// 绘制表格线
    for (i in 0..14) {
        val y = startY + i * cellHeight
        canvas.drawLine(startX.toFloat(), y.toFloat(), (startX + tableWidth).toFloat(), y.toFloat(), paint)
    }
    for (i in 0..8) {
        val x = startX + (if (i == 0) firstWidth else (i - 1) * cellWidth)
        canvas.drawLine(x.toFloat(), startY.toFloat(), x.toFloat(), (startY + tableHeight).toFloat(), paint)
    }


    // 绘制课程方块和课程名称
    for (myClass in myClasses) {
        val xqj = myClass.xqj.toInt()
        val jc = myClass.jc.replace("节", "").split("-").map { it.toInt() }
        val left = startX + (xqj - 1) * cellWidth
        val top = startY + (jc.first() - 1) * cellHeight
        val right = left + cellWidth
        val bottom = top + (jc.last() - jc.first() + 1) * cellHeight

        // 绘制课程方块
        val backpaint = Paint()
        backpaint.color = Color.makeRGB(30, 144, 255)
        canvas.drawRect(Rect.makeLTRB(left.toFloat() + 1, top.toFloat() + 1, right.toFloat() - 1, bottom.toFloat() - 1), backpaint)

        // 绘制课程名称
        val text = myClass.kcmc
        val textX = left + cellWidth / 2
        val textY = top + cellHeight / 2
        val textPaint = Paint()
        val textFont1 = Font(Typeface.makeFromName("微软雅黑",FontStyle.NORMAL),16f)
        textPaint.color = Color.BLACK
        textPaint.isAntiAlias = true

        val textBounds = textFont1.measureText(text)
        val textWidth = textBounds.width
        val textHeight = textBounds.height
        val textLeft = textX - textWidth / 2
        val textTop = textY + textHeight / 2
        canvas.drawString(text, textLeft, textTop, textFont1,textPaint)
    }


}