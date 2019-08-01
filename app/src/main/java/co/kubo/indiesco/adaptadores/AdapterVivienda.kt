package co.kubo.indiesco.adaptadores

import android.app.Activity
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
import com.squareup.picasso.Picasso

/**
 * Created by estacion on 31/05/18.
 */
class AdapterVivienda(private val inmuebleArray: ArrayList<InmuebleVO>, private val activity: Activity,
                      private val iVivieda: IVivieda)
    : RecyclerView.Adapter<AdapterVivienda.ViviendaViewHolder>() {

    val singleton = Singleton.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViviendaViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_tipo_vivienda, parent, false)
        return ViviendaViewHolder(v)
    }

    override fun getItemCount(): Int {
        return inmuebleArray[singleton.posCat.toInt()].tiposInmuebles.size
    }

    override fun onBindViewHolder(holder: ViviendaViewHolder, position: Int) {
        var temp = inmuebleArray[singleton.posCat.toInt()].tiposInmuebles[position]

        holder.tvVivienda.text = temp.inmueble
        Picasso.with(activity)
                .load(temp.imagen)
                .into(holder.imgVivienda)

        if (temp.active) {
            holder.imgCheckHome.visibility = View.VISIBLE
            holder.llItem.setBackgroundColor(activity.resources.getColor(R.color.colorNaranja))
        } else {
            holder.imgCheckHome.visibility = View.INVISIBLE
            holder.llItem.setBackgroundColor(activity.resources.getColor(R.color.colorNaranjaOpaco))
        }

        holder.llItem.setOnClickListener {
            for (item in inmuebleArray[singleton.posCat.toInt()].tiposInmuebles.indices) {
                inmuebleArray[singleton.posCat.toInt()].tiposInmuebles[item].active = false
            }
            inmuebleArray[singleton.posCat.toInt()].tiposInmuebles[position].active = true
            singleton.posTipoInmueble = position.toString()
            singleton.idTipoInmueble = temp.idTipoInmueble
            notifyDataSetChanged()
            iVivieda.viviendaCheck()
        }
    }

    class ViviendaViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var llItem = itemView!!.findViewById<LinearLayout>(R.id.llItem)
        var imgCheckHome = itemView!!.findViewById<ImageView>(R.id.imgCheckHome)
        var imgVivienda = itemView!!.findViewById<ImageView>(R.id.imgVivienda)
        var tvVivienda = itemView!!.findViewById<TextView>(R.id.tvVivienda)
    }
}

interface IVivieda {
    fun viviendaCheck()
}
