package co.kubo.indiesco.adaptadores;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.Calendario2;
import co.kubo.indiesco.activities.ViewHolderListItemCalendario;
import co.kubo.indiesco.dialog.DialogDetalleCalendario;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by estacion on 1/03/18.
 */

public class CalendarioDetalleFechaAdapter extends RecyclerView.Adapter<CalendarioDetalleFechaAdapter.CalendarioDetalleFechaViewHolder> {

    public static final String TAG = "CalenDetFechAdapter";
    Activity activity;
    private ArrayList<Historial> calendar;
    private String hora;
    Calendario2 calendario2;

    public CalendarioDetalleFechaAdapter(ArrayList<Historial> calendar, Activity activity, Calendario2 calendario2) {
        this.activity = activity;
        this.calendar = calendar;
        this.calendario2 = calendario2;
    }

    @Override
    public CalendarioDetalleFechaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendario_detalle_fecha, parent, false);
        return new CalendarioDetalleFechaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CalendarioDetalleFechaViewHolder holder, final int position) {
        final Historial hist = calendar.get(position);
        holder.tvDir.setText(hist.getDireccion());
        holder.tvHora.setText(hist.getHora());
        holder.llItemCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogDetalleCalendario(activity, calendar.get(position).getLatitud(), calendar.get(position).getLongitud(),
                        calendar.get(position).getId_solicitud(), calendar.get(position).getDireccion(), calendar.get(position).getCiudad(),
                        calendar.get(position).getDimension(), calendar.get(position).getId_tipo_inmueble(),
                        calendar.get(position).getFecha_servicio(), hora, calendar.get(position).getValor(),
                        new DialogDetalleCalendario.RespuestaListener() {
                            @Override
                            public void onCancelarServicio() {
                                cancelarServicio(calendar.get(position).getId_solicitud(), position);
                            }
                            @Override
                            public void onSalir() {}
                        }).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return calendar.size();
    }
    public class CalendarioDetalleFechaViewHolder extends RecyclerView.ViewHolder{
        TextView tvDir, tvHora;
        LinearLayout llItemCalendario;
        public CalendarioDetalleFechaViewHolder(View itemView) {
            super(itemView);
            tvDir = (TextView) itemView.findViewById(R.id.tvDir);
            tvHora = (TextView) itemView.findViewById(R.id.tvHora);
            llItemCalendario = (LinearLayout) itemView.findViewById(R.id.llItemCalendario);
        }
    }

    private void cancelarServicio(String id_solicitud, final int adapter_position){
        String authToken = SharedPreferenceManager.getAuthToken(activity);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(activity);
        Call<ResponseGeneral> responseGeneralCall = endpoints.cancelarServicio(authToken, id_solicitud, usuario.getId_user());
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        Toast.makeText(activity, "Cancelo el servicio con éxito", Toast.LENGTH_LONG).show();
                        if (calendar.size() == 1){
                            calendario2.pintarSinServicio();
                        }
                        calendar.remove(adapter_position);
                        notifyDataSetChanged();
                        calendario2.obtenerCalendario();
                        break;
                    case "102":
                        Toast.makeText(activity, "La solicitud no se puede cancelar", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Cod: 102 No se puede cancelar la solicitud");
                        break;
                    case "120":
                        Log.e(TAG, "Cod: 120 auth token invalido");
                        break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                Log.e(TAG, "cancelarServicio onFailure");
            }
        });
    }//private void cancelarServicio

}
