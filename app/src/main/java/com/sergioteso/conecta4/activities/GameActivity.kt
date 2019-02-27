package com.sergioteso.conecta4.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.models.MovimientoC4
import com.sergioteso.conecta4.models.TableroC4
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    val columnas = mutableMapOf<Int,LinearLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        val columns = intent.getIntExtra("columns", 4)
        val rows = intent.getIntExtra("rows", 4)
        val name = intent.getStringExtra("name_player")

        val tablero = TableroC4(columns, rows)

        tv_name.text = name
        tv_column.text = columns.toString()
        tv_row.text = rows.toString()

        crearBoard(tablero)

        Toast.makeText(this, "Cargado con exito",Toast.LENGTH_SHORT).show()
    }

    fun crearBoard(tablero: TableroC4){
        ll_board.removeAllViews()
        for (i in 0..tablero.columnas-1){
            columnas[i] = crearColumna(tablero.filas,i,tablero)
        }

        for (c in columnas.values){
            ll_board.addView(c)
        }
    }

    fun crearColumna(filas : Int,indiceColumna : Int ,tablero: TableroC4) : LinearLayout{
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.VERTICAL
        for (i in 0..filas-1)
            ll.addView(crearCasilla(tablero,indiceColumna,i))
        return ll
    }

    fun crearCasilla(tablero: TableroC4, indiceColumna: Int, indiceFila : Int): ImageButton {
        val ib = ImageButton(this)
        ib.setOnClickListener {
            tablero.matriz[indiceFila][indiceColumna] = Random.nextInt(1,3)
            crearBoard(tablero)
            println("clickado loco")
        }
        ib.update(tablero.matriz[indiceFila][indiceColumna])
        return ib
    }
}