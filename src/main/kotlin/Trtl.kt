import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.hypot
import kotlin.math.log
import kotlin.math.PI
import java.util.ArrayList

class V2(var x: Double, var y: Double) {
    fun plus(u: V2) {
        x += u.x
        y += u.y
    }
    
    fun scale(k: Double) {
        x *= k
        y *= k
    }
}

// simulating a turtle
class T(var x: Double, var y: Double, var m: Double, var a: Double) {
    var i = 0.0
    var step = 0.0
    var turn = 0.0
    
    fun sense(j: Double) {
        i = j
        turn  = (PI / 8.0) * i / 450.0
        step = (PI / 8.0) * i / 450.0
    }
    
    fun live(d: Drawer) {
        fd(d, step)
        lt(turn)
    }
    
    fun rndr(d: Drawer) {
        val x2 = x + m * cos(a)
        val y2 = y - m * sin(a)
        d.lineSegment(x, y, x2, y2)
    }
    
    fun fd(d: Drawer, r: Double) {
        x = x + r*cos(a)
        y = y - r*sin(a)
    }
    
    fun bk(d: Drawer, r: Double) {
        fd(d, -r)
    }
    
    fun lt(phi: Double) {
        a += phi
    }
    
    fun rt(phi: Double) {
        lt(-phi)
    }
}

class I(var x: Double, var y: Double) {
    fun feed(t: T) {
        val h = hypot(x - t.x, y - t.y)
        t.sense(0.064 * h * h)
    }
}

class TS(var x: Double, var y: Double, var m: Double, val n: Int) {
    val sys: ArrayList<T>
    
    init {
        sys = ArrayList<T>()
        for (i in -n/4..n/4) {
            sys.add(T(x + i * 2, y, m, 0.0))
        }
        for (i in -n/4..n/4) {
            sys.add(T(x, y + i * 2, m, 0.0))
        }
    }
    
    fun pulse(d: Drawer, ego: I) {
        for (t in sys) {
            t.rndr(d)
            t.live(d)
            ego.feed(t)
        }
    }
}

