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
}