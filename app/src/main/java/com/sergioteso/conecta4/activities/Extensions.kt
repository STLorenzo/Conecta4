package com.sergioteso.conecta4.activities

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.Round
import com.sergioteso.conecta4.models.RoundRepository
import com.sergioteso.conecta4.models.TableroC4
import kotlinx.android.synthetic.main.fragment_round_list.*
import kotlinx.android.synthetic.main.fragment_round_list.view.*

/**
 * Funcion que extiende la funcionalidad de un ImageButton permitiendo establecer
 * su background segun el valor de un player del tablero.
 */
fun ImageButton.update(player : Int){
    when(player){
        TableroC4.CASILLA_J1 -> this.setBackgroundResource(R.drawable.casilla_verde)
        TableroC4.CASILLA_J2 -> this.setBackgroundResource(R.drawable.casilla_roja)
        TableroC4.CASILLA_WIN_J1 -> this.setBackgroundResource(R.drawable.casilla_win_verde)
        TableroC4.CASILLA_WIN_J2 -> this.setBackgroundResource(R.drawable.casilla_win_roja)
        else -> this.setBackgroundResource(R.drawable.casilla_vacia)
    }
}

fun RecyclerView.update(onClickListener: (Round) -> Unit){
        if(adapter == null)
            adapter = RoundAdapter(RoundRepository.rounds, onClickListener)
        adapter?.notifyDataSetChanged()
}