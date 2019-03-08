package com.sergioteso.conecta4.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.Fragments.GameFragment
/**
 * clase que implementa la actividad la actividad del juego principal.
 * Además implementa la interfaz PartidaListener para el correcto desarrollo de la partida enfrentando
 * a un jugador real contra un jugadorAleatorio segun los parametros de tablero pasados en el GameEditor
 */
class RoundActivity : AppCompatActivity(),
    GameFragment.OnRoundFragmentInteractionListener {
    override fun onRoundUpdated() {

    }
    /**
     * función que sobreescribe el onCreate básico de las Activities en Android
     * Se encarga de obtener los datos del Intent enviado por el GameEditor y crear el tablero con esos datos
     * asi como tambien crea al jugador local e inicializa la partida
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_list)
        val fm = supportFragmentManager
        if(fm.findFragmentById(R.id.fragment_game_container) == null){
            val fragment = GameFragment.newInstance(intent.getStringExtra(EXTRA_ROUND_ID))
            fm.beginTransaction().add(R.id.fragment_round_list_container,fragment).commit()
        }else{
            startActivity(RoundListActivity.newIntent(this))
        }

    }

    companion object{
        val EXTRA_ROUND_ID = "com.sergioteso.conecta4.round_id"
        val EXTRA_TYPE = "com.sergioteso.conecta4.game_type"
        val EXTRA_COLUMNS = "com.sergioteso.conecta4.columns"
        val EXTRA_ROWS = "com.sergioteso.conecta4.rows"
        val EXTRA_NAME = "com.sergioteso.conecta4.name"

        fun newIntentRound(context: Context,round_id: String): Intent {
            val intent = Intent(context, RoundActivity::class.java)
            intent.putExtra(EXTRA_ROUND_ID,round_id)
            intent.putExtra(EXTRA_TYPE,"roundlist")
            return intent
        }

        fun newIntentEditor(context: Context,columns: Int,rows: Int,name: String): Intent{
            val intent = Intent(context, RoundActivity::class.java)
            intent.putExtra(EXTRA_COLUMNS,columns)
            intent.putExtra(EXTRA_ROWS, rows)
            intent.putExtra(EXTRA_NAME,name)
            intent.putExtra(EXTRA_TYPE,"editor")
            return intent
        }
    }
}