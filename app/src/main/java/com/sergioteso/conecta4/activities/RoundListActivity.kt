package com.sergioteso.conecta4.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.Fragments.RoundFragment
import com.sergioteso.conecta4.activities.Fragments.RoundListFragment
import com.sergioteso.conecta4.firebase.FRDataBase
import com.sergioteso.conecta4.models.Round
import com.sergioteso.conecta4.models.RoundRepository
import com.sergioteso.conecta4.models.RoundRepositoryFactory
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
                    round_recycler_view.update(
                        SettingsActivityC4.getPlayerUUID(baseContext),
                        { round -> onRoundSelected(round) }
                    )
                } else
                    Toast.makeText(
                        applicationContext,
                        R.string.error_updating_round,
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
        Log.d("DEBUG", "update_roudn_list")
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
        val rows = SettingsActivityC4.getRows(this)
        val columns = SettingsActivityC4.getColumns(this)

        val callback = object : RoundRepository.BooleanCallback {
            override fun onResponse(response: Boolean) {
                if (response == false)
                    Snackbar.make(
                        findViewById(R.id.round_recycler_view),
                        R.string.error_adding_round, Snackbar.LENGTH_LONG
                    ).show()
                else {
                    Snackbar.make(
                        findViewById(R.id.round_recycler_view),
                        "New round added", Snackbar.LENGTH_LONG
                    ).show()
                    val fragmentManager = supportFragmentManager
                    val roundListFragment =
                        fragmentManager.findFragmentById(R.id.fragment_round_list_container)
                                as RoundListFragment
                    roundListFragment.round_recycler_view.update(
                        SettingsActivityC4.getPlayerUUID(applicationContext),
                        { round -> onRoundSelected(round) }
                    )
                }
            }
        }
        repository = RoundRepositoryFactory.createRepository(this)
        repository?.createRound(rows, columns, this, callback)
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
//        }
//    }

    /**
     * Funcion que instancia el fragmento de la lista de rondas al crear la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_list)

        val fm: FragmentManager = supportFragmentManager
        fm.beginTransaction().replace(R.id.fragment_round_list_container, RoundListFragment()).commit()
        setSupportActionBar(roundList_toolbar)
    }

    override fun onDestroy() {
        repository?.close()
        super.onDestroy()
    }

    override fun onStart() {
        val callback = object : RoundRepository.RoundsCallback {
            override fun onResponse(rounds: List<Round>) {
                for (round in rounds) {
                    round_recycler_view.update(
                        SettingsActivityC4.getPlayerUUID(baseContext),
                        { round -> onRoundSelected(round) }
                    )
                }
            }

            override fun onError(error: String) {
                Toast.makeText(applicationContext, "Error on Start", Toast.LENGTH_SHORT).show()
            }
        }
        if (!RoundRepositoryFactory.LOCAL) FRDataBase(this).startListeningChanges(callback)
        super.onStart()
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
        var name: String? = "Anonymus"

        fun newIntent(context: Context): Intent {
            return Intent(context, RoundListActivity::class.java)
        }
    }
}