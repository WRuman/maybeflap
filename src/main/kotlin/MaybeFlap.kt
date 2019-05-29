import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*
import kotlin.math.*

const val MS_PER_FRAME = 1000 / 60
const val PIXELS_PER_FRAME = 1
const val HORIZ = 640
const val VERT = 480

fun main() {
    SwingUtilities.invokeLater { createAndShow() }
}

fun createAndShow() {
    println("Running at $MS_PER_FRAME milliseconds per frame")
    with(JFrame("MaybeFlap")) {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        minimumSize = Dimension(HORIZ, VERT)
        setSize(HORIZ, VERT + 30)
        contentPane.add(Field, BorderLayout.CENTER)
        setLocationRelativeTo(null) // Places window at center of screen
        isVisible = true
    }
    Timer(MS_PER_FRAME, Field).start()
}

object Field: JPanel(), ActionListener {
    override fun actionPerformed(eve: ActionEvent?) {
        this.repaint()
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(HORIZ, VERT)
    }

    override fun paintBorder(ctx: Graphics?) {
        super.paintBorder(ctx)
        with(ctx as Graphics2D) {
            color = Color.BLUE
            drawLine(0, 0, HORIZ, 0)
            drawLine(0, 0, 0, VERT)
            drawLine(HORIZ, 0, HORIZ, VERT)
            drawLine(0, VERT, HORIZ, VERT)
        }
    }

    var lastTime = System.currentTimeMillis()
    var ticks = 0L
    var xSign = 1
    var ySign = 1
    var bob = Rectangle(0, 0, 25, 25)

    override fun paintComponent(ctx: Graphics?) {
        super.paintComponent(ctx)
        with(ctx as Graphics2D) {
            color = Color.GREEN
            //clearRect(bob.x, bob.y, bob.x + bob.width, bob.y + bob.height)
            //updateBob()
            sinBob()
            fillRect(bob.x, bob.y, bob.width, bob.height)
            drawString("X=${bob.x}, Y=${bob.y}", bob.x, bob.y)
            toolkit.sync()
        }
    }

    private fun sinBob() {
        with(bob) {
            val ts = System.currentTimeMillis()
            val diff = ts - lastTime
            lastTime = ts
            ticks += min(diff, 5L)
            val pct = sin(ticks * .01)
            val fin = ((VERT - height) / (1.0 - -1.0)) * (pct - -1.0)
            //println(fin)
            y = fin.roundToInt()
        }
    }

    private fun updateBob() {
        with(bob) {
            val ts = System.currentTimeMillis()
            val delta = ts - lastTime
            lastTime = ts
            val pctFrameBudget = ((delta * 1.0 / MS_PER_FRAME) * PIXELS_PER_FRAME)
            x = (x + pctFrameBudget * xSign).roundToInt()
            y = (y + pctFrameBudget * ySign).roundToInt()
            if (x + width >= HORIZ) {
                xSign = -1
                x = HORIZ - width
            }
            if(x <= 0) {
                xSign = 1
                x = 0
            }
            if (y + height >= VERT) {
                ySign = -1
                y = VERT - height
            }
            if(y <= 0) {
                ySign = 1
                y = 0
            }
        }
    }

}