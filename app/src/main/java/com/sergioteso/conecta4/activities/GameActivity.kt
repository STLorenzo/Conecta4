package com.sergioteso.conecta4.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.LocalPlayerC4
import com.sergioteso.conecta4.models.MovimientoC4
import com.sergioteso.conecta4.models.RoundRepository
import com.sergioteso.conecta4.models.TableroC4
import es.uam.eps.multij.*
import kotlinx.android.synthetic.main.activity_game.*
import java.lang.Exception

/**
 * clase que implementa la actividad la actividad del juego principal.
 * Además implementa la interfaz PartidaListener para el correcto desarrollo de la partida enfrentando
 * a un jugador real contra un jugadorAleatorio segun los parametros de tablero pasados en el GameEditor
 */
class GameActivity : AppCompatActivity(), PartidaListener {
    //TODO: Cambiar para que esto llame a GameFragment teniendo cambiando los intents por newInstance :D
    val BOARDSTRING = "com.sergioteso.conecta4.grid"
    private lateinit var game: Partida
    private lateinit var tablero: TableroC4
    private lateinit var localPlayerC4 : LocalPlayerC4
    private var casillas = mutableListOf<MutableList<ImageButton>>()

    /**
     * función que sobreescribe el onCreate básico de las Activities en Android
     * Se encarga de obtener los datos del Intent enviado por el GameEditor y crear el tablero con esos datos
     * asi como tambien crea al jugador local e inicializa la partida
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val columns :Int
        val rows : Int
        val name : String

        if(intent.getStringExtra(EXTRA_TYPE) == "editor"){
            columns = intent.getIntExtra(EXTRA_COLUMNS, 4)
            rows = intent.getIntExtra(EXTRA_ROWS, 4)
            name = intent.getStringExtra(EXTRA_NAME)
            tablero = TableroC4(rows, columns)
            localPlayerC4 = LocalPlayerC4(name)
        }else if(intent.getStringExtra(EXTRA_TYPE) == "roundlist"){
            tablero = getTableroByID(intent.getStringExtra(EXTRA_ROUND_ID))
            localPlayerC4 = LocalPlayerC4("Anon")
        }else
            throw ExcepcionJuego("Error al inicializar GameActivity")


        crearBoard()
        startRound()
    }

    private fun getTableroByID(id: String?): TableroC4 {
        val round = RoundRepository.rounds.filter { round -> round.id == id  }.single()
        return round.tableroc4
    }

    /**
     * Funcion que sobreescribe la funcionalidad basica de las Activities de guardar el InstanceState
     * Guarda la versión string de tableroToString para recuperarla luego con el metodo onRestoreInstanceState
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(BOARDSTRING,tablero.tableroToString())
        super.onSaveInstanceState(outState)
    }

    /**
     * Funcion que sobreescribe la funcionalidad basica de las Activities de restaurar el InstanceState
     * Restaura la versión string de tableroToString mediante la funcion stringToTablero
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        try{
            if (savedInstanceState?.getString(BOARDSTRING) != null)
                tablero.stringToTablero(savedInstanceState.getString(BOARDSTRING))
                updateUI()
        }catch (e: ExcepcionJuego){
            e.printStackTrace()
            Toast.makeText(this,R.string.game_exception,Toast.LENGTH_SHORT ).show()
        }
    }

    fun crearBoard(){
        ll_board.removeAllViews()
        for (i in 0..tablero.columnas-1){
            val col = crearColumna(tablero.filas,i)
            ll_board.addView(col)
        }
    }

    fun crearColumna(filas : Int,indiceColumna : Int ) : LinearLayout{
        val ll = LinearLayout(this)
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
                    Toast.makeText(this, R.string.round_already_finished,Toast.LENGTH_SHORT).show()
                }else{
                    val m = MovimientoC4(indiceColumna)
                    val a = AccionMover(localPlayerC4,m)
                    game.realizaAccion(a)
                }
            }catch (e: Exception){
                Toast.makeText(this, R.string.invalid_movement,Toast.LENGTH_SHORT).show()
            }
        }
        return ll
    }

    fun crearCasilla(indiceColumna: Int, indiceFila : Int): ImageButton {
        val ib = ImageButton(this)
        ib.isClickable = false
        ib.update(tablero.matriz[indiceFila][indiceColumna])
        return ib
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

    override fun onCambioEnPartida(evento: Evento?) {
        when(evento?.tipo){
            Evento.EVENTO_CAMBIO -> updateUI()
            Evento.EVENTO_FIN -> {
                if( tablero.estado == Tablero.TABLAS){
                    Toast.makeText(this,"Tablas - Game Over",Toast.LENGTH_SHORT).show()
                }else{
                    tablero.setComprobacionIJ(tablero.ultimoMovimiento as MovimientoC4)
                    Toast.makeText(this,"Gana - ${game.getJugador(tablero.turno).nombre}",Toast.LENGTH_SHORT).show()
                }
                updateUI()
            }
        }
    }

    companion object{
        val EXTRA_ROUND_ID = "com.sergioteso.conecta4.round_id"
        val EXTRA_TYPE = "com.sergioteso.conecta4.game_type"
        val EXTRA_COLUMNS = "com.sergioteso.conecta4.columns"
        val EXTRA_ROWS = "com.sergioteso.conecta4.rows"
        val EXTRA_NAME = "com.sergioteso.conecta4.name"

        fun newIntentRound(context: Context,id: String): Intent {
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtra(EXTRA_ROUND_ID,id)
            intent.putExtra(EXTRA_TYPE,"roundlist")
            return intent
        }

        fun newIntentEditor(context: Context,columns: Int,rows: Int,name: String): Intent{
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtra(EXTRA_COLUMNS,columns)
            intent.putExtra(EXTRA_ROWS, rows)
            intent.putExtra(EXTRA_NAME,name)
            intent.putExtra(EXTRA_TYPE,"editor")
            return intent
        }
    }
}