package com.sergioteso.conecta4

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game_editor.*

class GameEditorActivity : AppCompatActivity() {

    private var MIN_COLUMN = 4
    private var MIN_ROW = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_editor)

        val sb_listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                cambioTextoSeekBar(seekBar?.id,progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //TODO
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //TODO
            }
        }

        tv_sb_columns.text = (seekBar_columns_GameEditor.progress + MIN_COLUMN).toString()
        tv_sb_rows.text = (seekBar_rows_GameEditor.progress + MIN_ROW).toString()


        seekBar_columns_GameEditor.setOnSeekBarChangeListener(sb_listener)
        seekBar_rows_GameEditor.setOnSeekBarChangeListener(sb_listener)

        btn_start_GameEditor.setOnClickListener {
            val intent = Intent(this,GameActivity::class.java)
            intent.putExtra("columns",seekBar_columns_GameEditor.progress+MIN_COLUMN)
            intent.putExtra("rows",seekBar_rows_GameEditor.progress+MIN_ROW)
            intent.putExtra("name",et_insertplayer_GameEditor.text)
            startActivity(intent)
        }
    }

    fun cambioTextoSeekBar(id: Int?,progress : Int){
        val n : Int
        when(id){
            R.id.seekBar_columns_GameEditor -> {
                n = progress + MIN_COLUMN
                tv_sb_columns.text = n.toString()
            }

            R.id.seekBar_rows_GameEditor -> {
                n = progress + MIN_ROW
                tv_sb_rows.text = n.toString()
            }

        }
    }

}