package com.sergioteso.conecta4.activities

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.Round
import kotlinx.android.synthetic.main.list_item_round.view.*

class RoundViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview), View.OnClickListener {
    lateinit var idTextView: TextView
    lateinit var dateTextView: TextView
    lateinit var tableroTextView: TextView

    init{
        idTextView = itemview.findViewById(R.id.list_item_id) as TextView
        dateTextView = itemview.findViewById(R.id.list_item_date) as TextView
        tableroTextView = itemview.findViewById(R.id.list_item_tablero) as TextView
        itemview.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        Toast.makeText(itemView.context, "${idTextView.text}",Toast.LENGTH_SHORT).show()
        //val intent : Intent = Intent(textView.context, LoadActivity::class.java)
        //ContextCompat.startActivity(textView.context,intent,null)
    }

    fun bindRound(round: Round){
        idTextView.text = round.title
        dateTextView.text = round.date.toString().substring(0,19)
        tableroTextView.text = round.tableroc4.tableroInString()
    }
}


class RoundAdapter(var rounds: List<Round>) : RecyclerView.Adapter<RoundViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RoundViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val view = layoutInflater.inflate(R.layout.list_item_round,p0,false)
        return RoundViewHolder(view)
    }


    override fun getItemCount(): Int = rounds.size

    override fun onBindViewHolder(p0: RoundViewHolder, p1: Int) {
        p0.bindRound(rounds[p1])
    }
}