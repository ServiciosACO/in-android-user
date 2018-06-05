package co.kubo.indiesco.adaptadores

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import co.kubo.indiesco.R
import co.kubo.indiesco.modelo.InmuebleVO
import co.kubo.indiesco.utils.Singleton

/**
 * Created by estacion on 1/06/18.
 */
class AdapterEspacios(private val inmuebleArray : ArrayList<InmuebleVO>, private val activity: Activity,
                      private val posInmueble: Int, private val posDim: Int)
    : RecyclerView.Adapter<AdapterEspacios.EspaciosViewHolder>() {

    val singleton = Singleton.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EspaciosViewHolder {
        val v = LayoutInflater.from(parent!!.context).inflate(R.layout.item_espacios, parent, false)
        return EspaciosViewHolder(v)
    }

    override fun getItemCount(): Int {
        return inmuebleArray[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.size
    }

    override fun onBindViewHolder(holder: EspaciosViewHolder?, position: Int) {
        var temp = inmuebleArray[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position]
        holder!!.tvEspacios.text = temp.espacio
        holder!!.tvQty.text = temp.qty.toString()

        holder!!.imgPlus0.setOnClickListener{
            var data = singleton.data
            var qty = data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].qty
            ++qty
            holder!!.tvQty.text = qty.toString()
            data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].qty = qty
        }

        holder!!.imgMinus0.setOnClickListener{
            var data = singleton.data
            var qty = data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].qty
            if (qty <= 0){
                holder!!.tvQty.text = qty.toString()
                data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].qty = qty
            } else {
                --qty
                holder!!.tvQty.text = qty.toString()
                data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].qty = qty
            }
        }
    }
    class EspaciosViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        var tvEspacios = itemView!!.findViewById<TextView>(R.id.tvEspacios)
        var imgMinus0 = itemView!!.findViewById<ImageView>(R.id.imgMinus0)
        var tvQty = itemView!!.findViewById<TextView>(R.id.tvQty)
        var imgPlus0 = itemView!!.findViewById<ImageView>(R.id.imgPlus0)
    }
}