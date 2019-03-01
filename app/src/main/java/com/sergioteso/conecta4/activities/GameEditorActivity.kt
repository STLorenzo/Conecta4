package com.sergioteso.conecta4.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.Toast
import com.sergioteso.conecta4.R
import kotlinx.android.synthetic.main.activity_game_editor.*
import android.util.DisplayMetrics



class GameEditorActivity : AppCompatActivity() {

    private var MIN_COLUMN = 4
    private var MIN_ROW = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_editor)

        val sb_listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                cambioTextoSeekBar(seekBar?.id, progress)
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

        seekBar_rows_GameEditor.max

        btn_start_GameEditor.setOnClickListener {
            val s = et_insertplayer_GameEditor.text.toString()
            if (s == "") {
                Toast.makeText(this, getString(R.string.insert_player_hint), Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("columns", seekBar_columns_GameEditor.progress + MIN_COLUMN)
                intent.putExtra("rows", seekBar_rows_GameEditor.progress + MIN_ROW)
                intent.putExtra("name_player", et_insertplayer_GameEditor.text.toString())
                startActivity(intent)
            }
        }
    }

    fun cambioTextoSeekBar(id: Int?, progress: Int) {
        val n: Int
        when (id) {
            seekBar_columns_GameEditor.id -> {
                n = progress + MIN_COLUMN
                tv_sb_columns.text = n.toString()
            }

            seekBar_rows_GameEditor.id -> {
                n = progress + MIN_ROW
                tv_sb_rows.text = n.toString()
            }

        }
    }

    fun getDimensiones(){
        //TODO: calcular las dimensiones para el maximo del seekbar
    }

}