package com.sergioteso.conecta4.activities

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.Fragments.RoundListFragment
import com.sergioteso.conecta4.models.RoundRepository
import kotlinx.android.synthetic.main.activity_round_list.*

class RoundListActivity : AppCompatActivity(), RoundListFragment.OnFragmentInteractionListener{
    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_list)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_round_list_container,RoundListFragment()).commit()
    }


}