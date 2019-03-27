package com.sergioteso.conecta4.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.Logger.log

class OptionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, OptionsActivity::class.java)
            return intent
        }
    }
}