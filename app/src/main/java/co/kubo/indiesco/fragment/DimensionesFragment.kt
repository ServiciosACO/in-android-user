package co.kubo.indiesco.fragment

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.AdapterDimensiones
import co.kubo.indiesco.adaptadores.IDimension
import co.kubo.indiesco.adaptadores.IVivieda
import co.kubo.indiesco.modelo.InmuebleVO
import co.kubo.indiesco.utils.Singleton

/**
 * Created by estacion on 28/05/18.
 */
class DimensionesFragment : Fragment(), View.OnClickListener{

    val singleton = Singleton.getInstance()
    var inmuebles = ArrayList<InmuebleVO>()
    lateinit var llm : LinearLayoutManager
    lateinit var adapter : AdapterDimensiones
    var posInmueble = 0
    var nPisos = 0
    lateinit var iDimension : IDimension

    private var rvDimensiones : RecyclerView ?= null
    private var tvQtyFloor : TextView ?= null

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.imgMinus -> {
                if (inmuebles[posInmueble].getnPisos() < 2){
                    Toast.makeText(activity, "Debe tener mínimo 1 piso", Toast.LENGTH_SHORT).show()
                } else {
                    nPisos = inmuebles[posInmueble].getnPisos()
                    --nPisos
                    tvQtyFloor!!.text = nPisos.toString()
                    inmuebles[posInmueble].setnPisos(nPisos)
                    singleton.setnPisos(nPisos.toString())
                }
                iDimension.dimensionCheck(3)
            }
            R.id.imgPlus -> {
                if (inmuebles[posInmueble].getnPisos() > 2){
                    Toast.makeText(activity, "Debe tener máximo 3 pisos", Toast.LENGTH_SHORT).show()
                } else {
                    nPisos = inmuebles[posInmueble].getnPisos()
                    ++nPisos
                    tvQtyFloor!!.text = nPisos.toString()
                    inmuebles[posInmueble].setnPisos(nPisos)
                    singleton.setnPisos(nPisos.toString())
                }
                iDimension.dimensionCheck(3)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_dimensiones, container, false)
        iDimension = (activity as? IDimension)!!
        var imgMinus = v.findViewById<ImageView>(R.id.imgMinus)
        var imgPlus  = v.findViewById<ImageView>(R.id.imgPlus)
        tvQtyFloor = v.findViewById(R.id.tvQtyFloor)
        rvDimensiones = v.findViewById(R.id.rvDimensiones)

        imgMinus.setOnClickListener(this)
        imgPlus.setOnClickListener(this)

        inmuebles = singleton.data
        llm = LinearLayoutManager(activity)
        rvDimensiones!!.layoutManager = llm
        if (inmuebles[singleton.posCat.toInt()].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!!.size != 0){
            adapter = AdapterDimensiones(inmuebles, activity!!, posInmueble, iDimension!!)
            rvDimensiones!!.adapter = adapter
        }
        Log.e("fragment", "onCreateView")

        return v
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            iDimension = (activity as? IDimension)!!
            Log.e("fragment", "Visible")
            inmuebles = singleton.data
            try{
                posInmueble = singleton.posTipoInmueble.toInt()
            }catch (e: Exception){
                posInmueble = 0
            }

            if (inmuebles[posInmueble].getnPisos() != 0){
                tvQtyFloor!!.text = inmuebles[posInmueble].getnPisos().toString()
            }

            llm = LinearLayoutManager(activity)
            rvDimensiones!!.layoutManager = llm
            if (inmuebles[singleton.posCat.toInt()].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!!.size != 0){
                adapter = AdapterDimensiones(inmuebles, activity!!, posInmueble, iDimension!!)
                rvDimensiones!!.adapter = adapter
            }
        } else {
            Log.e("fragment", "No visible")
        }
    }

}