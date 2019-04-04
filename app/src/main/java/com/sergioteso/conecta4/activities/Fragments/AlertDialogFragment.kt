package com.sergioteso.conecta4.activities.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.RoundActivity
import com.sergioteso.conecta4.activities.RoundListActivity
import com.sergioteso.conecta4.models.RoundRepository

/**
 * Clase que modela una simple ventana de dialogo que permite crear una nueva partida.
 * Esta se muestra cuando ocurre un GameOver
 */
class AlertDialogFragment : DialogFragment() {

    /**
     * Metodo sobrescrito que modela la creacción de esta ventana de dialogo.
     * Muestra un mensaje de GameOver y dos botones que permiten crear o no una nueva partida.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity as AppCompatActivity?
        val alertDialogBuilder = AlertDialog.Builder(getActivity())
        alertDialogBuilder.apply {
            setTitle(R.string.game_over)
            setMessage(R.string.game_over_message)
            setPositiveButton(R.string.yes) { dialog, _ ->
                if (activity is RoundListActivity)
                    activity.onRoundAdded()
                    //activity.onRoundUpdated()
                else
                    activity?.finish()
                dialog.dismiss()
            }

            setNegativeButton(R.string.no,
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        if (activity is RoundActivity)
                            activity.finish()
                        dialog?.dismiss()
                    }
                })
        }
        return alertDialogBuilder.create()
    }
}
