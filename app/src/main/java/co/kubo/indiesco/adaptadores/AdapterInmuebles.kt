package co.kubo.indiesco.adaptadores

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import co.kubo.indiesco.R
import co.kubo.indiesco.activities.CircleTransform
import co.kubo.indiesco.modelo.InmuebleVO
import co.kubo.indiesco.utils.Singleton
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

/**
 * Created by estacion on 24/05/18.
 */
class AdapterInmuebles(private val inmuebleArray : ArrayList<InmuebleVO>, private val activity: Activity,
                       private val iShowOption: IShowOption)
    : RecyclerView.Adapter<AdapterInmuebles.InmuebleViewHolder>() {

    var pos = 0
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): InmuebleViewHolder {
        val v = LayoutInflater.from(parent!!.context).inflate(R.layout.item_inmueble, parent, false)
        return InmuebleViewHolder(v)
    }

    override fun onBindViewHolder(holder: InmuebleViewHolder?, position: Int) {
        var temp = inmuebleArray[position]
        holder!!.tvInmueble.text = temp.categoria
        Picasso.with(activity)
                .load(temp.imagen)
                .into(holder.imgProperty)

        holder.radioButton.isChecked = temp.check
        holder.llItemInmueble.setOnClickListener{
            for (item in inmuebleArray.indices){
                inmuebleArray[item].check = false
            }
            notifyDataSetChanged()
            temp.check = true
            if (temp.categoria == "Vivienda"){
                iShowOption.option(0, position)
            } else {
                iShowOption.option(1, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return inmuebleArray.size
    }

    class InmuebleViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        var tvInmueble = itemView!!.findViewById<TextView>(R.id.tvInmueble)
        var radioButton = itemView!!.findViewById<RadioButton>(R.id.radioButton)
        var imgProperty = itemView!!.findViewById<ImageView>(R.id.imgProperty)
        var llItemInmueble = itemView!!.findViewById<LinearLayout>(R.id.llItemInmueble)
    }
}

interface IShowOption{
    fun option(flag: Int, pos : Int)
}