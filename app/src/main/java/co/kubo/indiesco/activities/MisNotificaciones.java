package co.kubo.indiesco.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.adaptadores.NotificacionesAdapter;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.interfaces.INotificacionesPresenter;
import co.kubo.indiesco.interfaces.INotificacionesView;
import co.kubo.indiesco.modelo.Notificaciones;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.presenter.NotificacionesPresenter;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseNotificacion;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisNotificaciones extends AppCompatActivity implements INotificacionesView, View.OnClickListener {

    public static final String TAG = "MisNotificaciones";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.rvNotificacion)
    RecyclerView rvNotificacion;
    @BindView(R.id.llSinServicio)
    LinearLayout llSinServicio;
    ArrayList<Notificaciones> notificaciones = new ArrayList<>();
    ArrayList<Notificaciones> notificacionesTemp = new ArrayList<>();
    private ArrayList<String> fecha = new ArrayList<>();
    NotificacionesAdapter notificacionesAdapter;

    INotificacionesPresenter presenter;

    private int page = 20;
    private int offSetReal = 0;
    private int lastPosition = 0;
    private DialogProgress dialogProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_notificaciones);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);

        presenter = new NotificacionesPresenter(this, getApplicationContext(), MisNotificaciones.this);

        rvNotificacion.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerViewHelper mRecyclerViewHelper = new RecyclerViewHelper(recyclerView);
                int visibleItemCount = rvNotificacion.getChildCount();
                int firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                if (notificaciones != null && notificaciones.size() != 0) {
                    int lastItem = firstVisibleItem + visibleItemCount;
                    lastItem = lastItem;
                    Log.v("lastItem", lastItem + "");
                    if ((lastItem / 2) == page && page == (notificaciones.size() / 2)) {
                        lastPosition = lastItem;
                        int valuePage = page;
                        offSetReal += 20;
                        page += 20;
                        // llamar paginado
                        obtenerNotificaciones();
                    }
                }
            }
        });
    }


    @Override
    public void generarLinearLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvNotificacion.setLayoutManager(llm);
    }

    @Override
    public void inicializarAdaptadorRvNotificaciones(NotificacionesAdapter notificacionesAdapter) {
        rvNotificacion.setAdapter(notificacionesAdapter);
    }

    @Override
    public NotificacionesAdapter crearAdaptadorNotificaciones(ArrayList<Notificaciones> notificaciones) {
        this.notificaciones = notificaciones;
        notificacionesAdapter = new NotificacionesAdapter(notificaciones, MisNotificaciones.this);
        return notificacionesAdapter;
    }

    @Override
    public void pintarSinInfo() {
        llSinServicio.setVisibility(View.VISIBLE);
        rvNotificacion.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
        }//switch
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void obtenerNotificaciones() {
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(MisNotificaciones.this);
            dialogProgress.show();
        }
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        Call<ResponseNotificacion> responseNotificacionCall = endpoints.listarNotificaciones(authToken, usuario.getId_user(), offSetReal + "");
        responseNotificacionCall.enqueue(new Callback<ResponseNotificacion>() {
            @Override
            public void onResponse(Call<ResponseNotificacion> call, Response<ResponseNotificacion> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code) {
                    case "100":
                        notificacionesTemp = response.body().getData();
                        if (notificacionesTemp.size() != 0) {
                            fillData();
                        } else {
                            // iNotificacionesView.pintarSinInfo();
                        }

                        // mostrarNotificaciones();
                        break;
                    case "102":
                        // iNotificacionesView.pintarSinInfo();
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
                // iNotificacionesView.pintarSinInfo();
            }
        });
    }

    public void fillData() {
        fecha.add(notificacionesTemp.get(0).getFecha());
        String date = notificacionesTemp.get(0).getFecha();
        int x = 0;
        for (int i = 0; i < notificacionesTemp.size(); i++) {
            if (!date.equals(notificacionesTemp.get(i).getFecha())) {
                fecha.add(notificacionesTemp.get(i).getFecha());
            }//if
        }//for
        boolean band = true;
        for (int y = 0; y < fecha.size(); y++) {
            band = true;
            for (int i = 0; i < notificacionesTemp.size(); i++) {
                if (notificacionesTemp.get(i).getFecha().equals(fecha.get(y))) {
                    if (band) {
                        Notificaciones not = new Notificaciones();
                        not.setFecha(notificacionesTemp.get(i).getFecha());
                        not.setId_notificacion(notificacionesTemp.get(i).getId_notificacion());
                        not.setNotificacion("");
                        not.setIsHeader("si");
                        band = false;
                        notificaciones.add(not);
                    }
                    Notificaciones not = new Notificaciones();
                    not.setFecha(notificacionesTemp.get(i).getFecha());
                    not.setId_notificacion(notificacionesTemp.get(i).getId_notificacion());
                    not.setNotificacion(notificacionesTemp.get(i).getNotificacion());
                    not.setIsHeader("no");
                    band = false;
                    notificaciones.add(not);
                }
            }//for2
        }//for
        //notificaciones.addAll(notificacionesTemp);
        notificacionesAdapter.notifyDataSetChanged();
    }
}
