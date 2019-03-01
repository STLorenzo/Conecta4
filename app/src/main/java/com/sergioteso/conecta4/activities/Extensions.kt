package com.sergioteso.conecta4.activities

import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.widget.ImageButton
import android.widget.LinearLayout
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.Round
import com.sergioteso.conecta4.models.RoundRepository

fun ImageButton.update(player : Int){
    when(player){
        0 -> this.setBackgroundResource(R.drawable.casilla_vacia)
        1 -> this.setBackgroundResource(R.drawable.casilla_verde)
        2 -> this.setBackgroundResource(R.drawable.casilla_roja)
        else -> this.setBackgroundResource(R.drawable.casilla_amarilla)
    }
}