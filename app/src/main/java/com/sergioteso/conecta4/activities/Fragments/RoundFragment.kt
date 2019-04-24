package com.sergioteso.conecta4.activities.Fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.SettingsActivityC4
import com.sergioteso.conecta4.models.*
import com.sergioteso.conecta4.views.ButtonC4
import es.uam.eps.multij.*
import kotlinx.android.synthetic.main.fragment_round.*
import java.lang.Exception


/**
 * Fragmento que modela una partida con su tablero y sus listeners oportunos.
 * Inicializa al jugador local y el tablero y muestra este ultimo por pantalla para
 * poder jugar.
 */
class RoundFragment : Fragment(), PartidaListener {
    private lateinit var round: Round
    private lateinit var name: String
    private lateinit var game: Partida
    private lateinit var tablero: TableroC4
    private lateinit var localPlayerC4: LocalPlayerC4
    private var casillas = mutableListOf<MutableList<ButtonC4>>()
    var listener: OnRoundFragmentInteractionListener? = null

    /**
     * interfaz que deben a implementar las clases que quieran ejecutar codigo cuando
     * ocurre una interaccion en la ronda de este fragmento. Principalmente actualizaciones de UI.
     */
    interface OnRoundFragmentInteractionListener {
        fun onRoundUpdated(round: Round)
    }

    /**
     * Metodo que ejecutado al crear la vista que se encarga de obtener la ronda de
     * RoundRepository en la cual se va a jugar.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            arguments?.let {
                round = Round.fromJSONString(it.getString(ARG_ROUND)!!)
            }
        } catch (e: Exception) {
            Log.d("DEBUG", e.message)
            activity?.finish()
        }

    }

    /**
     * Metodo llamado al crear la vista en el cual se le indica el layout a inflar.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_round, container, false)
    }

    /**
     * Metodo llamado una vez la vista es creada. Es el encargado en asignar al jugador local y el tablero y
     * establecer el listener del boton de reset
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_title.text = round.title
        if( savedInstanceState != null){
            round.board.stringToTablero(savedInstanceState.getString(BOARDSTRING))
        }

        name = SettingsActivityC4.getName(context!!)
        localPlayerC4 = LocalPlayerC4(name)
        tablero = round.board

        reset_round_fab.setOnClickListener {
            if (tablero.estado != Tablero.EN_CURSO) {
                Snackbar.make(
                    view,
                    R.string.round_already_finished, Snackbar.LENGTH_SHORT
                ).show()
            } else {
                tablero.reset()
                startRound()
                listener?.onRoundUpdated(round)
                //updateUI()
                board_viewc4.invalidate()
                Snackbar.make(
                    view, R.string.round_restarted,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Metodo que inicia una ronda. Para ello añade a la partida un jugador local y un jugador aleatorio y
     * la inicializa.
     */
    private fun startRound() {
        val players = ArrayList<Jugador>()
        val randomPlayer = JugadorAleatorio("Random Player")
        players.add(localPlayerC4)
        players.add(randomPlayer)
        game = Partida(tablero, players)
        game.addObservador(this)
        localPlayerC4.setPartida(game)

        board_viewc4.setBoard(round.board)
        board_viewc4.setOnPlayListener(localPlayerC4)

        if (game.tablero.estado == Tablero.EN_CURSO)
            game.comenzar()
    }

    /**
     * Metodo que se ejecuta al empezar la aplicacion. Crea el tablero e inicia la ronda.
     */
    override fun onStart() {
        super.onStart()
        startRound()
    }

    /**
     *
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d("DEBUG","attach")
        if (context is OnRoundFragmentInteractionListener)
            listener = context
        else {
            throw RuntimeException(
                context.toString() +
                        " must implement OnRoundFragmentInteractionListener"
            )
        }
    }

    /**
     *
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(BOARDSTRING, round.board.tableroToString())
        super.onSaveInstanceState(outState)
    }

    /**
     * Al resumir la actividad actualiza la interfaz
     */
    override fun onResume() {
        super.onResume()
        board_viewc4.invalidate()
    }

    /**
     * Funcion que se ejecuta siempre que hay un cambio en la partida mediante la interfaz PartidaListener.
     * Principalmente actualiza la UI de la aplicacion
     */
    override fun onCambioEnPartida(evento: Evento?) {
        when (evento?.tipo) {
            Evento.EVENTO_CAMBIO -> {
                board_viewc4.invalidate()
                listener?.onRoundUpdated(round)
            }
            Evento.EVENTO_FIN -> {
                listener?.onRoundUpdated(round)
                if (tablero.estado == Tablero.TABLAS) {
                    Toast.makeText(context, "Tablas - Game Over", Toast.LENGTH_SHORT).show()
                } else {
                    tablero.setComprobacionIJ(tablero.ultimoMovimiento as MovimientoC4)
                    Toast.makeText(context, "Gana - ${game.getJugador(tablero.turno).nombre}", Toast.LENGTH_LONG)
                        .show()
                }

                board_viewc4.invalidate()
                AlertDialogFragment().show(
                    activity?.supportFragmentManager, "ALERT_DIALOG"
                )
            }
        }
    }

    /**
     * objeto estatico que permite a clases externas pasar el argumento del id de la ronda al instanciarse.
     */
    companion object {
        private const val ARG_ROUND = "com.sergioteso.conecta4.round"
        private const val BOARDSTRING = "com.sergioteso.conecta4.boardstring"
        @JvmStatic
        fun newInstance(round: String) =
            RoundFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ROUND, round)
                }
            }
    }
}
