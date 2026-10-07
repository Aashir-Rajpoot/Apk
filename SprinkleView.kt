package com.example.sprinkle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class SprinkleView(ctx: Context) : View(ctx) {
    private class P(var x: Float, var y: Float, val vx: Float, val vy: Float,
                    val color: Int, var life: Float)

    private val parts = mutableListOf<P>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init { setBackgroundColor(Color.BLACK) }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (e.actionMasked == MotionEvent.ACTION_DOWN ||
            e.actionMasked == MotionEvent.ACTION_POINTER_DOWN) {
            val i = e.actionIndex
            repeat(40) {
                val a = Random.nextDouble(0.0, 6.28)
                val s = Random.nextFloat() * 12f + 3f
                parts += P(
                    e.getX(i), e.getY(i),
                    (cos(a) * s).toFloat(), (sin(a) * s).toFloat(),
                    Color.HSVToColor(floatArrayOf(Random.nextFloat() * 360f, 1f, 1f)),
                    1f
                )
            }
            invalidate()
        }
        return true
    }

    override fun onDraw(c: Canvas) {
        c.drawColor(Color.BLACK)
        val iter = parts.iterator()
        while (iter.hasNext()) {
            val p = iter.next()
            p.x += p.vx; p.y += p.vy; p.life -= 0.02f
            if (p.life <= 0f) { iter.remove(); continue }
            paint.color = p.color
            paint.alpha = (p.life * 255).toInt()
            c.drawCircle(p.x, p.y, 8f * p.life, paint)
        }
        if (parts.isNotEmpty()) postInvalidateOnAnimation()
    }
}
