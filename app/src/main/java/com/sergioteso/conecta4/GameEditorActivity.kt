package com.sergioteso.conecta4

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

class GameEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_editor)

        val sb_columns = findViewById<SeekBar>(R.id.seekBar_columns_GameEditor)
        val sb_rows = findViewById<SeekBar>(R.id.seekBar_rows_GameEditor)

        val tv_sb_columns = findViewById<TextView>(R.id.tv_sb_columns)
        val tv_sb_rows = findViewById<TextView>(R.id.tv_sb_rows)

        tv_sb_columns.text = sb_columns.progress.toString()
        tv_sb_rows.text = sb_rows.progress.toString()


        sb_columns.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_sb_columns.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Toast.makeText(applicationContext,"start tracking",Toast.LENGTH_SHORT).show()            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Toast.makeText(applicationContext,"stop tracking",Toast.LENGTH_SHORT).show()
            }
        })

        sb_rows.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_sb_rows.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Toast.makeText(applicationContext,"start tracking",Toast.LENGTH_SHORT).show()            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Toast.makeText(applicationContext,"stop tracking",Toast.LENGTH_SHORT).show()
            }
        })
    }

}