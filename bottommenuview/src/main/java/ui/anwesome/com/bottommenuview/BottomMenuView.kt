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
    data class BottomMenuCircle(var x:Float,var h:Float,var r:Float,var y:Float = 0.85f*h) {
        fun draw(canvas: Canvas, paint: Paint, scale: Float) {
            y = 0.85f*h - 0.8f*h*scale
            canvas.save()
            canvas.translate(x, y)
            paint.color = Color.GRAY
            canvas.drawCircle(0f, 0f, r, paint)
            for (i in 0..1) {
                canvas.save()
                canvas.rotate(i * 90f * (1 - scale))
                paint.strokeWidth = r / 5
                paint.color = Color.WHITE
                paint.strokeCap = Paint.Cap.ROUND
                canvas.drawLine(-r / 2, 0f, r / 2, 0f, paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun handleTap(x:Float,y:Float):Boolean = x>=this.x-r && x<=this.x+r && y>=this.y - r && y<=this.y+r
    }
    data class BottomMenuContiner(var w:Float,var h:Float) {
        var bottomMenuCircle = BottomMenuCircle(w/10,h,h/20)
        var bottomMenu = BottomMenu(w,h)
        fun draw(canvas:Canvas,paint:Paint) {
            bottomMenu.draw(canvas,paint,1f)
            bottomMenuCircle.draw(canvas,paint,1f)
        }
        fun update(stopcb:(Float)->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
    data class BottomMenuState(var scale:Float = 0f,var dir:Float = 0f,var prevScale:Float = 0f) {
        fun update(stopcb:(Float)->Unit) {
            scale += 0.1f*dir
            if(Math.abs(scale - prevScale) > 1) {
                scale = prevScale+dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb:()->Unit) {
            dir = 1f-2*scale
            startcb()
        }
    }
}