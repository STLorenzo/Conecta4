package com.sergioteso.conecta4.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.RoundRepository
import kotlinx.android.synthetic.main.activity_load.*

class LoadActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)

        round_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@LoadActivity)
            itemAnimator = DefaultItemAnimator()
        }
    }

    override fun onResume() {
        super.onResume()
        update()
    }

    private fun update() {
        round_recycler_view.apply {
            if(adapter == null)
                adapter = RoundAdapter(RoundRepository.rounds)
            adapter?.notifyDataSetChanged()
        }
    }
}