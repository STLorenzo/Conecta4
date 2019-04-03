package com.sergioteso.conecta4.activities.Fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.SettingsActivityC4
import com.sergioteso.conecta4.models.*
import com.sergioteso.conecta4.views.ButtonC4
import es.uam.eps.multij.*
import kotlinx.android.synthetic.main.fragment_round2.*
import java.lang.Exception


private const val ROUND_ID = "round_id"
private const val NAME = "name"

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
        fun onRoundUpdated()
    }

    /**
     * Metodo que ejecutado al crear la vista que se encarga de obtener la ronda de
     * RoundRepository en la cual se va a jugar.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = SettingsActivityC4.getName(context!!)
            round = RoundRepository.getRound(it.getString(ROUND_ID))
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
        return inflater.inflate(R.layout.fragment_round2, container, false)
    }

    /**
     * Metodo llamado una vez la vista es creada. Es el encargado en asignar al jugador local y el tablero y
     * establecer el listener del boton de reset
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_title.text = round.title
        localPlayerC4 = LocalPlayerC4(name)
        tablero = round.tableroc4
        reset_round_fab.setOnClickListener {
            if (tablero.estado != Tablero.EN_CURSO) {
                Snackbar.make(
                    view,
                    R.string.round_already_finished, Snackbar.LENGTH_SHORT
                ).show()
            } else {
                tablero.reset()
                startRound()
                listener?.onRoundUpdated()
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
     *
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
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

    /**
     * objeto estatico que permite a clases externas pasar el argumento del id de la ronda al instanciarse.
     */
    companion object {
        @JvmStatic
        fun newInstance(round_id: String, name: String) =
            RoundFragment().apply {
                arguments = Bundle().apply {
                    putString(ROUND_ID, round_id)
                    putString(NAME, name)
                }
            }
    }

    /**
     * Al resumir la actividad actualiza la interfaz
     */
    override fun onResume() {
        super.onResume()
        //updateUI()
        board_viewc4.invalidate()
    }

    /**
     * Funcion que se ejecuta siempre que hay un cambio en la partida mediante la interfaz PartidaListener.
     * Principalmente actualiza la UI de la aplicacion
     */
    override fun onCambioEnPartida(evento: Evento?) {
        when (evento?.tipo) {
            Evento.EVENTO_CAMBIO -> {
                //updateUI()
                board_viewc4.invalidate()
                listener?.onRoundUpdated()
            }
            Evento.EVENTO_FIN -> {
                listener?.onRoundUpdated()
                if (tablero.estado == Tablero.TABLAS) {
                    Toast.makeText(context, "Tablas - Game Over", Toast.LENGTH_SHORT).show()
                } else {
                    tablero.setComprobacionIJ(tablero.ultimoMovimiento as MovimientoC4)
                    Toast.makeText(context, "Gana - ${game.getJugador(tablero.turno).nombre}", Toast.LENGTH_LONG)
                        .show()
                }

                //updateUI()
                board_viewc4.invalidate()
                AlertDialogFragment().show(
                    activity?.supportFragmentManager, "ALERT_DIALOG"
                )

            }
        }
    }

    /**
     * Metodo que actualiza la interfaz de Usuario del tablero
     */
    fun updateUI() {
        for (j in 0..tablero.columnas - 1) {
            for (i in 0..tablero.filas - 1) {
                try {
                    casillas[i][j].setBackgroundCasilla(tablero.getTablero(i, j))
                } catch (e: ExcepcionJuego) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Metodo que se ejecuta al empezar la aplicacion. Crea el tablero e inicia la ronda.
     */
    override fun onStart() {
        super.onStart()
        //crearBoard()
        startRound()
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

        board_viewc4.setBoard(round.tableroc4)
        board_viewc4.setOnPlayListener(localPlayerC4)


        if (game.tablero.estado == Tablero.EN_CURSO)
            game.comenzar()
    }


    /**
     * Crea el tablero en la UI llamando a crearColumna para cada columna que tenga el tablero

    fun crearBoard() {
    ll_board.removeAllViews()
    for (i in 0..tablero.columnas - 1) {
    val col = crearColumna(tablero.filas, i)
    ll_board.addView(col)
    }
    }*/

    /**
     * Crea cada columna en la UI llamando al metodo de crearCasilla para cada casilla en la columna.
     * A cada columna creada como le añade su correspondiente listener para realizar los movimientos
     * en el Tablero.
     */
    fun crearColumna(filas: Int, indiceColumna: Int): LinearLayout {
        val ll = LinearLayout(context)
        var casilla: ButtonC4
        ll.orientation = LinearLayout.VERTICAL
        for (i in 0..filas - 1) {
            if (casillas.size < filas) casillas.add(mutableListOf())
            casilla = crearCasilla(indiceColumna, i)
            ll.addView(casilla)
            casillas[i].add(casilla)
        }
        ll.isClickable = true
        ll.setOnClickListener {
            try {
                if (game.tablero.estado != Tablero.EN_CURSO) {
                    Toast.makeText(context, R.string.round_already_finished, Toast.LENGTH_SHORT).show()
                } else {
                    val m = MovimientoC4(indiceColumna)
                    val a = AccionMover(localPlayerC4, m)
                    game.realizaAccion(a)
                }
            } catch (e: Exception) {
                Toast.makeText(context, R.string.invalid_movement, Toast.LENGTH_SHORT).show()
            }
        }
        return ll
    }

    /**
     * Crea cada casilla en la UI creandola de la clase personalizada ButtonC4 mostrando cada casilla del color
     * correspondiente a su posicion en el Tablero.
     */
    fun crearCasilla(indiceColumna: Int, indiceFila: Int): ButtonC4 {
        if (context == null) {
            throw ExcepcionJuego("Fallo al crear casilla")
        } else {
            val ib = ButtonC4(context!!)
            ib.isClickable = false
            ib.setBackgroundCasilla(tablero.matriz[indiceFila][indiceColumna])
            return ib
        }

    }
}
