package com.sergioteso.conecta4

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.Btn_start_mainmenu)
        btnStart.setOnClickListener {
            val intent = Intent(this,GameEditorActivity::class.java)
            startActivity(intent)
        }
        val btnOptions = findViewById<Button>(R.id.Btn_options_mainmenu)
    }
}
