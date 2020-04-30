package co.kubo.indiesco.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.kubo.indiesco.R
import co.kubo.indiesco.restAPI.modelo.ResponseTipoServicio
import com.squareup.picasso.Picasso

class TiposServiciosAdapter(
        private val data: List<ResponseTipoServicio.TipoServicio>,
        private val context: Context,
        private val onCheckClickListener: OnCheckClickListener)
    : RecyclerView.Adapter<TiposServiciosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tipo_servicio, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    private fun limpiarSeleccion() {
        data.forEach { tipoServicio ->
            tipoServicio.isSeleccionado = false
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val checkTipoServicio = itemView.findViewById<CheckBox>(R.id.checkTipoServicio)
        private val imgTipoServicio = itemView.findViewById<ImageView>(R.id.imgTipoServicio)
        private val txtTipoServicio = itemView.findViewById<TextView>(R.id.txtTipoServicio)

        fun bind(tipoServicio: ResponseTipoServicio.TipoServicio) {
            val urlImage = "http://apibackoffice.serviciosaco.com/imgs_recursos/${tipoServicio.imagen}"
            txtTipoServicio.text = tipoServicio.servicio
            Picasso.with(context).load(urlImage).into(imgTipoServicio)
            checkTipoServicio.isChecked = tipoServicio.isSeleccionado
            checkTipoServicio.setOnClickListener {
                val nuevaSeleccion = !tipoServicio.isSeleccionado
                limpiarSeleccion()
                tipoServicio.isSeleccionado = nuevaSeleccion
                notifyDataSetChanged()
                onCheckClickListener.onCheck(tipoServicio)
            }
        }
    }

    interface OnCheckClickListener {
        fun onCheck(tipoServicio: ResponseTipoServicio.TipoServicio)
    }
}