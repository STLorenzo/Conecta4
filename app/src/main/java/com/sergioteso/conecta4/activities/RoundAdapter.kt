package com.sergioteso.conecta4.activities

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.Round
import com.sergioteso.conecta4.models.RoundRepository
import es.uam.eps.multij.ExcepcionJuego
import es.uam.eps.multij.Tablero
import kotlinx.android.synthetic.main.list_item_round.view.*

class RoundViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){
    var idTextView: TextView
    var dateTextView: TextView
    var tableroTextView: TextView

    init{
        idTextView = itemview.findViewById(R.id.list_item_id) as TextView
        dateTextView = itemview.findViewById(R.id.list_item_date) as TextView
        tableroTextView = itemview.findViewById(R.id.list_item_tablero) as TextView
    }

    fun bindRound(round: Round, listener: (Round) -> Unit){
        idTextView.text = round.title
        dateTextView.text = round.date.toString().substring(0,19)
        tableroTextView.text = round.tableroc4.tableroInString()
        if(round.tableroc4.estado == Tablero.FINALIZADA){
            itemView.setBackgroundResource(R.color.darkRed)
        }else if(round.tableroc4.estado == Tablero.FINALIZADA){
            itemView.setBackgroundResource(R.color.darkYellow)
        }else{
            itemView.setBackgroundResource(R.color.darkGreen)
        }
        itemView.setOnClickListener{listener(round)}
    }
}


class RoundAdapter(var rounds: List<Round>, val listener : (Round) -> Unit) : RecyclerView.Adapter<RoundViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RoundViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val view = layoutInflater.inflate(R.layout.list_item_round,p0,false)
        return RoundViewHolder(view)
    }

    override fun getItemCount(): Int = rounds.size

    override fun onBindViewHolder(p0: RoundViewHolder, p1: Int) {
        p0.bindRound(rounds[p1],listener)
    }
}