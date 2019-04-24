package com.sergioteso.conecta4.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.Fragments.RoundFragment
import com.sergioteso.conecta4.activities.Fragments.RoundListFragment
import com.sergioteso.conecta4.models.Round
import com.sergioteso.conecta4.models.RoundRepository
import com.sergioteso.conecta4.models.RoundRepositoryFactory
import com.sergioteso.conecta4.models.TableroC4
import kotlinx.android.synthetic.main.activity_round_list.*
import kotlinx.android.synthetic.main.fragment_round_list.*


/**
 * Actividad que modela la visualizacion de la lista de rondas disponibles en la aplicacion.
 * Implementa las interfaces para interaccionar tanto con el fragmento de la lista de rondas como el
 * fragmento de ronda.
 * Esta actividad segun las densidades de la pantalla instanciara ambos fragmentos o lanzara una actividad de
 * tipo RoundActivity.
 */
class RoundListActivity : AppCompatActivity(),
    RoundListFragment.OnFragmentInteractionListener,
    RoundFragment.OnRoundFragmentInteractionListener {

    private var repository: RoundRepository? = null
    /**
     * Metodo que actualiza la UI del RecyclerView de la lista de partidas notificandole que
     * los datos de esta han cambiado
     */
    override fun onRoundUpdated(round: Round) {
        repository = RoundRepositoryFactory.createRepository(this)
        val callback = object : RoundRepository.BooleanCallback {
            override fun onResponse(response: Boolean) {
                if (response == true) {
                    //Log.d("DEBUG","onRoundUpdated")
                    round_recycler_view.update(
                        SettingsActivityC4.getPlayerUUID(baseContext),
                        { round -> onRoundSelected(round) }
                    )
                } else
                    Snackbar.make(findViewById(R.id.title),
                        R.string.error_updating_round,
                        Snackbar.LENGTH_LONG).show()
            }
        }
        Log.d("DEBUG","update_roudn_list")
        repository?.updateRound(round, callback)
    }


    /**
     * Funcion que se ejecuta al seleccionar una ronda en la lista. Segun la densidad instancia el fragmento
     * RoundFragment o lanza la actividad RoundActivity
     */
    override fun onRoundSelected(round: Round) {
        val fm = supportFragmentManager
        if (fragment_game_container != null) {
            val gameFragment = RoundFragment.newInstance(round.toJSONString())
            fm.beginTransaction().replace(R.id.fragment_game_container, gameFragment).commit()
        } else {
            val intento = RoundActivity.newIntentRound(this, round.toJSONString())
            startActivityForResult(intento, GAME_REQUEST_ID)
        }
    }

    /**
     * Metodo que añade una ronda y actualiza la interfaz
     */
    override fun onRoundAdded() {
        val round = Round(SettingsActivityC4.getRows(this),
            SettingsActivityC4.getColumns(this))
        round.firstPlayerName = "Random"
        round.firstPlayerUUID = "Random"
        round.secondPlayerName = SettingsActivityC4.getPlayerName(this)
        round.secondPlayerUUID = SettingsActivityC4.getPlayerUUID(this)

        repository = RoundRepositoryFactory.createRepository(this)

        val callback = object : RoundRepository.BooleanCallback {
            override fun onResponse(response: Boolean) {
                if (response == false)
                    Snackbar.make(findViewById(R.id.round_recycler_view),
                        R.string.error_adding_round, Snackbar.LENGTH_LONG).show()
                else {
                    Snackbar.make(findViewById(R.id.round_recycler_view),
                        "New " + round.title + " added", Snackbar.LENGTH_LONG).show()
                    val fragmentManager = supportFragmentManager
                    val roundListFragment =
                        fragmentManager.findFragmentById(R.id.fragment_round_list_container)
                                as RoundListFragment
                    roundListFragment.round_recycler_view.update(
                        SettingsActivityC4.getPlayerUUID(baseContext),
                        { round -> onRoundSelected(round) }
                    )
                }
            }
        }
        repository?.addRound(round, callback)
    }



//    /**
//     * Funcion que se ejecuta cuando termina la partida previamente lanzada. Recoge que la ronda
//     * ha terminado correctamente y el id de esta.
//     */
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        when(requestCode){
//            GAME_REQUEST_ID -> {
//                if (resultCode == Activity.RESULT_OK)
//                    onRoundSelected(RoundRepository.getRound(
//                        data?.getStringExtra(EXTRA_ROUND_ID)))
//            }
//
//            GAME_EDITOR_ID -> {
//                if (resultCode == Activity.RESULT_OK){
//                    val rows = data?.getIntExtra(EXTRA_ROUND_ROWS,4)
//                    val columns = data?.getIntExtra(EXTRA_ROUND_COLUMNS, 4)
//                    name = data?.getStringExtra(EXTRA_ROUND_NAME)
//                    RoundRepository.addRound(rows!!,columns!!)
//                }
//            }
//        }
//    }

    /**
     * Funcion que instancia el fragmento de la lista de rondas al crear la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_list)

        val fm : FragmentManager = supportFragmentManager
        fm.beginTransaction().replace(R.id.fragment_round_list_container, RoundListFragment()).commit()
        setSupportActionBar(roundList_toolbar)
    }

    override fun onDestroy() {
        repository?.close()
        super.onDestroy()
    }

    /**
     * Al resumir la actividad ejecuta el metodo onRoundUpdated por si hay que actualizar la UI
     */
//    override fun onResume() {
//        super.onResume()
//        onRoundUpdated()
//    }

    /**
     * Funcion que crea el menu de opciones
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onPreferenceSelected() {
        startActivity(Intent(this, SettingsActivityC4::class.java))
    }

//    override fun onRoundAdded() {
//        startActivityForResult(GameEditorActivity.newIntent(this), GAME_EDITOR_ID)
//        onRoundUpdated()
//    }

    /**
     * objeto usado a modo de estatico para crar un intent de esta actividad pasandole un id de ronda a mostrar.
     */
    companion object {
        val GAME_REQUEST_ID = 1
        val GAME_EDITOR_ID = 2
        val EXTRA_ROUND_ID = "com.sergioteso.conecta4.round_id"
        val EXTRA_ROUND_ROWS = "com.sergioteso.conecta4.rows"
        val EXTRA_ROUND_COLUMNS = "com.sergioteso.conecta4.columns"
        val EXTRA_ROUND_NAME = "com.sergioteso.conecta4.name"
        var name : String? = "Anonymus"

        fun newIntent(context: Context, round_id: String?): Intent {
            val intent = Intent(context, RoundListActivity::class.java)
            intent.putExtra(EXTRA_ROUND_ID, round_id)
            intent.putExtra(EXTRA_ROUND_NAME, name)
            return intent
        }
    }
}