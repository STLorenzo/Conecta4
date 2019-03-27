package com.sergioteso.conecta4.models

import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.MovimientoC4
import com.sergioteso.conecta4.views.ViewC4
import es.uam.eps.multij.*
import java.lang.Exception


class LocalPlayerC4(var name: String) : Jugador, ViewC4.OnPlayListener {
    private lateinit var game: Partida

    override fun onPlay(column: Int) {
        if(game.tablero.estado != Tablero.EN_CURSO)
            return
        val m : MovimientoC4
        m = MovimientoC4(column)
        game.realizaAccion(AccionMover(this,m))
    }

    fun setPartida(game: Partida) {
        this.game = game
    }

    override fun getNombre(): String = name

    override fun puedeJugar(p0: Tablero?): Boolean = true

    override fun onCambioEnPartida(p0: Evento?) {}

}