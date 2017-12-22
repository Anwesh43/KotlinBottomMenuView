package ui.anwesome.com.kotlinbottommenu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.bottommenuview.BottomMenuView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BottomMenuView.create(this)
    }
}
