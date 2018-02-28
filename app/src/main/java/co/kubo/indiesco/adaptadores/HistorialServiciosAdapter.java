package co.kubo.indiesco.adaptadores;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.Calificar;
import co.kubo.indiesco.dialog.DialogDetalleHistorial;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.utils.Utils;
import okhttp3.internal.Util;

/**
 * Created by Diego on 12/02/2018.
 */

public class HistorialServiciosAdapter extends RecyclerView.Adapter<HistorialServiciosAdapter.HistorialServiciosViewHolder> {

    private ArrayList<Historial> historials;
    Activity activity;
    Utils utils = new Utils();

    public HistorialServiciosAdapter(ArrayList<Historial> historials, Activity activity) {
        this.historials = historials;
        this.activity = activity;
    }

    @Override
    public HistorialServiciosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        return new HistorialServiciosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HistorialServiciosViewHolder holder, int position) {
        final Historial hist = historials.get(position);
        holder.tvNoServicioHist.setText(String.valueOf(hist.getId_solicitud()));
        holder.tvFechaServicioHist.setText(String.valueOf(utils.StringToDate2(hist.getFecha_transaccion())).replace(" ", "/"));
        holder.tvDirServicioHist.setText(String.valueOf(hist.getDireccion()));
        DecimalFormat formateador = new DecimalFormat("###,###");
        holder.tvPrecioServicio.setText(formateador.format(Double.parseDouble(String.valueOf(hist.getValor()))) + " COP");

        if (hist.getCalificado().equals("si")){
            holder.tvCalificar.setVisibility(View.VISIBLE);
            holder.ratingBarHist.setVisibility(View.GONE);
        }else{
            holder.tvCalificar.setVisibility(View.GONE);
            holder.ratingBarHist.setVisibility(View.VISIBLE);
            holder.ratingBarHist.setRating(Float.valueOf(hist.getCalificacion()));
        }

        holder.llHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogDetalleHistorial(activity, hist.getLatitud(), hist.getLongitud(), hist.getId_solicitud(), hist.getDireccion(),
                        hist.getCiudad(), hist.getDimension(), hist.getId_tipo_inmueble(), hist.getFecha_transaccion(), hist.getHora(),
                        hist.getValor(), hist.getCalificado(), hist.getCalificacion(),
                        new DialogDetalleHistorial.RespuestaListener() {
                    @Override
                    public void onSalir() {
                    }
                }).show();
            }
        });

        holder.tvCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inCal = new Intent(activity, Calificar.class);
                inCal.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(inCal);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return historials.size();
    }

    public class HistorialServiciosViewHolder extends RecyclerView.ViewHolder{

        TextView tvNoServicioHist, tvDirServicioHist, tvPrecioServicio, tvFechaServicioHist, tvCalificar;
        LinearLayout llHistorial;
        RatingBar ratingBarHist;

        public HistorialServiciosViewHolder(View itemView) {
            super(itemView);
            tvNoServicioHist = (TextView) itemView.findViewById(R.id.tvNoServicioHist);
            tvDirServicioHist = (TextView) itemView.findViewById(R.id.tvDirServicioHist);
            tvPrecioServicio = (TextView) itemView.findViewById(R.id.tvPrecioServicio);
            tvFechaServicioHist = (TextView) itemView.findViewById(R.id.tvFechaServicioHist);
            tvCalificar = (TextView) itemView.findViewById(R.id.tvCalificar);
            ratingBarHist = (RatingBar) itemView.findViewById(R.id.ratingBarHist);
            llHistorial = (LinearLayout) itemView.findViewById(R.id.llHistorial);
        }
    }
}
