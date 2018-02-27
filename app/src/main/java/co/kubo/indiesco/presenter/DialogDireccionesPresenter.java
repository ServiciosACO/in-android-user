package co.kubo.indiesco.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import co.kubo.indiesco.activities.IniciarSesion;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.interfaces.IDialogDireccionesView;
import co.kubo.indiesco.interfaces.IMisDireccionesPresenter;
import co.kubo.indiesco.modelo.Direccion;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseDireccion;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by estacion on 20/02/18.
 */

public class DialogDireccionesPresenter implements IMisDireccionesPresenter {
    public static final String TAG = "DialogDirPresenter";
    private IDialogDireccionesView iDialogDireccionesView;
    private Context context;
    private Activity activity;
    private ArrayList<Direccion> direccion;
    private DialogProgress dialogProgress;

    public DialogDireccionesPresenter(IDialogDireccionesView iDialogDireccionesView, Context context, Activity activity) {
        this.iDialogDireccionesView = iDialogDireccionesView;
        this.context = context;
        this.activity = activity;
        obtenerDirecciones();
    }

    @Override
    public void obtenerDirecciones() {
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(activity);
            dialogProgress.show();
        }
        String authToken = SharedPreferenceManager.getAuthToken(context);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(context);
        final Call<ResponseDireccion> responseDireccionCall = endpoints.listarDireccion(authToken, usuario.getId_user());
        responseDireccionCall.enqueue(new Callback<ResponseDireccion>() {
            @Override
            public void onResponse(Call<ResponseDireccion> call, Response<ResponseDireccion> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        direccion = response.body().getData();

                        mostrarDirecciones();
                        break;
                    case "102":
                        Toast.makeText(context, "Algo fallo intente de nuevo", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Cod: 102 No hay datos");
                        break;
                    case "120":
                        Log.e(TAG, "Cod: 120 auth token invalido");
                        break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseDireccion> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "obtener direcciones onFailure");
            }
        });
    }

    @Override
    public void mostrarDirecciones() {
        iDialogDireccionesView.inicializarAdaptadorRvDirecciones(iDialogDireccionesView.crearAdaptadorDialogDirecciones(direccion));
        iDialogDireccionesView.generarLinearLayoutVertical();
    }
}
