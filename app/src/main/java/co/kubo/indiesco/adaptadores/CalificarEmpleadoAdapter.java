package co.kubo.indiesco.adaptadores;

import android.content.Context;
import android.graphics.Color;
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

public class CalificarEmpleadoAdapter extends ArrayAdapter<Personal> {

    private LayoutInflater inflate;
    private List<Personal> listSolcitudes;
    ActivityCalificarPersonal context;


    public CalificarEmpleadoAdapter(ActivityCalificarPersonal context, List<Personal> listSolcitudes) {
        super(context, R.layout.item_calificar_personal, listSolcitudes);
        inflate = LayoutInflater.from(context);
        this.listSolcitudes = listSolcitudes;
        this.context = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflate.inflate(R.layout.item_calificar_personal, parent, false);

        TextView txtNombreEmpladoCalificar = (TextView) convertView.findViewById(R.id.txtNombreEmpladoCalificar);
        ImageView imgEmpleado = (ImageView) convertView.findViewById(R.id.imgEmpleado);
        RatingBar ratingBarCalPer = (RatingBar) convertView.findViewById(R.id.ratingBarCalPer);

        txtNombreEmpladoCalificar.setText(getItem(position).getNombre());
        Picasso.with(context).load(getItem(position).getFoto()).transform(new RoundedCornersTransformation(50,0, RoundedCornersTransformation.CornerType.ALL)).into(imgEmpleado);

        ratingBarCalPer.setRating(Float.parseFloat(getItem(position).getCalificacion()));

        ratingBarCalPer.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                if (rating != 0){
                  getItem(position).setCalificacion(rating+"");
                  getItem(position).setCalificado(true);
                }else{
                    getItem(position).setCalificacion("1");
                    getItem(position).setCalificado(true);
                }
                context.validarCalificados();
            }
        });

        return convertView;
    }

}