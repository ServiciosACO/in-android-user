package co.kubo.indiesco.adaptadores

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import co.kubo.indiesco.R
import co.kubo.indiesco.modelo.InmuebleVO
import com.squareup.picasso.Picasso

/**
 * Created by estacion on 31/05/18.
 */
class AdapterVivienda(private val inmuebleArray : ArrayList<InmuebleVO>, private val activity: Activity)
    :RecyclerView.Adapter<AdapterVivienda.ViviendaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViviendaViewHolder {
        val v = LayoutInflater.from(parent!!.context).inflate(R.layout.item_tipo_vivienda, parent, false)
        return ViviendaViewHolder(v)
    }

    override fun getItemCount(): Int {
        return inmuebleArray[0].tiposInmuebles.size
    }

    override fun onBindViewHolder(holder: ViviendaViewHolder?, position: Int) {
        var temp = inmuebleArray[0].tiposInmuebles[position]

        holder!!.tvVivienda.text = temp.inmueble
        Picasso.with(activity)
                .load(temp.imagen)
                .into(holder.imgVivienda)

        if (temp.active){
            holder.imgCheckHome.visibility = View.VISIBLE
            holder.llItem.setBackgroundColor(activity.resources.getColor(R.color.colorNaranja))
        } else {
            holder.imgCheckHome.visibility = View.INVISIBLE
            holder.llItem.setBackgroundColor(activity.resources.getColor(R.color.colorNaranjaOpaco))
        }

        holder.llItem.setOnClickListener{
            for (item in inmuebleArray[0].tiposInmuebles.indices){
                inmuebleArray[0].tiposInmuebles[item].active = false
            }
            inmuebleArray[0].tiposInmuebles[position].active = true
            notifyDataSetChanged()
        }

    }

    class ViviendaViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        var llItem = itemView!!.findViewById<LinearLayout>(R.id.llItem)
        var imgCheckHome = itemView!!.findViewById<ImageView>(R.id.imgCheckHome)
        var imgVivienda = itemView!!.findViewById<ImageView>(R.id.imgVivienda)
        var tvVivienda = itemView!!.findViewById<TextView>(R.id.tvVivienda)
    }
}
