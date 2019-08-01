package co.kubo.indiesco.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.AdapterVivienda
import co.kubo.indiesco.adaptadores.IVivieda

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

        var iVivieda = (activity as? IVivieda)

        inmuebles = singleton.data
        var rvTipoVivienda = v.findViewById<RecyclerView>(R.id.rvTipoVivienda)
        var tvTipo = v.findViewById<TextView>(R.id.tvTipo)
        tvTipo.text = "Tipo de ${singleton.categoria}"

        glm = GridLayoutManager(activity, 2)
        rvTipoVivienda.layoutManager = glm
        adapter = AdapterVivienda(inmuebles, activity!!, iVivieda!!)
        rvTipoVivienda.adapter = adapter
        return v
    }

}