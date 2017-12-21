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
}