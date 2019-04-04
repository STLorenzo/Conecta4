package com.sergioteso.conecta4.activities.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.*

import com.sergioteso.conecta4.R
import com.sergioteso.conecta4.activities.Logger.log
import com.sergioteso.conecta4.activities.SettingsActivityC4
import com.sergioteso.conecta4.activities.update
import com.sergioteso.conecta4.models.Round
import com.sergioteso.conecta4.models.TableroC4
import kotlinx.android.synthetic.main.fragment_round_list.*

/**
 * Clase que modela el fragmento de la lista de rondas.
 */
class RoundListFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    /**
     * Interfaz que modela la interaccion con este fragmento para clases exteriores.
     */
    interface OnFragmentInteractionListener {
        fun onRoundSelected(round: Round)
        fun onPreferenceSelected()
        fun onRoundAdded()
    }

    /**
     * Metodo que al crear la actividad establece que tiene un menu de opciones
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /**
     * Metodo que al crear la vista la infla con su layout correspondiente
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_round_list, container, false)
    }

    /**
     * Al crear la vista establecemos los parametros necesarios para el correcto funcionamiento del
     * RecyclerView usado para lista de rondas.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        round_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            update(SettingsActivityC4.getPlayerUUID(context!!))
                { round -> listener?.onRoundSelected(round) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        round_recycler_view.update(SettingsActivityC4.getPlayerUUID(context!!))
        { round -> listener?.onRoundSelected(round) }
    }

    /**
     *
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    /**
     *
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

//    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater?.inflate(R.menu.menu, menu)
//    }

    /**
     * Metodo que sobreescribe el comportamiento al pulsar en el menu de opciones.
     * En este caso el boton de de añadir ronda a la lista. Añadiendola al repositorio y
     * actualizando la UI de la lista
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_item_new_round -> {
                listener?.onRoundAdded()
                return true
            }
            R.id.menu_item_preferences -> {
                listener?.onPreferenceSelected()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
