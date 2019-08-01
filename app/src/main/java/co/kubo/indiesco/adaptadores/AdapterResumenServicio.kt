package co.kubo.indiesco.adaptadores

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.kubo.indiesco.R
import co.kubo.indiesco.modelo.ServiceResumen
import java.text.DecimalFormat

/**
 * Created by estacion on 31/05/18.
 */
class AdapterResumenServicio(private val resumen: ArrayList<ServiceResumen>, private val activity: Activity)
    : RecyclerView.Adapter<AdapterResumenServicio.ResumenServicioViewHolder>() {

    val df = DecimalFormat("$###,###")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResumenServicioViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_resumen, parent, false)
        return ResumenServicioViewHolder(v)
    }

    override fun getItemCount(): Int {
        return resumen.size
    }

    override fun onBindViewHolder(holder: ResumenServicioViewHolder, position: Int) {
        var temp = resumen[position]
        holder.tvTipoVivienda.text = temp.category
        holder.tvFecha.text = temp.date
        holder.tvDir.text = temp.address
        holder.tvDimension.text = temp.dimension
        holder.tvCost.text = "Subtotal: ${df.format(temp.totalCost.toDouble())}"

    }

    class ResumenServicioViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var llResumen = itemView!!.findViewById<LinearLayout>(R.id.llResumen)
        var tvTipoVivienda = itemView!!.findViewById<TextView>(R.id.tvTipoVivienda)
        var tvFecha = itemView!!.findViewById<TextView>(R.id.tvFecha)
        var tvDir = itemView!!.findViewById<TextView>(R.id.tvDir)
        var tvDimension = itemView!!.findViewById<TextView>(R.id.tvDimension)
        var tvCost = itemView!!.findViewById<TextView>(R.id.tvCost)
    }
}