package co.kubo.indiesco.adaptadores

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.kubo.indiesco.R
import co.kubo.indiesco.modelo.InmuebleVO
import co.kubo.indiesco.utils.Singleton
import com.squareup.picasso.Picasso

/**
 * Created by estacion on 24/05/18.
 */
class AdapterInmuebles(private val inmuebleArray : ArrayList<InmuebleVO>, private val activity: Activity,
                       private val iShowOption: IShowOption)
    : RecyclerView.Adapter<AdapterInmuebles.InmuebleViewHolder>() {

    var pos = 0
    var singleton = Singleton.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InmuebleViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_inmueble, parent, false)
        return InmuebleViewHolder(v)
    }

    override fun onBindViewHolder(holder: InmuebleViewHolder, position: Int) {
        var temp = inmuebleArray[position]
        holder.tvInmueble.text = temp.categoria
        holder.tvDescription1.text = temp.description
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
            singleton.categoria = inmuebleArray[position].categoria
            singleton.idCategoria = inmuebleArray[position].idCategoria
            singleton.posCat = position.toString()
            singleton.posTipoInmueble = "0"
            singleton.posDimension = "0"
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

    class InmuebleViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        var tvInmueble = itemView!!.findViewById<TextView>(R.id.tvInmueble)
        var radioButton = itemView!!.findViewById<RadioButton>(R.id.radioButton)
        var imgProperty = itemView!!.findViewById<ImageView>(R.id.imgProperty)
        var llItemInmueble = itemView!!.findViewById<RelativeLayout>(R.id.llItemInmueble)
        var tvDescription1 = itemView!!.findViewById<TextView>(R.id.tvDescription1)

    }
}

interface IShowOption{
    fun option(flag: Int, pos : Int)
}