package com.example.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlin.view.JoinChatFragment

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        //view fragment
        val homeFragment = JoinChatFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fl_view, homeFragment)
        ft.commit()
    }
}