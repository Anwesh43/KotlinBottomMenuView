package ui.anwesome.com.kotlinbottommenu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ui.anwesome.com.bottommenuview.BottomMenuView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = BottomMenuView.create(this)
        for(i in 1..10) {
            val text = "hello world ${i}"
            view.addMenu(text,{Toast.makeText(this,text,Toast.LENGTH_SHORT).show()})
        }
        view.addToParent(this)
    }
}
