package co.kubo.indiesco.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.ActivityCalificarPersonal;
import co.kubo.indiesco.modelo.Personal;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class HistorialEmpleadoAdapter extends ArrayAdapter<Personal> {

    private LayoutInflater inflate;
    private List<Personal> listSolcitudes;
    Context context;


    public HistorialEmpleadoAdapter(Context context, List<Personal> listSolcitudes) {
        super(context, R.layout.item_historial_empleado, listSolcitudes);
        inflate = LayoutInflater.from(context);
        this.listSolcitudes = listSolcitudes;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflate.inflate(R.layout.item_historial_empleado, parent, false);

        TextView txtNombreEmpladoCalificarHist = (TextView) convertView.findViewById(R.id.txtNombreEmpladoCalificarHist);
        TextView txtCalificacionHist = (TextView) convertView.findViewById(R.id.txtCalificacionHist);
        ImageView imgEmpleadoHist = (ImageView) convertView.findViewById(R.id.imgEmpleadoHist);


        txtNombreEmpladoCalificarHist.setText(getItem(position).getNombre());
        txtCalificacionHist.setText(getItem(position).getCalificacion());
        Picasso.with(context).load(getItem(position).getFoto()).transform(new RoundedCornersTransformation(50,0, RoundedCornersTransformation.CornerType.ALL)).into(imgEmpleadoHist);



        return convertView;
    }

}
