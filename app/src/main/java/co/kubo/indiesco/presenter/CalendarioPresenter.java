package co.kubo.indiesco.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import co.kubo.indiesco.activities.OlvidoContrasena;
import co.kubo.indiesco.dialog.DialogProgress;
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
import co.kubo.indiesco.utils.Utils;
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
    Activity activity;
    Utils utils = new Utils();
    private ArrayList<Historial> calendario = new ArrayList<>();
    private ArrayList<Historial> holder_calendar = new ArrayList<>();
    private ArrayList<String> fecha = new ArrayList<>();
    private DialogProgress dialogProgress;

    public CalendarioPresenter(ICalendarioView iCalendarioView, Context context, Activity activity) {
        this.iCalendarioView = iCalendarioView;
        this.context = context;
        this.activity = activity;
        obtenerCalendario();
    }

    @Override
    public void obtenerCalendario() {
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(activity);
            dialogProgress.show();
        }
        String authToken = SharedPreferenceManager.getAuthToken(context);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(context);
        Call<ResponseHistorial> responseHistorialCall = endpoints.listarHistorial(authToken, usuario.getId_user(), "calendario");
        responseHistorialCall.enqueue(new Callback<ResponseHistorial>() {
            @Override
            public void onResponse(Call<ResponseHistorial> call, Response<ResponseHistorial> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        calendario = response.body().getData();
                        fecha.add(calendario.get(0).getFechaServicio());
                        String date[] = calendario.get(0).getFechaServicio().split("-");
                        int x = 0;
                        for (int i = 0; i < calendario.size(); i++){
                            String month[] = calendario.get(i).getFechaServicio().split("-");
                            if (!date[1].equals(month[1])){
                                fecha.add(calendario.get(i).getFechaServicio());
                                date[1] = month[1];
                            }//if
                        }//for
                        boolean band = true;
                        for (int y = 0; y < fecha.size(); y++){
                            band = true;
                            for (int i = 0; i < calendario.size(); i++){
                                String month_date[] = fecha.get(y).split("-");
                                String month_date_service[] = calendario.get(i).getFechaServicio().split("-");
                                if (month_date_service[1].equals(month_date[1])){
                                    if (band){
                                        Historial not = new Historial();
                                        not.setFechaServicio(calendario.get(i).getFechaServicio());
                                        not.setHora(calendario.get(i).getHora());
                                        not.setDireccion(calendario.get(i).getDireccion());
                                        not.setCiudad(calendario.get(i).getCiudad());
                                        not.setInmueble(calendario.get(i).getInmueble());
                                        not.setIdSolicitudItem(calendario.get(i).getIdSolicitudItem());
                                        not.setValor(calendario.get(i).getValor());
                                        not.setIdTipoInmueble(calendario.get(i).getIdTipoInmueble());
                                        not.setDimension(calendario.get(i).getDimension());
                                        not.setLatitud(calendario.get(i).getLatitud());
                                        not.setLongitud(calendario.get(i).getLongitud());
                                        not.setUrgente(calendario.get(i).getUrgente());
                                        not.setIdDireccion(calendario.get(i).getIdDireccion());
                                        not.setData(calendario.get(i).getData());
                                        not.setIsHeader("si");
                                        band = false;
                                        holder_calendar.add(not);
                                    }
                                    Historial not = new Historial();
                                    not.setFechaServicio(calendario.get(i).getFechaServicio());
                                    not.setHora(calendario.get(i).getHora());
                                    not.setDireccion(calendario.get(i).getDireccion());
                                    not.setCiudad(calendario.get(i).getCiudad());
                                    not.setInmueble(calendario.get(i).getInmueble());
                                    not.setIdSolicitudItem(calendario.get(i).getIdSolicitudItem());
                                    not.setValor(calendario.get(i).getValor());
                                    not.setIdTipoInmueble(calendario.get(i).getIdTipoInmueble());
                                    not.setDimension(calendario.get(i).getDimension());
                                    not.setLatitud(calendario.get(i).getLatitud());
                                    not.setLongitud(calendario.get(i).getLongitud());
                                    not.setUrgente(calendario.get(i).getUrgente());
                                    not.setIdDireccion(calendario.get(i).getIdDireccion());
                                    not.setEstado(calendario.get(i).getEstado());
                                    not.setData(calendario.get(i).getData());
                                    not.setIsHeader("no");
                                    band = false;
                                    holder_calendar.add(not);
                                }
                            }//for2
                        }//for
                        mostrarCalendario();
                        break;
                    case "102":
                        iCalendarioView.pintarSinServicio();
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
    }

    @Override
    public void mostrarCalendario() {
        iCalendarioView.inicializarAdaptadorRvCalendario(iCalendarioView.crearAdaptadorCalendario(holder_calendar));
        iCalendarioView.generarLinearLayoutVertical();
    }
}
