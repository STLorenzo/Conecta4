package com.sergioteso.conecta4.activities

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.Round
import com.sergioteso.conecta4.views.ViewC4
import es.uam.eps.multij.Tablero

/**
 * Clase que implementa el ViewHolder de RecyclerView
 */
class RoundViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
    var cardView: CardView
    var idTextView: TextView
    var dateTextView: TextView
    //var tableroViewC4: TextView
    var tableroViewC4: ViewC4
    var item_rl: RelativeLayout

    /**
     * Al inicializar la clase obtiene del layout las views a usar
     */
    init {
        item_rl = itemview.findViewById(R.id.list_item_rl)
        cardView = itemview.findViewById(R.id.list_cardview)
        idTextView = itemview.findViewById(R.id.list_item_id) as TextView
        dateTextView = itemview.findViewById(R.id.list_item_date) as TextView
        //tableroViewC4 = itemview.findViewById(R.id.list_item_tablero) as TextView
        tableroViewC4 = itemview.findViewById(R.id.list_board_viewc4) as ViewC4
    }

    /**
     * funcion que establece los valores de sus views de acorde a la ronda y el listener pasados
     * como parametros
     */
    fun bindRound(round: Round, listener: (Round) -> Unit) {
        idTextView.text = round.title
        dateTextView.text = round.date.substring(0, 19)
        //tableroViewC4.text = round.board.tableroInString()
        tableroViewC4.setBoard(round.board)

        tableroViewC4.print_board()

        if (round.board.estado == Tablero.FINALIZADA) {
            itemView.setBackgroundResource(R.color.darkRed)
        } else if (round.board.estado == Tablero.TABLAS) {
            itemView.setBackgroundResource(R.color.darkYellow)
        } else {
            itemView.setBackgroundResource(R.color.darkGreen)
        }

        item_rl.setOnClickListener { listener(round) }
        cardView.setOnClickListener { listener(round) }
        idTextView.setOnClickListener { listener(round) }
        tableroViewC4.setOnClickListener { listener(round) }
        dateTextView.setOnClickListener { listener(round) }

    }
}

/**
 * Clase que implementa el Adapter de RecyclerView basandose en el Holder previamente implmentado.
 */
class RoundAdapter(var rounds: List<Round>, val listener: (Round) -> Unit) : RecyclerView.Adapter<RoundViewHolder>() {

    /**
     * Funcion que infla el layout del elemento de la lista y devuelve su Holder correspondiente a esa vista.
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RoundViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val view = layoutInflater.inflate(R.layout.list_item_round, p0, false)
        return RoundViewHolder(view)
    }

    /**
     * Establece el numero de items en el Holder
     */
    override fun getItemCount(): Int = rounds.size

    /**
     * Hace un bind de la ronda en el indice pasado con el RoundViewHolder
     */
    override fun onBindViewHolder(p0: RoundViewHolder, p1: Int) {
        p0.bindRound(rounds[p1], listener)
    }
}