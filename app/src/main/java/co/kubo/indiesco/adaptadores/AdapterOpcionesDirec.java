package co.kubo.indiesco.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import co.kubo.indiesco.R;
import co.kubo.indiesco.modelo.direccionesGoogleVO;

/**
 * Created by estacion on 23/02/18.
 */

public class AdapterOpcionesDirec extends ArrayAdapter<direccionesGoogleVO> {

    private List<direccionesGoogleVO> lista;
    private LayoutInflater inflater;
    private Context contexto;

    public AdapterOpcionesDirec(Context context, List<direccionesGoogleVO> lista) {
        super(context, R.layout.item_palabra, lista);
        this.lista = lista;
        inflater = LayoutInflater.from(context);
        contexto = context;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_palabra, parent, false);
        }

        TextView nombreDir = (TextView) convertView.findViewById(R.id.nombreDir);
        TextView ciudadDur = (TextView) convertView.findViewById(R.id.ciudadDur);
        nombreDir.setText(getItem(posicion).getDescription());
        ciudadDur.setText(getItem(posicion).getCiudad());
        return convertView;
    }



}