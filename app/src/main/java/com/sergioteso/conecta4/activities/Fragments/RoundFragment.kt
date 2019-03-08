package com.sergioteso.conecta4.activities.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.update
import com.sergioteso.conecta4.models.*
import es.uam.eps.multij.*
import kotlinx.android.synthetic.main.fragment_round.*
import java.lang.Exception


private const val ROUND_ID = "round_id"

class GameFragment : Fragment(), PartidaListener{
    private lateinit var round: Round
    private lateinit var game: Partida
    private lateinit var tablero: TableroC4
    private lateinit var localPlayerC4 : LocalPlayerC4
    private var casillas = mutableListOf<MutableList<ImageButton>>()
    var listener: OnRoundFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            round = RoundRepository.getRound(it.getString(ROUND_ID))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_round, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_title.text = round.title
        localPlayerC4 = LocalPlayerC4("Anon")
        tablero = round.tableroc4
    }

    interface OnRoundFragmentInteractionListener {
        fun onRoundUpdated()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is OnRoundFragmentInteractionListener)
            listener = context
        else{
            throw RuntimeException(context.toString() +
                " must implement OnRoundFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(round_id: String) =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putString(ROUND_ID, round_id)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        crearBoard()
        startRound()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onCambioEnPartida(evento: Evento?) {
        listener?.onRoundUpdated()
        when(evento?.tipo){
            Evento.EVENTO_CAMBIO ->{
                updateUI()
            }
            Evento.EVENTO_FIN -> {
                if( tablero.estado == Tablero.TABLAS){
                    updateUI()
                    Toast.makeText(context,"Tablas - Game Over",Toast.LENGTH_SHORT).show()
                }else{
                    tablero.setComprobacionIJ(tablero.ultimoMovimiento as MovimientoC4)
                    Toast.makeText(context,"Gana - ${game.getJugador(tablero.turno).nombre}",Toast.LENGTH_SHORT).show()
                }
                updateUI()
            }
        }
    }

    fun updateUI(){
        for (j in 0..tablero.columnas-1){
            for (i in 0..tablero.filas-1){
                try{
                    casillas[i][j].update(tablero.getTablero(i,j))
                }catch (e: ExcepcionJuego){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun startRound() {
        val players = ArrayList<Jugador>()
        val randomPlayer = JugadorAleatorio("Random Player")
        players.add(localPlayerC4)
        players.add(randomPlayer)
        game = Partida(tablero, players)
        game.addObservador(this)
        localPlayerC4.setPartida(game)
        if(game.tablero.estado == Tablero.EN_CURSO)
            game.comenzar()
    }

    fun crearBoard(){
        ll_board.removeAllViews()
        for (i in 0..tablero.columnas-1){
            val col = crearColumna(tablero.filas,i)
            ll_board.addView(col)
        }
    }

    fun crearColumna(filas : Int,indiceColumna : Int ) : LinearLayout {
        val ll = LinearLayout(context)
        var casilla: ImageButton
        ll.orientation = LinearLayout.VERTICAL
        for (i in 0..filas-1) {
            if(casillas.size < filas) casillas.add(mutableListOf())
            casilla = crearCasilla(indiceColumna, i)
            ll.addView(casilla)
            casillas[i].add(casilla)
        }
        ll.isClickable = true
        ll.setOnClickListener{
            try {
                if(game.tablero.estado != Tablero.EN_CURSO){
                    Toast.makeText(context, R.string.round_already_finished, Toast.LENGTH_SHORT).show()
                }else{
                    val m = MovimientoC4(indiceColumna)
                    val a = AccionMover(localPlayerC4,m)
                    game.realizaAccion(a)
                }
            }catch (e: Exception){
                Toast.makeText(context, R.string.invalid_movement, Toast.LENGTH_SHORT).show()
            }
        }
        return ll
    }

    fun crearCasilla(indiceColumna: Int, indiceFila : Int): ImageButton {
        val ib = ImageButton(context)
        ib.isClickable = false
        ib.update(tablero.matriz[indiceFila][indiceColumna])
        return ib
    }
}
