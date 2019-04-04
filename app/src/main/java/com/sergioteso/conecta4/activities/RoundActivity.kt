package com.sergioteso.conecta4.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.Fragments.RoundFragment
import com.sergioteso.conecta4.models.Round
import kotlinx.android.synthetic.main.activity_round_list.*

/**
 * clase que implementa la actividad la actividad del juego principal.
 * Para ello simplemente infla el fragmento de RoundFragment que se encarga de toda la logica entre
 * la UI y la logica de negocio.
 */
class RoundActivity : AppCompatActivity(),
    RoundFragment.OnRoundFragmentInteractionListener {


    override fun onRoundUpdated(round: Round) {
    }

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
            fm.beginTransaction().add(R.id.fragment_round_list_container, fragment).commit()
        } else {
//            val returnIntent = Intent()
//            returnIntent.putExtra(RoundListActivity.EXTRA_ROUND_ID, round_id)
//            setResult(Activity.RESULT_OK, returnIntent)
//            finish()
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