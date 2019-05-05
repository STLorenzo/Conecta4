package com.sergioteso.conecta4.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.Fragments.RoundFragment
import com.sergioteso.conecta4.models.Round
import com.sergioteso.conecta4.models.RoundRepository
import com.sergioteso.conecta4.models.RoundRepositoryFactory
import kotlinx.android.synthetic.main.activity_round_list.*

/**
 * clase que implementa la actividad la actividad del juego principal.
 * Para ello simplemente infla el fragmento de RoundFragment que se encarga de toda la logica entre
 * la UI y la logica de negocio.
 */
class RoundActivity : AppCompatActivity(),
    RoundFragment.OnRoundFragmentInteractionListener {

    private var repository: RoundRepository? = null

    override fun onRoundUpdated(round: Round) {
        repository = RoundRepositoryFactory.createRepository(this)
        val callback = object : RoundRepository.BooleanCallback {
            override fun onResponse(response: Boolean) {
                if (response == true) {
                    //Log.d("DEBUG","Response of RoundUpdated succesful")
                } else
                    Toast.makeText(
                        applicationContext,
                        R.string.error_updating_round,
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
        Log.d("DEBUG", "update_roudn_activity")
        repository?.updateRound(round, callback)
    }

//    override fun onDestroy() {
//        repository?.close()
//        super.onDestroy()
//    }

    /**
     * función que sobreescribe el onCreate básico de las Activities en Android
     * Se encarga de isntanciar el fragmento RoundFragment de acorde a la ronda seleccionada
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_list)
        val fm = supportFragmentManager
        val round = intent.getStringExtra(EXTRA_ROUND)
        if (fragment_game_container == null) {
            val fragment = RoundFragment.newInstance(round)
            fm.beginTransaction().replace(R.id.fragment_round_list_container, fragment).commit()
        } else {
            finish()
        }
        // my_toolbar is defined in the layout file
        setSupportActionBar(roundList_toolbar)
        // Enable the Up button from the support ActionBar corresponding to this toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    /**
     * objeto que permite crear intents para iniciar esta actividad de acorde a los parametros que le pasemos
     */
    companion object {
        val EXTRA_ROUND = "com.sergioteso.conecta4.round_id"

        fun newIntentRound(context: Context, round: String): Intent {
            val intent = Intent(context, RoundActivity::class.java)
            intent.putExtra(EXTRA_ROUND, round)
            return intent
        }
    }
}