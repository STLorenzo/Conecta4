package com.sergioteso.conecta4.activities.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.*

import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.update
import com.sergioteso.conecta4.models.Round
import kotlinx.android.synthetic.main.fragment_round_list.*

class RoundListFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    interface OnFragmentInteractionListener {
        fun onRoundSelected(round: Round)
        fun onRoundAdded()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_round_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        round_recycler_view.apply {
            layoutManager = LinearLayoutManager(view.context)
            itemAnimator = DefaultItemAnimator()
            update { round -> listener?.onRoundSelected(round) }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

//    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater?.inflate(R.menu.menu, menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_new_round -> {
                listener?.onRoundAdded()
                round_recycler_view.update { round ->
                    listener?.onRoundSelected(round)
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
