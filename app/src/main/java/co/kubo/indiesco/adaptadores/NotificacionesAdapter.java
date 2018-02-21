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
import co.kubo.indiesco.modelo.Notificaciones;

/**
 * Created by estacion on 21/02/18.
 */

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.NotificacionesViewHolder>{

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<Notificaciones> notificaciones;
    Activity activity;

    public NotificacionesAdapter(ArrayList<Notificaciones> notificaciones, Activity activity){
        this.notificaciones = notificaciones;
        this.activity=activity;
    }

    @Override
    public NotificacionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_notificaciones, parent, false);
            return new NotificacionesViewHolder(v);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_notificaciones, parent, false);
            return new NotificacionesViewHolder(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(NotificacionesViewHolder holder, int position) {
        final Notificaciones noti = notificaciones.get(position);
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    public class NotificacionesViewHolder extends RecyclerView.ViewHolder {
        TextView tvNoServicioHist, tvDirServicioHist, tvPrecioServicio, tvFechaServicioHist;
        LinearLayout llHistorial;

        public NotificacionesViewHolder(View itemView) {
            super(itemView);
            tvNoServicioHist = (TextView) itemView.findViewById(R.id.tvNoServicioHist);
            tvDirServicioHist = (TextView) itemView.findViewById(R.id.tvDirServicioHist);
            tvPrecioServicio = (TextView) itemView.findViewById(R.id.tvPrecioServicio);
            tvFechaServicioHist = (TextView) itemView.findViewById(R.id.tvFechaServicioHist);
            llHistorial = (LinearLayout) itemView.findViewById(R.id.llHistorial);
        }
    }
}