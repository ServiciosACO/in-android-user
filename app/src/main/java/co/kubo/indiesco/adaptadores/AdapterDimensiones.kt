package co.kubo.indiesco.adaptadores

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.kubo.indiesco.R
import co.kubo.indiesco.modelo.InmuebleVO
import co.kubo.indiesco.utils.Singleton

/**
 * Created by estacion on 1/06/18.
 */
class AdapterDimensiones(private val inmuebleArray : ArrayList<InmuebleVO>, private val activity: Activity,
                         private val posInmueble: Int, private val iDimension: IDimension)
    : RecyclerView.Adapter<AdapterDimensiones.DimensionesViewHolder>() {

    val singleton = Singleton.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DimensionesViewHolder {
        val v = LayoutInflater.from(parent!!.context).inflate(R.layout.item_dimensiones, parent, false)
        return DimensionesViewHolder(v)
    }

    override fun getItemCount(): Int {
        return inmuebleArray[0].tiposInmuebles[posInmueble].dimesiones!!.size
    }

    override fun onBindViewHolder(holder: DimensionesViewHolder?, position: Int) {
        var temp = inmuebleArray[0].tiposInmuebles[posInmueble].dimesiones!![position]
        holder!!.tvDimension.text = temp.dimension

        if (temp.checkDim){
            holder.tvDimension.setBackgroundColor(activity.resources.getColor(R.color.colorNaranja))
            holder.tvDimension.setCompoundDrawablesWithIntrinsicBounds(null,null,activity.resources.getDrawable(R.drawable.ic_selected),null)
        } else {
            holder.tvDimension.setBackgroundColor(activity.resources.getColor(R.color.colorNaranjaOpaco))
            holder.tvDimension.setCompoundDrawables(null,null, null,null)
        }

        holder.tvDimension.setOnClickListener{
            for (item in inmuebleArray[0].tiposInmuebles[posInmueble].dimesiones!!.indices){
                inmuebleArray[0].tiposInmuebles[posInmueble].dimesiones!![item].checkDim = false
            }
            inmuebleArray[0].tiposInmuebles[posInmueble].dimesiones!![position].checkDim = true
            singleton.idDimension = temp.idDimension
            singleton.posDimension = position.toString()
            notifyDataSetChanged()
            iDimension.dimensionCheck(2)
        }
    }
    class DimensionesViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        var tvDimension = itemView!!.findViewById<TextView>(R.id.tvDimension)
    }
}

interface IDimension{
    fun dimensionCheck(num: Int)
}