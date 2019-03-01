package com.sergioteso.conecta4.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.MovimientoC4
import com.sergioteso.conecta4.models.TableroC4
import es.uam.eps.multij.*
import kotlinx.android.synthetic.main.activity_game.*
import java.lang.Exception

class GameActivity : AppCompatActivity(), PartidaListener {
    private lateinit var game: Partida
    private lateinit var tablero: TableroC4
    private lateinit var localPlayerC4 : LocalPlayerC4
    private var casillas = mutableListOf<MutableList<ImageButton>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        val columns = intent.getIntExtra("columns", 4)
        val rows = intent.getIntExtra("rows", 4)
        val name = intent.getStringExtra("name_player")
        tv_name.text = name
        tv_column.text = columns.toString()
        tv_row.text = rows.toString()

        tablero = TableroC4(rows, columns)
        localPlayerC4 = LocalPlayerC4(name)
        crearBoard()
        startRound()

        Toast.makeText(this, "Cargado con exito",Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        casillas = mutableListOf()
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
            casillas.add(mutableListOf())
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
                casillas[i][j].update(tablero.matriz[i][j])
            }
        }
    }

    override fun onCambioEnPartida(evento: Evento?) {
        when(evento?.tipo){
            Evento.EVENTO_CAMBIO -> updateUI()
            Evento.EVENTO_FIN -> {
                updateUI()
                if( tablero.estado == Tablero.TABLAS){
                    Toast.makeText(this,"Tablas - Game Over",Toast.LENGTH_SHORT).show()
                }else{
                    val mapa = tablero.comprobacionIJ(tablero.ultimoMovimiento as MovimientoC4)
                    if ( mapa != null){
                        for (i in mapa)
                            casillas[i[0]][i[1]].update(3)
                    }
                    tv_name.text = "Ha Ganado ${game.getJugador(tablero.turno).nombre}"
                    Toast.makeText(this,"Gana - ${game.getJugador(tablero.turno).nombre}",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}