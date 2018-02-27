package co.kubo.indiesco.adaptadores;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.IniciarSesion;
import co.kubo.indiesco.activities.ViewHolderHeader;
import co.kubo.indiesco.activities.ViewHolderListItem;
import co.kubo.indiesco.dialog.DialogDosOpciones;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.modelo.Notificaciones;
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
 * Created by estacion on 21/02/18.
 */

public class NotificacionesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final String TAG = "NotificacionesAdapter";
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<Notificaciones> notificaciones;
    private ArrayList<String> fecha;
    Activity activity;
    private String fechaX;
    private int i = 0;
    private DialogProgress dialogProgress;

    public NotificacionesAdapter(ArrayList<Notificaciones> notificaciones, Activity activity){
        this.notificaciones = notificaciones;
        this.fecha = fecha;
        this.activity=activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_notificaciones, parent, false);
            ViewHolderListItem holder = new ViewHolderListItem(v);
            return holder;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_notificaciones, parent, false);
            return new ViewHolderHeader(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolderListItem) {
            String [] splitHora_1 = notificaciones.get(position).getFecha().split(" ");
            String [] splitHora_2 = splitHora_1[1].split(":");
            String hora = splitHora_2[0].concat(":").concat(splitHora_2[1]);
            ((ViewHolderListItem) holder).setTvHora(hora);
            ((ViewHolderListItem) holder).setTvMsj(notificaciones.get(position).getNotificacion());

            ((ViewHolderListItem) holder).getLlBorrar().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogDosOpciones(activity, "1", new DialogDosOpciones.RespuestaListener() {
                        @Override
                        public void onCancelar() {
                        }
                        @Override
                        public void onAceptar() {
                            borrarNotificacion(notificaciones.get(position).getId_notificacion(), position, ((ViewHolderListItem) holder).getLlBorrar());
                        }
                        @Override
                        public void onSalir() {
                        }
                    }).show();
                }
            });

        } else if (holder instanceof ViewHolderHeader) {
            Utils utils = new Utils();
            Date currentTime = Calendar.getInstance().getTime();
            String today =  utils.DateToString(currentTime);
            String nueva_fecha = utils.StringToDate(notificaciones.get(position).getFecha());
            if (today.equals(nueva_fecha)){
                ((ViewHolderHeader) holder).setTvHeader("Hoy");
            }else{
                ((ViewHolderHeader) holder).setTvHeader(nueva_fecha.replace(" ", " de "));
            }
        }
    }

    @Override
    public int getItemCount() {
        //return notificaciones.size() + fecha.size();
        return notificaciones.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        if (notificaciones.get(position).getIsHeader().equals("no")){
            return false;
        }else{
            return true;
        }
    }

    public void borrarNotificacion(String id_notif, final int adapter_position, final LinearLayout llBorrar ){
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(activity);
            dialogProgress.show();
        }
        llBorrar.setEnabled(false);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        String authToken = SharedPreferenceManager.getAuthToken(activity);
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(activity);
        String uid = usuario.getId_user();
        Call<ResponseGeneral> responseGeneralCall = endpoints.eliminarNotificacion(authToken, uid,id_notif);
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        Toast.makeText(activity, "Notificación eliminada con éxito", Toast.LENGTH_SHORT).show();
                        int cont = 0;
                        for (int i = 0; i < notificaciones.size(); i++){
                            if (notificaciones.get(adapter_position).getFecha().equals(notificaciones.get(i).getFecha())){
                                cont++;
                            }
                        }//for
                        if (cont == 2){
                            notificaciones.remove(adapter_position);
                            notificaciones.remove(adapter_position-1);
                            notifyDataSetChanged();
                        }else{
                            notificaciones.remove(adapter_position);
                            notifyDataSetChanged();
                        }//else

                        if (notificaciones.size() == 0){
                            Toast.makeText(activity, "No hay mas notificaciones para mostrar", Toast.LENGTH_LONG).show();
                        }//if
                        llBorrar.setEnabled(true);
                        break;
                    case "102":
                        Log.e(TAG, "borrarNotificacion Algo fallo");
                        break;
                    case "120":
                        Log.e(TAG, "borrarNotificacion falla auth token");
                        break;
                    default:break;
                }
            }
            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "borrarNotificacion onFailure");
            }
        });
    }
}