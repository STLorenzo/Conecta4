package com.sergioteso.conecta4.activities

import android.content.Context
import android.widget.ImageButton
import android.widget.LinearLayout
import com.sergioteso.conecta4.R

fun ImageButton.update(player : Int){
    when(player){
        0 -> this.setBackgroundResource(R.drawable.casilla_vacia)
        1 -> this.setBackgroundResource(R.drawable.casilla_verde)
        else -> this.setBackgroundResource(R.drawable.casilla_roja)
    }
}