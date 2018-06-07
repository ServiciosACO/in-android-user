package co.kubo.indiesco.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.AdapterEspacios
import co.kubo.indiesco.adaptadores.IEspacios
import co.kubo.indiesco.modelo.InmuebleVO
import co.kubo.indiesco.utils.Singleton

/**
 * Created by estacion on 28/05/18.
 */
class EspaciosPrincipalesFragment : Fragment() {

    val singleton = Singleton.getInstance()
    var inmuebles = ArrayList<InmuebleVO>()
    lateinit var llm : LinearLayoutManager
    lateinit var adapter : AdapterEspacios

    var posInmueble = 0
    var posDim = 0

    var rvEspaciosPpal : RecyclerView ?= null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_espacios_principales, container, false)

        var iEspacios = (activity as? IEspacios)!!

        try{
            posInmueble = singleton.posTipoInmueble.toInt()
            posDim = singleton.posDimension.toInt()
        }catch (e: Exception){
            posInmueble = 0
        }

        rvEspaciosPpal = v.findViewById(R.id.rvEspaciosPpal)
        inmuebles = singleton.data
        llm = LinearLayoutManager(activity)
        rvEspaciosPpal!!.layoutManager = llm
        adapter = AdapterEspacios(inmuebles, activity!!, posInmueble, posDim, iEspacios)
        rvEspaciosPpal!!.adapter = adapter

        return v
    }

}