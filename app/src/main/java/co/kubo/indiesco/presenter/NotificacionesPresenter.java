package co.kubo.indiesco.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import co.kubo.indiesco.activities.IniciarSesion;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.interfaces.INotificacionesPresenter;
import co.kubo.indiesco.interfaces.INotificacionesView;
import co.kubo.indiesco.modelo.Notificaciones;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseNotificacion;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by estacion on 21/02/18.
 */

public class NotificacionesPresenter implements INotificacionesPresenter {

    public static final String TAG = "NotificacionesPresenter";
    private INotificacionesView iNotificacionesView;
    private Context context;
    private Activity activity;
    private ArrayList<Notificaciones> notificaciones = new ArrayList<>();
    private ArrayList<Notificaciones> holder_notif = new ArrayList<>();
    private ArrayList<String> fecha = new ArrayList<>();
    private DialogProgress dialogProgress;

    public NotificacionesPresenter(INotificacionesView iNotificacionesView, Context context, Activity activity) {
        this.iNotificacionesView = iNotificacionesView;
        this.context = context;
        this.activity = activity;
        obtenerNotificaciones();
    }

    @Override
    public void obtenerNotificaciones() {
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(activity);
            dialogProgress.show();
        }
        String authToken = SharedPreferenceManager.getAuthToken(context);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(context);
        Call<ResponseNotificacion> responseNotificacionCall= endpoints.listarNotificaciones(authToken, usuario.getId_user(), "0");
        responseNotificacionCall.enqueue(new Callback<ResponseNotificacion>() {
            @Override
            public void onResponse(Call<ResponseNotificacion> call, Response<ResponseNotificacion> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        notificaciones = response.body().getData();
                        if (notificaciones.size() != 0){
                            fecha.add(notificaciones.get(0).getFecha());
                            String date = notificaciones.get(0).getFecha();
                            int x = 0;
                            for (int i = 0; i < notificaciones.size(); i++){
                                if (!date.equals(notificaciones.get(i).getFecha())){
                                    fecha.add(notificaciones.get(i).getFecha());
                                }//if
                            }//for
                            boolean band = true;
                            for (int y = 0; y < fecha.size(); y++){
                                band = true;
                                for (int i = 0; i < notificaciones.size(); i++){
                                    if (notificaciones.get(i).getFecha().equals(fecha.get(y))){
                                        if (band){
                                            Notificaciones not = new Notificaciones();
                                            not.setFecha(notificaciones.get(i).getFecha());
                                            not.setId_notificacion(notificaciones.get(i).getId_notificacion());
                                            not.setNotificacion("");
                                            not.setIsHeader("si");
                                            band = false;
                                            holder_notif.add(not);
                                        }
                                        Notificaciones not = new Notificaciones();
                                        not.setFecha(notificaciones.get(i).getFecha());
                                        not.setId_notificacion(notificaciones.get(i).getId_notificacion());
                                        not.setNotificacion(notificaciones.get(i).getNotificacion());
                                        not.setIsHeader("no");
                                        band = false;
                                        holder_notif.add(not);
                                    }
                                }//for2
                            }//for
                        }else{
                            iNotificacionesView.pintarSinInfo();
                        }

                        mostrarNotificaciones();
                        break;
                    case "102":
                        iNotificacionesView.pintarSinInfo();
                        Log.e(TAG, "Cod: 102 No hay datos");
                        break;
                    case "120":
                        Log.e(TAG, "Cod: 120 auth token invalido");
                        break;
                }
            }
            @Override
            public void onFailure(Call<ResponseNotificacion> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "obtener notificaciones onFailure");
                iNotificacionesView.pintarSinInfo();
            }
        });
    }

    @Override
    public void mostrarNotificaciones() {
        iNotificacionesView.inicializarAdaptadorRvNotificaciones(iNotificacionesView.crearAdaptadorNotificaciones(holder_notif));
        iNotificacionesView.generarLinearLayoutVertical();
    }
}
