package co.kubo.indiesco.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
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

    lateinit var iEspacios: IEspacios

    var posInmueble = 0
    var posDim = 0
    var pos1 = -1
    var pos2 = -1
    var pos3 = -1

    var rvEspaciosPpal : RecyclerView ?= null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_espacios_principales, container, false)
        iEspacios = (activity as? IEspacios)!!
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
        for (i in inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.indices) {
            if (inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![i].espacio == "1 Piso") {
                pos1 = i
                singleton.priceFloorOne = inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![i].valor!!.toDouble()
                inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.removeAt(i)
                break
            }
        }
        for (i in inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.indices) {
            if (inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![i].espacio == "2 Piso") {
                pos1 = i
                singleton.priceFloorTwo = inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![i].valor!!.toDouble()
                inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.removeAt(i)
                break
            }
        }
        for (i in inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.indices) {
            if (inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![i].espacio == "3 Piso") {
                pos1 = i
                singleton.priceFloorThree = inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![i].valor!!.toDouble()
                inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.removeAt(i)
                break
            }
        }


        adapter = AdapterEspacios(inmuebles, activity!!, posInmueble, posDim, iEspacios)
        rvEspaciosPpal!!.adapter = adapter

        return v
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            iEspacios = (activity as? IEspacios)!!

            inmuebles = singleton.data
            try{
                posInmueble = singleton.posTipoInmueble.toInt()
                posDim = singleton.posDimension.toInt()
            }catch (e: Exception){
                posInmueble = 0
            }

            llm = LinearLayoutManager(activity)
            rvEspaciosPpal!!.layoutManager = llm

            for (i in inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.indices) {
                if (inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![i].espacio == "1 Piso") {
                    pos1 = i
                    inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.removeAt(i)
                    break
                }
            }
            for (i in inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.indices) {
                if (inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![i].espacio == "2 Piso") {
                    pos1 = i
                    inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.removeAt(i)
                    break
                }
            }
            for (i in inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.indices) {
                if (inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![i].espacio == "3 Piso") {
                    pos1 = i
                    inmuebles[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.removeAt(i)
                    break
                }
            }

            adapter = AdapterEspacios(inmuebles, activity!!, posInmueble, posDim, iEspacios)
            rvEspaciosPpal!!.adapter = adapter
        } else {
            Log.e("fragment", "No visible")
        }
    }

}