package com.sergioteso.conecta4.activities

import android.widget.ImageButton
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.Round

fun ImageButton.update(player : Int){
    when(player){
        Round.CASILLA_J1 -> this.setBackgroundResource(R.drawable.casilla_verde)
        Round.CASILLA_J2 -> this.setBackgroundResource(R.drawable.casilla_roja)
        Round.CASILLA_WIN_J1 -> this.setBackgroundResource(R.drawable.casilla_win_verde)
        Round.CASILLA_WIN_J2 -> this.setBackgroundResource(R.drawable.casilla_win_roja)
        else -> this.setBackgroundResource(R.drawable.casilla_vacia)
    }
}