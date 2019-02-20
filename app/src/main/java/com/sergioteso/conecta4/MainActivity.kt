package com.sergioteso.conecta4

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_new_game_mainmenu.setOnClickListener {
            val intent = Intent(this,GameEditorActivity::class.java)
            startActivity(intent)
        }
    }
}
