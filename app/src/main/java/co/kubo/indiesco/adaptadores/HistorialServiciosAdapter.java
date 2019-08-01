package co.kubo.indiesco.adaptadores;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.Calificar;
import co.kubo.indiesco.dialog.DialogDetalleHistorial;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.utils.Utils;

/**
 * Created by Diego on 12/02/2018.
 */

public class HistorialServiciosAdapter extends RecyclerView.Adapter<HistorialServiciosAdapter.HistorialServiciosViewHolder> {

    private ArrayList<Historial> historials;
    Activity activity;
    Utils utils = new Utils();
    DialogDetalleHistorial dialog;

    public HistorialServiciosAdapter(ArrayList<Historial> historials, Activity activity) {
        this.historials = historials;
        this.activity = activity;
        dialog = new DialogDetalleHistorial(activity);
    }

    @Override
    public HistorialServiciosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        return new HistorialServiciosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HistorialServiciosViewHolder holder, int position) {
        final Historial hist = historials.get(position);

        holder.tvNoServicioHist.setText(String.valueOf(hist.getIdSolicitudItem()));
        holder.tvFechaServicioHist.setText(String.valueOf(utils.StringToDate2(hist.getFechaServicio())).replace(" ", "/"));
        holder.tvDirServicioHist.setText(String.valueOf(hist.getDireccion()));
        DecimalFormat formateador = new DecimalFormat("###,###");
        holder.tvPrecioServicio.setText(formateador.format(Double.parseDouble(String.valueOf(hist.getValor()))) + " COP");

        if (hist.getCalificado().equals("no")){
            holder.tvCalificar.setVisibility(View.VISIBLE);
            holder.ratingBarHist.setVisibility(View.GONE);
            Log.v("hist",hist.getEstado());
            if (hist.getEstado().equals("cancelado_usuario")){

                holder.tvCalificar.setVisibility(View.VISIBLE);
                holder.tvCalificar.setText("Cancelado");
                holder.tvCalificar.setTextColor(activity.getResources().getColor(R.color.colorRojo));
                holder.ratingBarHist.setVisibility(View.GONE);
                holder.llCalificar.setClickable(false);
                holder.llCalificar.setEnabled(false);
            } else {
                holder.tvCalificar.setVisibility(View.VISIBLE);
                holder.tvCalificar.setText("Calificar");
                holder.tvCalificar.setTextColor(activity.getResources().getColor(R.color.colorVerde));
                holder.ratingBarHist.setVisibility(View.GONE);
                holder.llCalificar.setClickable(true);
                holder.llCalificar.setEnabled(true);
            }
        }else{
            holder.tvCalificar.setVisibility(View.GONE);
            holder.ratingBarHist.setVisibility(View.VISIBLE);
            holder.ratingBarHist.setRating(Float.valueOf(hist.getCalificacion()));
        }



        holder.llHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dialog.isShowing()) {
                    dialog = new DialogDetalleHistorial(activity, hist.getLatitud(), hist.getLongitud(), hist.getIdSolicitudItem(), hist.getDireccion(),
                            hist.getCiudad(), hist.getDimension(), hist.getIdTipoInmueble(), hist.getFechaServicio(), hist.getHora(),
                            hist.getValor(), hist.getCalificado(), hist.getCalificacion(),
                            new DialogDetalleHistorial.RespuestaListener() {
                                @Override
                                public void onSalir() {
                                }
                            },hist.getData());
                    dialog.show();
                }
            }
        });

        holder.llCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inCal = new Intent(activity, Calificar.class);
                inCal.putExtra("id_servicio", hist.getIdSolicitudItem());
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
        LinearLayout llHistorial, llCalificar;
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
            llCalificar = (LinearLayout) itemView.findViewById(R.id.llCalificar);
        }
    }
}
