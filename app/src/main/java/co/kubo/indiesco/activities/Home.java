package co.kubo.indiesco.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.dialog.DialogPendienteCalificar;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.restAPI.modelo.ResponseHistorial;
import co.kubo.indiesco.restAPI.modelo.ResponsePendienteCalificar;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Home";
    @BindView(R.id.imgFotoPerfil)
    ImageView imgFotoPerfil;
    @BindView(R.id.llSolicitarServ)
    LinearLayout llSolicitarServ;
    @BindView(R.id.llCalendario)
    LinearLayout llCalendario;
    @BindView(R.id.llNotificaciones)
    LinearLayout llNotificaciones;
    @BindView(R.id.llExcedente)
    LinearLayout llExcedente;
    @BindView(R.id.tvMiPerfil)
    TextView tvMiPerfil;
    @BindView(R.id.tvNombrePerfil)
    TextView tvNombrePerfil;
    @BindView(R.id.tvNserviciosProgramados)
    TextView tvNserviciosProgramados;
    @BindView(R.id.tvNnotificaciones)
    TextView tvNnotificaciones;
    @BindView(R.id.tvVersion)
    TextView tvVersion;

    private ArrayList<Historial> calendario = new ArrayList<>();
    private DialogProgress dialogProgress;
    String versionName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setTokenFirebase();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvVersion.setText("V" + versionName);
        setListeners();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        tvNombrePerfil.setText(usuario.getName());
        Picasso
                .with(getApplicationContext())
                .load(usuario.getFoto())
                .placeholder(getResources().getDrawable(R.drawable.registro_foto))
                .error(getResources().getDrawable(R.drawable.registro_foto))
                .transform(new CircleTransform())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imgFotoPerfil);

        obtenerCalendario();
        pendienteCalificar(usuario.getId_user());
    }

    private void setListeners() {
        llExcedente.setOnClickListener(this);
        tvMiPerfil.setOnClickListener(this);
        llSolicitarServ.setOnClickListener(this);
        llNotificaciones.setOnClickListener(this);
        llCalendario.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO: obtenerCalendario();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvMiPerfil:
                Intent in = new Intent(Home.this, MiPerfil.class);
                startActivity(in);
                break;
            case R.id.llSolicitarServ:
                Intent inServ = new Intent(Home.this, SolicitudServicio3.class);
                startActivity(inServ);
                break;
            case R.id.llNotificaciones:
                Intent inNot = new Intent(this, MisNotificaciones.class);
                startActivity(inNot);
                break;
            case R.id.llCalendario:
                Intent inCal = new Intent(this, Calendario.class);
                startActivity(inCal);
                break;
            case R.id.llExcedente:
                Intent inExc = new Intent(this, Excedentes.class);
                startActivity(inExc);
                break;
            default:break;
        }//switch
    }//onClick

    private void pendienteCalificar(String uid){
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(Home.this);
            dialogProgress.show();
        }
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        final Call<ResponsePendienteCalificar> responsePendienteCalificarCall = endpoints.pendienteCalificar(authToken, uid);
        responsePendienteCalificarCall.enqueue(new Callback<ResponsePendienteCalificar>() {
            @Override
            public void onResponse(Call<ResponsePendienteCalificar> call, Response<ResponsePendienteCalificar> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){
                    case "100": //Servicios pendientes por calificar
                        new DialogPendienteCalificar(Home.this, new DialogPendienteCalificar.RespuestaListener() {
                            @Override
                            public void onIrCalificar() {
                                Intent goCal = new Intent(Home.this, Calificar.class);
                                goCal.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(goCal);
                                finish();
                            }
                            @Override
                            public void onSalir() {
                                //Toast.makeText(Home.this, "Para solicitar un nuevo servicio debe calificar primero", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                        break;
                    case "102": //No hay pendientes para calificar va a Home
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
    }//private void pendienteCalificar

    public void obtenerCalendario() {
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        Call<ResponseHistorial> responseHistorialCall = endpoints.listarHistorial(authToken, usuario.getId_user(), "calendario");
        responseHistorialCall.enqueue(new Callback<ResponseHistorial>() {
            @Override
            public void onResponse(Call<ResponseHistorial> call, Response<ResponseHistorial> response) {
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        calendario = response.body().getData();
                        int nMiCalendar = calendario.size();
                        tvNserviciosProgramados.setText(String.valueOf(nMiCalendar)+ " programados");
                        break;
                    case "102":
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

    private void setTokenFirebase(){
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        Call<ResponseGeneral> responseGeneralCall = endpoints.actualizarTokenFirebase(authToken,
                usuario.getId_user(), sharedPreferenceManager.getFirebaseToken(this), "a");
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                if (response.isSuccessful()){
                    switch (response.body().getCode()){
                        case "100":
                            Log.e(TAG, "setTokenFirebase onResponse OK");
                            break;
                        case "102":
                            Log.e(TAG, "setTokenFirebase onResponse Fallo");
                            break;
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                Log.e(TAG, "setTokenFirebase onFailure");
            }
        });
    }
}
