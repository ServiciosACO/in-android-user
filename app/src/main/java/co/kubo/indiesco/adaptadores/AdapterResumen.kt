package co.kubo.indiesco.adaptadores

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.kubo.indiesco.modelo.ServiceResumen
import co.kubo.indiesco.R
import co.kubo.indiesco.activities.FechaServicio
import co.kubo.indiesco.dialog.DialogDosOpciones
import co.kubo.indiesco.utils.Singleton
import java.text.DecimalFormat


/**
 * Created by estacion on 8/06/18.
 */
class AdapterResumen(private val mList : ArrayList<ServiceResumen>,
                     private val activity: Activity, private val iChangeLayout: IChangeLayout)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private val TYPE_FOOTER = 2
    val df = DecimalFormat("$###,###")

    val singleton = Singleton.getInstance()

    override fun getItemViewType(position: Int): Int {
        if (isPositionHeader(position)) {
            return TYPE_HEADER
        } /*else if (isPositionFooter(position)) {
            return TYPE_FOOTER
        }*/
        return TYPE_ITEM
    }

    private fun isPositionHeader(position: Int) : Boolean {
        return position == 0
    }

    /*private fun isPositionFooter(position: Int) : Boolean {
        return position >= mList.size
        //return position > mList.size //if it have header the operator is >
    }*/

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) { //changing UI from footer to header
            var view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_button_footer, viewGroup, false)
            return HeaderViewHolder(view)

        } else if (viewType == TYPE_ITEM) {
            var view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_resumen, viewGroup, false)
            return ItemViewHolder(view)

        } /*else if (viewType == TYPE_FOOTER) {
            var view = LayoutInflater.from(viewGroup!!.context).inflate(R.layout.item_button_footer, viewGroup, false)
            return FooterViewHolder(view)
        }*/
        throw RuntimeException("there is no type that matches the type $viewType + make sure your using types correctly")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.tvAddService.setOnClickListener {
                val intent = Intent(activity, FechaServicio::class.java)
                activity.startActivity(intent)
            }
        }else if (holder is ItemViewHolder) {
            // Your code here
            var temp = mList[position-1]
            holder.tvTipoVivienda.text   = temp.category
            holder.tvFecha.text          = temp.date
            holder.tvDir.text            = temp.address
            holder.tvDimension.text      = temp.dimension
            holder.tvCost.text           = "Valor servicio: ${df.format(temp.totalCost.toDouble())}"

            holder.imgRemove.setOnClickListener{
                DialogDosOpciones(activity,"8", object : DialogDosOpciones.RespuestaListener{
                    override fun onCancelar() {
                    }
                    override fun onAceptar() {
                        mList.removeAt(position-1)
                        notifyDataSetChanged()
                        var pos = singleton.position
                        singleton.position = pos - 1
                        if (mList.size == 0){
                            iChangeLayout.changeForNoService(true, mList)
                        } else {
                            iChangeLayout.changeForNoService(false, mList)
                        }
                    }
                    override fun onSalir() {
                    }
                }).show()
            }

        }/*else if (holder is FooterViewHolder) {
            //your code here
            holder.tvAddService.setOnClickListener{
                val intent = Intent (activity, FechaServicio :: class.java)
                activity.startActivity(intent)
            }
        }*/
    }

    override fun getItemCount(): Int {
        //return mList.size + 2 //plus 2 if it have header and footer
        return mList.size + 1 //plus 1 because it have only footer
    }

    // The ViewHolders for Header, Item and Footer
    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAddService = itemView!!.findViewById<TextView>(R.id.tvAddService)!!
    }

    class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        // Add your UI Components here
        val llResumen       = itemView!!.findViewById<LinearLayout>(R.id.llResumen)
        val tvTipoVivienda  = itemView!!.findViewById<TextView>(R.id.tvTipoVivienda)
        val tvFecha         = itemView!!.findViewById<TextView>(R.id.tvFecha)
        val tvDir           = itemView!!.findViewById<TextView>(R.id.tvDir)
        val tvDimension     = itemView!!.findViewById<TextView>(R.id.tvDimension)
        val tvCost          = itemView!!.findViewById<TextView>(R.id.tvCost)
        val imgRemove       = itemView!!.findViewById<ImageView>(R.id.imgRemove)
    }

    /*class FooterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        // Add your UI Components here
        val tvAddService = itemView!!.findViewById<TextView>(R.id.tvAddService)!!
    }*/
}
 interface IChangeLayout{
     fun changeForNoService(b: Boolean, mList: ArrayList<ServiceResumen>)
 }