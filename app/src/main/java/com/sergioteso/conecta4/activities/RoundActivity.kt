package com.sergioteso.conecta4.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.Fragments.RoundFragment
import kotlinx.android.synthetic.main.activity_round_list.*

/**
 * clase que implementa la actividad la actividad del juego principal.
 * Para ello simplemente infla el fragmento de RoundFragment que se encarga de toda la logica entre
 * la UI y la logica de negocio.
 */
class RoundActivity : AppCompatActivity(),
    RoundFragment.OnRoundFragmentInteractionListener {


    override fun onRoundUpdated() {
    }

    /**
     * función que sobreescribe el onCreate básico de las Activities en Android
     * Se encarga de isntanciar el fragmento RoundFragment de acorde a la ronda seleccionada
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_list)
        val fm = supportFragmentManager
        val round_id = intent.getStringExtra(EXTRA_ROUND_ID)
        var name = intent.getStringExtra(EXTRA_NAME)
        if (name == null)
            name = "Anonymus"
        if (fragment_game_container == null) {
            val fragment = RoundFragment.newInstance(round_id,name)
            fm.beginTransaction().add(R.id.fragment_round_list_container, fragment).commit()
        } else {
            val returnIntent = Intent()
            returnIntent.putExtra(RoundListActivity.EXTRA_ROUND_ID, round_id)
            setResult(Activity.RESULT_OK, returnIntent)
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
        val EXTRA_ROUND_ID = "com.sergioteso.conecta4.round_id"
        val EXTRA_TYPE = "com.sergioteso.conecta4.game_type"
        val EXTRA_COLUMNS = "com.sergioteso.conecta4.columns"
        val EXTRA_ROWS = "com.sergioteso.conecta4.rows"
        val EXTRA_NAME = "com.sergioteso.conecta4.name"

        fun newIntentRound(context: Context, round_id: String): Intent {
            val intent = Intent(context, RoundActivity::class.java)
            intent.putExtra(EXTRA_ROUND_ID, round_id)
            intent.putExtra(EXTRA_TYPE, "roundlist")
            return intent
        }

        fun newIntentEditor(context: Context, columns: Int, rows: Int, name: String): Intent {
            val intent = Intent(context, RoundActivity::class.java)
            intent.putExtra(EXTRA_COLUMNS, columns)
            intent.putExtra(EXTRA_ROWS, rows)
            intent.putExtra(EXTRA_NAME, name)
            intent.putExtra(EXTRA_TYPE, "editor")
            return intent
        }
    }
}