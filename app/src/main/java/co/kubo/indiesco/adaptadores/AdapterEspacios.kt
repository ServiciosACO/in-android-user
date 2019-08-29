package co.kubo.indiesco.adaptadores

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.kubo.indiesco.R
import co.kubo.indiesco.modelo.InmuebleVO
import co.kubo.indiesco.utils.Singleton

/**
 * Created by estacion on 1/06/18.
 */
class AdapterEspacios(private val inmuebleArray: ArrayList<InmuebleVO>, private val activity: Activity,
                      private val posInmueble: Int, private val posDim: Int, private val iEspacios: IEspacios)
    : RecyclerView.Adapter<AdapterEspacios.EspaciosViewHolder>() {

    val singleton = Singleton.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspaciosViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_espacios, parent, false)
        return EspaciosViewHolder(v)
    }

    override fun getItemCount(): Int {
        return inmuebleArray[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.size
    }

    override fun onBindViewHolder(holder: EspaciosViewHolder, position: Int) {
        var temp = inmuebleArray[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position]
        holder.tvEspacios.text = temp.espacio
        holder.tvQty.text = temp.qty.toString()

        /*when(temp.espacio) {
            "1 Piso" -> {
                holder.imgPlus0.isEnabled = false
                holder.imgMinus0.isEnabled = false
                if (singleton.getnPisos() == 1.toString()){
                    holder.tvQty.text = "1"
                }
            }
            "2 Piso" -> {
                holder.imgPlus0.isEnabled = false
                holder.imgMinus0.isEnabled = false
                if (singleton.getnPisos() == 2.toString()){
                    holder.tvQty.text = "1"
                }
            }
            "3 Piso" -> {
                holder.imgPlus0.isEnabled = false
                holder.imgMinus0.isEnabled = false
                if (singleton.getnPisos() == 3.toString()){
                    holder.tvQty.text = "1"
                }
            }
            else -> {
                holder.llItem.visibility = View.VISIBLE
            }
        }*/

        holder!!.imgPlus0.setOnClickListener {

            var data = singleton.data
            if (data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].espacio == "Terraza"
                    && data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].qty == 1) {
            }
            else if (data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].espacio == "Balcon"
                    && data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].qty == 1) {

            }
            else if (data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].espacio == "Patio"
                    && data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].qty == 1) {

            } else {
                var qty = data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].qty
                ++qty
                holder!!.tvQty.text = qty.toString()
                data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].qty = qty
                //Para colocar en verde la barra de progreso
                var flag = false
                for (item in data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.indices) {
                    if (data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![item].qty != 0) {
                        flag = true
                    }
                    if (flag) {
                        iEspacios.espaciosCheck(true, posInmueble, posDim)
                    } else {
                        iEspacios.espaciosCheck(false, posInmueble, posDim)
                    }
                }
            }
        }

        holder!!.imgMinus0.setOnClickListener {
            var data = singleton.data
            var qty = data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].qty
            if (qty <= 0) {
                holder!!.tvQty.text = qty.toString()
                data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].qty = qty
            } else {
                --qty
                holder!!.tvQty.text = qty.toString()
                data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![position].qty = qty
            }
            //Para colocar en verde la barra de progreso
            var flag = false
            for (item in data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!!.indices) {
                if (data[0].tiposInmuebles[posInmueble].dimesiones!![posDim].espacios!![item].qty != 0) {
                    flag = true
                }
                if (flag) {
                    iEspacios.espaciosCheck(true, posInmueble, posDim)
                } else {
                    iEspacios.espaciosCheck(false, posInmueble, posDim)
                }
            }
        }
    }

    class EspaciosViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var llItem = itemView!!.findViewById<LinearLayout>(R.id.llItem)
        var tvEspacios = itemView!!.findViewById<TextView>(R.id.tvEspacios)
        var imgMinus0 = itemView!!.findViewById<ImageView>(R.id.imgMinus0)
        var tvQty = itemView!!.findViewById<TextView>(R.id.tvQty)
        var imgPlus0 = itemView!!.findViewById<ImageView>(R.id.imgPlus0)
    }
}

interface IEspacios {
    fun espaciosCheck(flag: Boolean, posInm: Int, posDim: Int)
}