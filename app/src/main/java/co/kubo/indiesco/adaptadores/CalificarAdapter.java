package co.kubo.indiesco.adaptadores;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.Calificar2;
import co.kubo.indiesco.modelo.PendienteCalificar;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import co.kubo.indiesco.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Diego on 25/02/2018.
 */

public class CalificarAdapter extends RecyclerView.Adapter<CalificarAdapter.CalificarViewHolder> {

    public static final String TAG = "CalificarAdapter";
    private ArrayList<PendienteCalificar> calificars;
    Activity activity;
    Utils utils = new Utils();
    float serviceCalification = 0;

    public CalificarAdapter(ArrayList<PendienteCalificar> calificars, Activity activity) {
        this.calificars = calificars;
        this.activity = activity;
    }

    @Override
    public CalificarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calificar, parent, false);
        return new CalificarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CalificarViewHolder holder, final int position) {
        final PendienteCalificar cal = calificars.get(position);

        holder.tvNoServicioCalificar.setText(cal.getIdSolicitud());
        holder.tvFechaServicioCalificar.setText(utils.StringToDate2(cal.getFechaTransaccion()).replace(" ", "/"));
        holder.tvDirServicioCalificar.setText(cal.getDireccion());
        holder.tvPrecioServicio.setText(cal.getValor());

        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                if (rating != 0){
                    serviceCalification = rating;
                    holder.tvEnviarCalificacion.setTextColor(activity.getResources().getColor(R.color.colorVerde));
                    holder.tvEnviarCalificacion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            enviarCalificacion(cal.getIdSolicitud(), String.valueOf(serviceCalification), "Sin comentarios", position);
                        }
                    });
                }else{
                    holder.tvEnviarCalificacion.setTextColor(activity.getResources().getColor(R.color.colorGris));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return calificars.size();
    }

    public class CalificarViewHolder extends RecyclerView.ViewHolder{
        TextView tvNoServicioCalificar, tvFechaServicioCalificar, tvDirServicioCalificar, tvPrecioServicio, tvEnviarCalificacion;
        RatingBar ratingBar;
        public CalificarViewHolder(View itemView) {
            super(itemView);
            tvNoServicioCalificar = (TextView) itemView.findViewById(R.id.tvNoServicioCalificar);
            tvFechaServicioCalificar = (TextView) itemView.findViewById(R.id.tvFechaServicioCalificar);
            tvDirServicioCalificar = (TextView) itemView.findViewById(R.id.tvDirServicioCalificar);
            tvPrecioServicio = (TextView) itemView.findViewById(R.id.tvPrecioServicio);
            tvEnviarCalificacion = (TextView) itemView.findViewById(R.id.tvEnviarCalificacion);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }
    }

    private void enviarCalificacion(String id_solicitud, String serviceCalification, String comentarios, final int adapter_position){
        String authToken = SharedPreferenceManager.getAuthToken(activity);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(activity);
        Call<ResponseGeneral> responseGeneralCall = endpoints.calificarServicio(authToken, id_solicitud, serviceCalification, comentarios);
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        Intent in = new Intent(activity, Calificar2.class);
                        activity.startActivity(in);
                        calificars.remove(adapter_position);
                        notifyDataSetChanged();
                        Toast.makeText(activity, "Servicio calificado", Toast.LENGTH_LONG).show();
                        break;
                    case "102":
                        Toast.makeText(activity, "Algo fallo intente de nuevo", Toast.LENGTH_SHORT).show();
                        break;
                    case "120":
                        Log.e(TAG, "enviarCalificacion cod: 120, onResponse Token Invalido");
                        break;
                }
            }
            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                Log.e(TAG, "onFailure, enviarCalificacion");
            }
        });
    }
}
