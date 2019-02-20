package com.sergioteso.conecta4

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar

class GameEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_game_editor)

        val sb_columns = findViewById<SeekBar>(R.id.seekBar_columns_GameEditor)
        val sb_rows = findViewById<SeekBar>(R.id.seekBar_rows_GameEditor)
    }

}