package co.kubo.indiesco.adaptadores

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import co.kubo.indiesco.R
import co.kubo.indiesco.modelo.Inmueble

/**
 * Created by estacion on 24/05/18.
 */
class AdapterInmuebles(private val inmuebleArray : ArrayList<Inmueble>, private val activity: Activity,
                       private val iShowOption: IShowOption)
    : RecyclerView.Adapter<AdapterInmuebles.InmuebleViewHolder>() {

    var flag = 0
    var flag2 = false
    var pos = 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): InmuebleViewHolder {
        val v = LayoutInflater.from(parent!!.context).inflate(R.layout.item_inmueble, parent, false)
        return InmuebleViewHolder(v)
    }

    override fun onBindViewHolder(holder: InmuebleViewHolder?, position: Int) {
        var temp = inmuebleArray[position]
        holder!!.tvInmueble.text = temp.inmueble



        holder.imgSelectInmueble.setOnClickListener{
            holder.imgSelectInmueble.setImageResource(R.drawable.ayuda_scroll)
            if (temp.inmueble == "vivienda"){
                flag = 0
                iShowOption.option(flag)
            } else {
                flag = 1
                iShowOption.option(flag)
            }
        }
    }

    override fun getItemCount(): Int {
        return inmuebleArray.size
    }

    class InmuebleViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        var tvInmueble = itemView!!.findViewById<TextView>(R.id.tvInmueble)
        var imgSelectInmueble = itemView!!.findViewById<ImageView>(R.id.imgSelectInmueble)
    }
}

interface IShowOption{
    fun option(flag: Int)
}