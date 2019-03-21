package com.sergioteso.conecta4.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.Fragments.GameFragment
import com.sergioteso.conecta4.activities.Fragments.RoundListFragment
import com.sergioteso.conecta4.models.Round
import com.sergioteso.conecta4.models.RoundRepository
import kotlinx.android.synthetic.main.activity_round_list.*
import kotlinx.android.synthetic.main.fragment_round_list.*



class RoundListActivity : AppCompatActivity(),
    RoundListFragment.OnFragmentInteractionListener,
    GameFragment.OnRoundFragmentInteractionListener {

    override fun onRoundUpdated() {
        round_recycler_view.adapter?.notifyDataSetChanged()
    }

    override fun onRoundSelected(round: Round) {
        val fm = supportFragmentManager
        if (fragment_game_container != null) {
            val gameFragment = GameFragment.newInstance(round.id)
            fm.beginTransaction().add(R.id.fragment_game_container, gameFragment).commit()
        } else {
            val intento = RoundActivity.newIntentRound(this, round.id)
            startActivityForResult(intento, GAME_REQUEST_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            GAME_REQUEST_ID -> {
                if (resultCode == Activity.RESULT_OK)
                    onRoundSelected(RoundRepository.getRound(
                        data?.getStringExtra(EXTRA_ROUND_ID)))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_list)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_round_list_container, RoundListFragment()).commit()
        setSupportActionBar(roundList_toolbar)
        val round_id = intent.getStringExtra(EXTRA_ROUND_ID)
        if (round_id != null){
            onRoundSelected(RoundRepository.getRound(round_id))
        }
    }

    override fun onResume() {
        super.onResume()
        onRoundUpdated()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onRoundAdded() {
        RoundRepository.addRound()
        onRoundUpdated()
    }

    companion object {
        val GAME_REQUEST_ID = 1
        val EXTRA_ROUND_ID = "com.sergioteso.conecta4.round_id"
        fun newIntent(context: Context, round_id: String?): Intent {
            val intent = Intent(context, RoundListActivity::class.java)
            intent.putExtra(EXTRA_ROUND_ID, round_id)
            return intent
        }
    }
}