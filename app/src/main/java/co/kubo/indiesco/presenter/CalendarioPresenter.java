package co.kubo.indiesco.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import co.kubo.indiesco.interfaces.ICalendarioPresenter;
import co.kubo.indiesco.interfaces.ICalendarioView;
import co.kubo.indiesco.interfaces.INotificacionesView;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.modelo.Notificaciones;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseHistorial;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Diego on 24/02/2018.
 */

public class CalendarioPresenter implements ICalendarioPresenter{

    public static final String TAG = "CalendarioPresenter";
    private ICalendarioView iCalendarioView;
    private Context context;
    private ArrayList<Historial> calendario = new ArrayList<>();
    private ArrayList<Notificaciones> holder_calendar = new ArrayList<>();
    private ArrayList<String> fecha = new ArrayList<>();

    public CalendarioPresenter(ICalendarioView iCalendarioView, Context context) {
        this.iCalendarioView = iCalendarioView;
        this.context = context;
        obtenerCalendario();
    }

    @Override
    public void obtenerCalendario() {
        String authToken = SharedPreferenceManager.getAuthToken(context);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(context);
        Call<ResponseHistorial> responseHistorialCall = endpoints.listarHistorial(authToken, usuario.getId_user(), "calendario");
        responseHistorialCall.enqueue(new Callback<ResponseHistorial>() {
            @Override
            public void onResponse(Call<ResponseHistorial> call, Response<ResponseHistorial> response) {
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        calendario = response.body().getData();
                        fecha.add(calendario.get(0).getFecha_servicio());
                        String date = calendario.get(0).getFecha_servicio();
                        int x = 0;
                        for (int i = 0; i < calendario.size(); i++){
                            if (!date.equals(calendario.get(i).getFecha_servicio())){
                                fecha.add(calendario.get(i).getFecha_servicio());
                            }//if
                        }//for
                        boolean band = true;
                        for (int y = 0; y < fecha.size(); y++){
                            band = true;
                            for (int i = 0; i < calendario.size(); i++){
                                if (calendario.get(i).getFecha_servicio().equals(fecha.get(y))){
                                    if (band){
                                        Notificaciones not = new Notificaciones();
                                        not.setFecha(calendario.get(i).getFecha_servicio());
                                        not.setNotificacion("");
                                        not.setIsHeader("si");
                                        band = false;
                                        holder_calendar.add(not);
                                    }
                                    Notificaciones not = new Notificaciones();
                                    not.setFecha(calendario.get(i).getFecha_servicio());
                                    not.setIsHeader("no");
                                    band = false;
                                    holder_calendar.add(not);
                                }
                            }//for2
                        }//for
                        mostrarCalendario();
                        break;
                    case "102":
                        Toast.makeText(context, "No hay datos para mostrar", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Cod: 102 No hay datos");
                        break;
                    case "120":
                        Log.e(TAG, "Cod: 120 auth token invalido");
                        break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseHistorial> call, Throwable t) {
                Log.e(TAG, "obtener historial onFailure");
            }
        });
    }

    @Override
    public void mostrarCalendario() {
        iCalendarioView.inicializarAdaptadorRvCalendario(iCalendarioView.crearAdaptadorCalendario(calendario));
        iCalendarioView.generarLinearLayoutVertical();
    }
}
