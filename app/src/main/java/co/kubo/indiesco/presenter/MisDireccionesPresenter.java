package co.kubo.indiesco.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import co.kubo.indiesco.activities.IMisDireccionesPresenter;
import co.kubo.indiesco.activities.IMisDireccionesView;
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
 * Created by Diego on 15/02/2018.
 */

public class MisDireccionesPresenter implements IMisDireccionesPresenter {

    public static final String TAG = "MisDireccionesPresenter";
    private IMisDireccionesView iMisDireccionesView;
    private Context context;
    private ArrayList<Direccion> direccion;

    public MisDireccionesPresenter(IMisDireccionesView iMisDireccionesView, Context context) {
        this.iMisDireccionesView = iMisDireccionesView;
        this.context = context;
        obtenerDirecciones();
    }

    @Override
    public void obtenerDirecciones() {
        String authToken = SharedPreferenceManager.getAuthToken(context);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(context);
        final Call<ResponseDireccion> responseDireccionCall = endpoints.listarDireccion(authToken, usuario.getId_user());
        responseDireccionCall.enqueue(new Callback<ResponseDireccion>() {
            @Override
            public void onResponse(Call<ResponseDireccion> call, Response<ResponseDireccion> response) {
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
                Log.e(TAG, "obtener direcciones onFailure");
            }
        });
    }

    @Override
    public void mostrarDirecciones() {
        iMisDireccionesView.inicializarAdaptadorRvDirecciones(iMisDireccionesView.crearAdaptadorDirecciones(direccion));
        iMisDireccionesView.generarLinearLayoutVertical();
    }
}
