package ui.anwesome.com.bottommenuview

/**
 * Created by anweshmishra on 21/12/17.
 */
import android.content.*
import android.graphics.*
import android.view.*
class BottomMenuView(ctx:Context):View(ctx) {
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class BottomMenu(var w:Float,var h:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.color = Color.parseColor("#673AB7")
            canvas.drawRect(RectF(0f,0f,h-0.9f*h*scale,w),paint)
        }
    }
    data class BottomMenuCircle(var x:Float,var y:Float,var r:Float) {
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            canvas.save()
            canvas.translate(x,y)
            paint.color = Color.GRAY
            canvas.drawCircle(0f,0f,r,paint)
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(i*90f*(1-scale))
                paint.strokeWidth = r/5
                paint.color = Color.WHITE
                paint.strokeCap = Paint.Cap.ROUND
                canvas.drawLine(-r/2,0f,r/2,0f,paint)
                canvas.restore()
            }
            canvas.restore()
        }
    }
}