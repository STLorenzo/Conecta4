package com.sergioteso.conecta4.activities

import android.support.v4.app.Fragment
import com.sergioteso.conecta4.models.Round

class RoundListFragment : Fragment() {

    fun onRoundSelected(round: Round){
        val intent = GameActivity.newIntentRound(context!!, round.id)
        startActivity(intent)
    }
}