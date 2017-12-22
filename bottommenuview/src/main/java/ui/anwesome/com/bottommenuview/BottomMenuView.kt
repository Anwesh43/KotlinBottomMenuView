package ui.anwesome.com.bottommenuview

/**
 * Created by anweshmishra on 21/12/17.
 */
import android.app.Activity
import android.content.*
import android.graphics.*
import android.view.*
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class BottomMenuView(ctx:Context):View(ctx) {
    val renderer = BottomMenuRenderer(this)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val menus:LinkedList<MenuItem> = LinkedList()
    data class MenuItem(var text:String,var clickListener: () -> Unit)
    fun addToParent(activity: Activity) {
        activity.setContentView(this)
    }
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    fun addMenu(text: String,clickListener: () -> Unit) {
        menus.add(MenuItem(text,clickListener))
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap(event.x,event.y)
            }
        }
        return true
    }
    data class BottomMenuSlide(var w:Float,var h:Float) {
        val menus:ConcurrentLinkedQueue<BottomMenuItem> = ConcurrentLinkedQueue()
        val animatingMenus:ConcurrentLinkedQueue<BottomMenuItem> = ConcurrentLinkedQueue()
        var y = h/20
        fun addMenu(menu:String,clickListener: () -> Unit) {
            val bottomMenu = BottomMenuItem(menu,w/2,y,clickListener)
            y += h/20
        }
        fun draw(canvas:Canvas,paint:Paint,scale:Float) {
            paint.color = Color.parseColor("#673AB7")
            val y = h - 0.9f*h*scale
            canvas.save()
            canvas.translate(0f,y)
            canvas.drawRect(RectF(0f,0f,w,h-y),paint)
            paint.textSize = h/30
            menus.forEach {
                it.draw(canvas,paint)
            }
            canvas.restore()
        }
        fun update(stopcb: () -> Unit) {
            animatingMenus.forEach {
                animatingMenus.remove(it)
                if(animatingMenus.size == 0) {
                    stopcb()
                }
            }
        }
        fun handleTap(x:Float,y:Float,startcb: () -> Unit) {
            menus.forEach {
                if(it.handleTap({
                    animatingMenus.add(it)
                    if(animatingMenus.size == 0) {
                        startcb()
                    }
                },x,y)) {
                    return
                }
            }
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
    data class BottomMenuContainer(var w:Float,var h:Float) {
        val state = BottomMenuState()

        var bottomMenuCircle = BottomMenuCircle(w/10,h,h/20)
        var bottomMenu = BottomMenuSlide(w,h)
        fun addMenu(text:String,clickListener: () -> Unit) {
            bottomMenu.addMenu(text,clickListener)
        }
        fun draw(canvas:Canvas,paint:Paint) {
            bottomMenu.draw(canvas,paint,state.scale)
            bottomMenuCircle.draw(canvas,paint,state.scale)
            canvas.drawLineIndicator(w/5,h/20,0.7f*w,state.scale,paint)
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
            if(state.scale == 1f) {
                bottomMenu.update({
                    stopcb(1f)
                })
            }
        }
        fun startUpdating(x:Float,y:Float,startcb:()->Unit) {
            if(bottomMenuCircle.handleTap(x,y)) {
                state.startUpdating(startcb)
                return
            }
            if(state.scale == 1f) {
                bottomMenu.handleTap(x, y, startcb)
            }
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
    data class BottomMenuAnimator(var container:BottomMenuContainer,var view:BottomMenuView) {
        var animated = false
        fun addMenu(text:String,clickListener: () -> Unit) {
            container.addMenu(text, clickListener)
        }
        fun update() {
            if(animated) {
                container.update{ scale ->
                    animated = false
                }
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun startUpdating(x:Float,y:Float) {
            if(!animated) {
                container.startUpdating(x,y,{
                    animated = true
                    view.postInvalidate()
                })
            }
        }
        fun draw(canvas:Canvas,paint:Paint) {
            container.draw(canvas,paint)
        }
    }
    data class BottomMenuRenderer(var view:BottomMenuView,var time:Int = 0) {
        var animator:BottomMenuAnimator?=null
        fun addMenu(text:String,clickListener: () -> Unit) {
            animator?.addMenu(text,clickListener)
        }
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                animator = BottomMenuAnimator(BottomMenuContainer(w,h),view)
                view.menus.forEach { menuItem ->
                    animator?.addMenu(menuItem.text,menuItem.clickListener)
                }
            }
            canvas.drawColor(Color.parseColor("#212121"))
            animator?.draw(canvas,paint)
            animator?.update()
            time++
        }
        fun handleTap(x:Float,y:Float) {
            animator?.startUpdating(x,y)
        }
    }
    companion object {
        fun create(activity: Activity):BottomMenuView {
            val view = BottomMenuView(activity)
            return view
        }
    }
    data class BottomMenuItem(var text:String,var x:Float,var y:Float,var clickListener:()->Unit,var tw:Float = 0f,var th:Float = 0f) {
        var itemClickListener:BottomMenuClickListener =  BottomMenuClickListener(clickListener)
        val state = BottomMenuItemState()
        fun draw(canvas:Canvas,paint:Paint) {
            if(tw == 0f && th == 0f) {
                tw = paint.measureText(text)
                th = paint.textSize
            }
            canvas.save()
            canvas.translate(x,y)
            canvas.save()
            canvas.scale(state.scale,state.scale)
            paint.color = Color.parseColor("#88EEEEEE")
            canvas.drawRoundRect(RectF(-tw/2,-th/2,tw/2,th/2),tw/10,tw/10,paint)
            canvas.restore()
            paint.color = Color.WHITE
            canvas.drawText(text,-tw/2,th/10,paint)
            canvas.restore()
        }
        fun update(stopcb: () -> Unit) {
            state.update {
                itemClickListener.clickListener?.invoke()
                stopcb()
            }
        }
        fun handleTap(startcb:()->Unit,x:Float,y:Float):Boolean {
            if(x>=this.x-tw/2 && x<=this.x+tw/2 && y>=this.y-th/2 && y<=this.y+th/2) {
                state.startUpdating {
                    startcb()
                }
                return true
            }
            return false
        }
    }
    data class BottomMenuItemState(var scale:Float = 0f,var deg:Float = 0f,var dir:Float = 0f) {
        fun update(stopcb: () -> Unit) {
            deg += 20*dir
            scale = Math.sin(deg*Math.PI/180).toFloat()
            if(deg > 180f) {
                deg = 0f
                scale = 0f
                dir = 0f
                stopcb()
            }
        }
        fun startUpdating(startcb:()->Unit) {
            if(dir == 0f) {
                dir = 1f
                startcb()
            }
        }
    }
    data class BottomMenuClickListener(var clickListener:()->Unit)
}
fun Canvas.drawLineIndicator(x:Float,y:Float,w:Float,scale:Float,paint:Paint) {
    paint.strokeWidth = w/15
    paint.color = Color.parseColor("#00E676")
    drawLine(x,y,x+w*scale,y,paint)
}