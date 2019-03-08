package com.sergioteso.conecta4.activities.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.update
import com.sergioteso.conecta4.models.Round
import kotlinx.android.synthetic.main.fragment_round_list.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RoundListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RoundListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RoundListFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onRoundSelected(round: Round)
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
            update {round -> listener?.onRoundSelected(round)}
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
}
