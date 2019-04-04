package com.sergioteso.conecta4.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.sergioteso.conecta4.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Actividad que funciona a modo de menu principal del Juego.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_new_game_mainmenu.setOnClickListener {
            startActivity(Intent(this, LoginActivityC4::class.java))
        }

        btn_about_mainmenu.setOnClickListener {
            startActivity(OptionsActivity.newIntent(this))
        }


    }

}


