package co.kubo.indiesco.adaptadores;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.modelo.Historial;

/**
 * Created by Diego on 12/02/2018.
 */

public class HistorialServiciosAdapter extends RecyclerView.Adapter<HistorialServiciosAdapter.HistorialServiciosViewHolder> {

    private ArrayList<Historial> historials;
    Activity activity;

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
    public void onBindViewHolder(HistorialServiciosViewHolder holder, int position) {
        Historial hist = historials.get(position);
        holder.tvNoServicioHist.setText(String.valueOf(hist.getId_pedido()));
        holder.tvFechaServicioHist.setText(String.valueOf(hist.getFecha_servicio()));
        holder.tvDirServicioHist.setText(String.valueOf(hist.getDireccion()));
        holder.tvPrecioServicio.setText(String.valueOf(hist.getValor()));

        holder.llHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return historials.size();
    }

    public class HistorialServiciosViewHolder extends RecyclerView.ViewHolder{

        TextView tvNoServicioHist, tvDirServicioHist, tvPrecioServicio, tvFechaServicioHist;
        LinearLayout llHistorial;

        public HistorialServiciosViewHolder(View itemView) {
            super(itemView);
            tvNoServicioHist = (TextView) itemView.findViewById(R.id.tvNoServicioHist);
            tvDirServicioHist = (TextView) itemView.findViewById(R.id.tvDirServicioHist);
            tvPrecioServicio = (TextView) itemView.findViewById(R.id.tvPrecioServicio);
            tvFechaServicioHist = (TextView) itemView.findViewById(R.id.tvFechaServicioHist);
            llHistorial = (LinearLayout) itemView.findViewById(R.id.llHistorial);
        }
    }
}
