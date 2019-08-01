package co.kubo.indiesco.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import co.kubo.indiesco.R;

/**
 * Created by estacion on 26/02/18.
 */

public class AdapterAutocomplete extends ArrayAdapter<String[]> {

    private Context context;
    private ArrayList<String[]> items, itemsSugerencias, itemsTemp;

    public AdapterAutocomplete(Context context, int resource, int textViewResourceId, ArrayList<String[]> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.items = items;
        itemsTemp = new ArrayList<>(items);
        itemsSugerencias = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_direccion_google, parent, false);
        }
        TextView txtMensaje = (TextView) view.findViewById(R.id.txtItemDireccionGoogle);
        txtMensaje.setText(items.get(position)[0]);

        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

/**
 * Custom Filter implementation for custom suggestions we provide.
 */

    private Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((String[]) resultValue)[0];
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                itemsSugerencias.clear();
                for (String[] item : itemsTemp) {
                    itemsSugerencias.add(item);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemsSugerencias;
                filterResults.count = itemsSugerencias.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
           /* ArrayList<String[]> filterList = (ArrayList<String[]>) results.values;
            if (results.count > 0) {
                clear();
                for (String[] item : filterList) {
                    add(item);
                    notifyDataSetChanged();
                }
            }*/

            ArrayList<String[]> filterList = (ArrayList<String[]>) results.values;
            if (results.count > 0) {
                Log.println(Log.INFO, "Results", "FOUND");
                filterList.clear();
                filterList.addAll((ArrayList<String[]>) results.values);
                notifyDataSetChanged();
            } else {
                Log.println(Log.INFO, "Results", "-");
                notifyDataSetInvalidated();
            }
        }
    };
}
