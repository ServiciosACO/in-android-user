package co.kubo.indiesco.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.AdapterVivienda

import co.kubo.indiesco.modelo.InmuebleVO
import co.kubo.indiesco.utils.Singleton
import java.util.ArrayList

/**
 * Created by estacion on 28/05/18.
 */
class TipoViviendaFragment : Fragment(), View.OnClickListener {

    val singleton = Singleton.getInstance()
    var inmuebles = ArrayList<InmuebleVO>()
    lateinit var glm : GridLayoutManager
    lateinit var adapter : AdapterVivienda

    override fun onClick(v: View?) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_tipo_vivienda, container, false)

        inmuebles = singleton.data
        var rvTipoVivienda = v.findViewById<RecyclerView>(R.id.rvTipoVivienda)

        glm = GridLayoutManager(activity, 2)
        rvTipoVivienda.layoutManager = glm
        adapter = AdapterVivienda(inmuebles, activity!!)
        rvTipoVivienda.adapter = adapter
        return v
    }

}