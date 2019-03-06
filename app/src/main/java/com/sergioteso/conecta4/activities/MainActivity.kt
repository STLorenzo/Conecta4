package com.sergioteso.conecta4.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.sergioteso.conecta4.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_new_game_mainmenu.setOnClickListener {
            val intent = Intent(this, GameEditorActivity::class.java)
            startActivity(intent)
        }

        btn_load_game_mainmenu.setOnClickListener {
            val intent = Intent(this, RoundListActivity::class.java)
            startActivity(intent)
        }
    }
}
