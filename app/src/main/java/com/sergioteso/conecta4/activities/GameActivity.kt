package com.sergioteso.conecta4.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sergioteso.conecta4.R
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tv_name.text = intent.getStringExtra("name_player")
        tv_column.text = intent.getIntExtra("columns",4).toString()
        tv_row.text = intent.getIntExtra("rows",4).toString()

        //Toast.makeText(this,"Name: ${intent.getStringExtra("name")}",Toast.LENGTH_SHORT).show()
    }
}