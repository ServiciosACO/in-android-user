package co.kubo.indiesco.adaptadores;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.Home;
import co.kubo.indiesco.activities.Transaccion;
import co.kubo.indiesco.activities.ViewHolderHeader;
import co.kubo.indiesco.activities.ViewHolderHeaderCalendario;
import co.kubo.indiesco.activities.ViewHolderListItem;
import co.kubo.indiesco.activities.ViewHolderListItemCalendario;
import co.kubo.indiesco.dialog.DialogDetalleCalendario;
import co.kubo.indiesco.modelo.Historial;
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
 * Created by Diego on 24/02/2018.
 */

public class CalendarioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final String TAG = "CalendarioAdapter";
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<Historial> calendar;
    private ArrayList<String> fecha;
    Activity activity;
    private String hora;
    Utils utils = new Utils();

    public CalendarioAdapter(ArrayList<Historial> calendar, Activity activity){
        this.calendar = calendar;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_calendario, parent, false);
            ViewHolderListItemCalendario holder = new ViewHolderListItemCalendario(v);
            return holder;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_calendario, parent, false);
            return new ViewHolderHeaderCalendario(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolderListItemCalendario) {
            ((ViewHolderListItemCalendario) holder).setTvDir(calendar.get(position).getDireccion());

            /*String [] splitHora_1 = calendar.get(position).getFecha_servicio().split(" ");
            String [] splitHora_2 = splitHora_1[1].split(":");
            final String time = splitHora_2[0].concat(":").concat(splitHora_2[1]); //Hora militar*/
            final String time = calendar.get(position).getHora();
            /**Para cambiar la hora al formato 12 horas*/
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObj = sdf.parse(time);
                System.out.println(dateObj);
                System.out.println(new SimpleDateFormat("K:mm a").format(dateObj));
                hora = new SimpleDateFormat("K:mm a").format(dateObj);
            } catch (final ParseException e) {
                e.printStackTrace();
            }
            ((ViewHolderListItemCalendario) holder).setTvHoraCalendar(hora);

            String fecha_1 = utils.StringToDate2(calendar.get(position).getFechaServicio());
            String[] fecha_2 = fecha_1.split(" ");
            String fecha = fecha_2[0].concat("/").concat(fecha_2[1]);
            ((ViewHolderListItemCalendario) holder).setTvFecha(fecha);

            ((ViewHolderListItemCalendario) holder).getLlItemCalendario().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogDetalleCalendario(activity, calendar.get(position).getLatitud(), calendar.get(position).getLongitud(),
                            calendar.get(position).getIdSolicitudItem(), calendar.get(position).getDireccion(), calendar.get(position).getCiudad(),
                            calendar.get(position).getDimension(), calendar.get(position).getIdTipoInmueble(),
                            calendar.get(position).getFechaServicio(), hora, calendar.get(position).getValor(), calendar.get(position).getEstado(),
                            new DialogDetalleCalendario.RespuestaListener() {
                        @Override
                        public void onCancelarServicio() {
                            cancelarServicio(calendar.get(position).getIdSolicitudItem(), position);
                        }
                        @Override
                        public void onSalir() {}
                    },calendar.get(position).getData()).show();
                }
            });

        } else if (holder instanceof ViewHolderHeaderCalendario) {
            Utils utils = new Utils();
            String nueva_fecha = utils.StringToDate(calendar.get(position).getFechaServicio());
            String [] extractMes = nueva_fecha.split(" ");
            String month = extractMes[1];
            ((ViewHolderHeaderCalendario) holder).setTvHeader(month);
        }
    }

    @Override
    public int getItemCount() {
        return calendar.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        if (calendar.get(position).getIsHeader().equals("no")){
            return false;
        }else{
            return true;
        }
    }

    private void cancelarServicio(String id_solicitud, final int adapter_position){
        String authToken = SharedPreferenceManager.getAuthToken(activity);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(activity);
        Call<ResponseGeneral> responseGeneralCall = endpoints.cancelarServicio(authToken, Integer.parseInt(id_solicitud), usuario.getId_user());
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        Toast.makeText(activity, "Cancelo el servicio con éxito", Toast.LENGTH_LONG).show();
                        calendar.remove(adapter_position);
                        notifyDataSetChanged();
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