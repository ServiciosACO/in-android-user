package co.kubo.indiesco.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.interfaces.ICalendario2Presenter;
import co.kubo.indiesco.interfaces.ICalendario2View;
import co.kubo.indiesco.interfaces.ICalendarioPresenter;
import co.kubo.indiesco.modelo.Historial;
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
 * Created by estacion on 1/03/18.
 */

public class Calendario2Presenter implements ICalendario2Presenter {
    public static final String TAG = "Calendario2Presenter";
    private ICalendario2View iCalendario2View;
    private Context context;
    Activity activity;
    Utils utils = new Utils();
    private ArrayList<Historial> calendario = new ArrayList<>();
    private ArrayList<Historial> holder_calendar = new ArrayList<>();
    private ArrayList<String> fecha = new ArrayList<>();
    private DialogProgress dialogProgress;
    private String _fecha;

    public Calendario2Presenter(ICalendario2View iCalendario2View, Context context, Activity activity, String _fecha) {
        this.iCalendario2View = iCalendario2View;
        this.context = context;
        this.activity = activity;
        this._fecha = _fecha;
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
                        for (int i = 0; i < calendario.size(); i++){
                            if (_fecha.equals(utils.StringToDate(calendario.get(i).getFechaServicio()).replace(" ", " de "))){
                                Historial holder_hist = new Historial();
                                holder_hist.setFechaServicio(calendario.get(i).getFechaServicio());
                                holder_hist.setHora(calendario.get(i).getHora());
                                holder_hist.setDireccion(calendario.get(i).getDireccion());
                                holder_hist.setCiudad(calendario.get(i).getCiudad());
                                holder_hist.setLongitud(calendario.get(i).getLongitud());
                                holder_hist.setLatitud(calendario.get(i).getLatitud());
                                holder_hist.setDimension(calendario.get(i).getDimension());
                                holder_hist.setInmueble(calendario.get(i).getInmueble());
                                holder_hist.setIdTipoInmueble(calendario.get(i).getIdTipoInmueble());
                                holder_hist.setIdSolicitudItem(calendario.get(i).getIdSolicitudItem());
                                //holder_hist.setId_pedido(calendario.get(i).getId_pedido());
                                holder_hist.setEstado(calendario.get(i).getEstado());
                                holder_hist.setIdDireccion(calendario.get(i).getIdDireccion());
                                holder_hist.setValor(calendario.get(i).getValor());
                                holder_calendar.add(holder_hist);
                            }
                        }//for
                        mostrarCalendario();
                        break;
                    case "102":
                        iCalendario2View.pintarSinServicio();
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
        iCalendario2View.inicializarAdaptadorRvCalendarioPorFecha(iCalendario2View.crearAdaptadorCalendarioPorFecha(holder_calendar));
        iCalendario2View.generarLinearLayoutVertical();
    }
}
