package co.kubo.indiesco.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import co.kubo.indiesco.activities.Calificar;
import co.kubo.indiesco.activities.Home;
import co.kubo.indiesco.activities.IniciarSesion;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.interfaces.ICalendarioView;
import co.kubo.indiesco.interfaces.ICalificarPresenter;
import co.kubo.indiesco.interfaces.ICalificarView;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.modelo.Notificaciones;
import co.kubo.indiesco.modelo.PendienteCalificar;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponsePendienteCalificar;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Diego on 25/02/2018.
 */

public class CalificarPresenter implements ICalificarPresenter {
    public static final String TAG = "CalificarPresenter";
    private ICalificarView iCalificarView;
    private Context context;
    private Activity activity;
    private ArrayList<PendienteCalificar> calificar = new ArrayList<>();
    private DialogProgress dialogProgress;

    public CalificarPresenter(ICalificarView iCalificarView, Context context, Activity activity) {
        this.iCalificarView = iCalificarView;
        this.context = context;
        this.activity = activity;

        obtenerCalificar();
    }


    @Override
    public void obtenerCalificar() {
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(activity);
            dialogProgress.show();
        }
        String authToken = SharedPreferenceManager.getAuthToken(context);
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(context);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        final Call<ResponsePendienteCalificar> responsePendienteCalificarCall = endpoints.pendienteCalificar(authToken, usuario.getId_user());
        responsePendienteCalificarCall.enqueue(new Callback<ResponsePendienteCalificar>() {
            @Override
            public void onResponse(Call<ResponsePendienteCalificar> call, Response<ResponsePendienteCalificar> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){
                    case "100": //Servicios pendientes por calificar
                    calificar = response.body().getData();
                    mostrarCalificar();
                        break;
                    case "102": //No hay pendientes para calificar va a Home
                        Log.e(TAG, "No hay datos obtenerCalificar");
                        break;
                    case "120": //auth_token no valido
                        break;
                    default: break;
                }
            }
            @Override
            public void onFailure(Call<ResponsePendienteCalificar> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "onFailure pendienteCalificar");
            }
        });
    }

    @Override
    public void mostrarCalificar() {
        iCalificarView.inicializarAdaptadorRvCalificar(iCalificarView.crearAdaptadorCalificar(calificar));
        iCalificarView.generarLinearLayoutVertical();
    }
}
