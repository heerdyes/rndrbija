import org.openrndr.KEY_ESCAPE
import org.openrndr.color.ColorRGBa
import org.openrndr.application
import org.openrndr.draw.loadFont
import org.openrndr.draw.Drawer
import org.openrndr.ffmpeg.ScreenRecorder
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

fun main() = application {
    configure {
        width = 900
        height = 900
    }

    program {
        val recorder = ScreenRecorder().apply {
            outputToVideo = false
        }
        val cx = width / 2.0
        val cy = height / 2.0
        val fg = ColorRGBa(.0, 1.0, 1.0)
        val bg = ColorRGBa(.0, .0, .0)
        val m = I(cx, cy)
        val ts = TS(cx, cy, 1.5, 840)
        
        extend(recorder)
        extend {
            drawer.stroke = fg
            drawer.fill = bg
            ts.pulse(drawer, m)
        }

        keyboard.keyDown.listen {
            when {
                it.key == KEY_ESCAPE -> program.application.exit()
                it.name == "r" -> {
                    recorder.outputToVideo = !recorder.outputToVideo
                    println(if (recorder.outputToVideo) "REC" else "PAUSED")
                }
            }
        }
    }
}

fun ga(d: Drawer, x: Double, y: Double, m: Double, n: Int, k: Double, j: Double, t: Double) {
    d.circle(x, y, m*sin(t))
    if (n > 0) {
        ga(d, x-k, y-j, m, n-1, .75*k, .75*j, t)
        ga(d, x+k, y-j, m, n-1, .75*k, .75*j, t)
    }
}

