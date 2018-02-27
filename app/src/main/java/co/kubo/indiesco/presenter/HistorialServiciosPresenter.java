package co.kubo.indiesco.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import co.kubo.indiesco.activities.OlvidoContrasena;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.interfaces.IHistorialServiciosPresenter;
import co.kubo.indiesco.interfaces.IHistorialServiciosView;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseHistorial;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Diego on 12/02/2018.
 */

public class HistorialServiciosPresenter implements IHistorialServiciosPresenter{

    public static final String TAG = "HistorialServiciosPrese";
    private IHistorialServiciosView iHistorialServiciosView;
    private Context context;
    private Activity activity;
    private ArrayList<Historial> hist = new ArrayList<>();
    private DialogProgress dialogProgress;

    public HistorialServiciosPresenter(IHistorialServiciosView iHistorialServiciosView, Context context, Activity activity) {
        this.iHistorialServiciosView = iHistorialServiciosView;
        this.context = context;
        this.activity = activity;
        obtenerHistorial();
    }

    @Override
    public void obtenerHistorial() {
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(activity);
            dialogProgress.show();
        }
        String authToken = SharedPreferenceManager.getAuthToken(context);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(context);
        Call<ResponseHistorial> responseHistorialCall = endpoints.listarHistorial(authToken, usuario.getId_user(), "historial");
        responseHistorialCall.enqueue(new Callback<ResponseHistorial>() {
            @Override
            public void onResponse(Call<ResponseHistorial> call, Response<ResponseHistorial> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        hist = response.body().getData();
                        mostrarHistorial();
                        break;
                    case "102":
                        Toast.makeText(context, "No tiene servicios agendados", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Cod: 102 No hay datos");
                        break;
                    case "120":
                        Log.e(TAG, "Cod: 120 auth token invalido");
                        break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseHistorial> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "obtener historial onFailure");
            }
        });
    }//obtenerHistorial

    @Override
    public void mostrarHistorial(){
        iHistorialServiciosView.inicializarAdaptadorRvHistorial(iHistorialServiciosView.crearAdaptadorHistorial(hist));
        iHistorialServiciosView.generarLinearLayoutVertical();
    }
}
