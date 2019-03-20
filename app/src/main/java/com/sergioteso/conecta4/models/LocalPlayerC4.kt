package com.sergioteso.conecta4.models

import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.MovimientoC4
import es.uam.eps.multij.*
import java.lang.Exception


class LocalPlayerC4(var name: String) : Jugador {
    private lateinit var game: Partida

    fun setPartida(game: Partida) {
        this.game = game
    }

    override fun getNombre(): String = name

    override fun puedeJugar(p0: Tablero?): Boolean = true

    override fun onCambioEnPartida(p0: Evento?) {}

}