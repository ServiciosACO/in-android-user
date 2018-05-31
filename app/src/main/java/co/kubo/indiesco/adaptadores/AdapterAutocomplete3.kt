package co.kubo.indiesco.adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import co.kubo.indiesco.R
import java.util.ArrayList

/**
 * Created by estacion on 30/05/18.
 */
class AdapterAutocomplete3(context: Context, resource: Int, textViewResourceId: Int, private val items: ArrayList<Array<String>>)
    : ArrayAdapter<Array<String>>(context, resource, textViewResourceId, items) {

    private val itemsSugerencias: ArrayList<Array<String>> = ArrayList()
    private val itemsTemp: ArrayList<Array<String>> = ArrayList(items)

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */

    private val nameFilter = object : Filter() {
        override fun convertResultToString(resultValue: Any): CharSequence {
            return (resultValue as Array<String>)[0]
        }

        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            if (constraint != null) {
                itemsSugerencias.clear()
                for (item in itemsTemp) {
                    itemsSugerencias.add(item)
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = itemsSugerencias
                filterResults.count = itemsSugerencias.size
                return filterResults
            } else {
                return Filter.FilterResults()
            }
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            val filterList = results.values as ArrayList<Array<String>>
            if (results.count > 0) {
                clear()
                for (item in filterList) {
                    add(item)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_direccion_google, parent, false)
        }
        val txtMensaje = view!!.findViewById<View>(R.id.txtItemDireccionGoogle) as TextView
        txtMensaje.text = items[position][0]
        return view
    }

    override fun getFilter(): Filter {
        return nameFilter
    }
}