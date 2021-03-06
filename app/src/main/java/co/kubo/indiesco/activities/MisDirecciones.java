package co.kubo.indiesco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.adaptadores.MisDireccionesAdapter;
import co.kubo.indiesco.dialog.DialogAgregarDir;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.interfaces.IMisDireccionesPresenter;
import co.kubo.indiesco.interfaces.IMisDireccionesView;
import co.kubo.indiesco.modelo.Direccion;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.presenter.MisDireccionesPresenter;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisDirecciones extends AppCompatActivity implements View.OnClickListener, IMisDireccionesView {

    public static final String TAG = "MisDirecciones";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.rvDir)
    RecyclerView rvDir;
    @BindView(R.id.fabAgregar)
    FloatingActionButton fabAgregar;
    @BindView(R.id.llNoAddress)
    LinearLayout llNoAddress;

    Boolean flag = true;
    IMisDireccionesPresenter presenter;

    private DialogProgress dialogProgress;
    private ArrayList<Direccion> direccion = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_direcciones);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);
        fabAgregar.setOnClickListener(this);

        presenter = new MisDireccionesPresenter(this, getApplicationContext(), MisDirecciones.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.obtenerDirecciones();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBotonVolver:
                finish();
                break;
            case R.id.fabAgregar:
                if (flag){
                    Intent in = new Intent(MisDirecciones.this, ActivityNuevaDireccionSinGps.class);
                    in.putExtra("estado", "");
                    startActivity(in);
                } else {
                    Toast.makeText(this, "Has llegado al m??ximo de direcciones que puedes agregar", Toast.LENGTH_LONG).show();
                }

                break;
            default:break;
        }//switch
    }

    private void agregarDir(){
        new DialogAgregarDir(MisDirecciones.this, new DialogAgregarDir.RespuestaListener() {
            @Override
            public void onAgregar(String dir, String lat, String lng, String complemento, String ciudad) {
                if (dialogProgress == null) {
                    dialogProgress = new DialogProgress(MisDirecciones.this);
                    dialogProgress.show();
                }
                String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
                RestApiAdapter restApiAdapter = new RestApiAdapter();
                Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
                Usuario usuario = new Usuario();
                usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
                Call<ResponseGeneral> responseGeneralCall = endpoints.agregarDireccion(authToken, usuario.getId_user(), dir, lat, lng, complemento, ciudad,"");
                responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
                    @Override
                    public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                        if (dialogProgress.isShowing()) {
                            dialogProgress.dismiss();
                        }
                        String code = response.body().getCode();
                        switch (code){
                            case "100": //OK
                                Toast.makeText(getApplicationContext(), "Se agreg?? la direcci??n con ??xito", Toast.LENGTH_LONG).show();
                                //Para refrescar la pantalla con la nueva data
                                Intent in = new Intent(MisDirecciones.this, MisDirecciones.class);
                                in.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                finish();
                                break;
                            case "102": //Fallo
                                Toast.makeText(getApplicationContext(), "Algo fallo intente de nuevo", Toast.LENGTH_LONG).show();
                                break;
                            case "120": //auth_token no valido
                                break;
                            default: break;
                        }//switch
                    }
                    @Override
                    public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                        if (dialogProgress.isShowing()) {
                            dialogProgress.dismiss();
                        }
                        Log.e(TAG, "onFailure agregarDir");
                    }
                });
            }
            @Override
            public void onSalir() {
            }
        }).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void noAddresses(){
        llNoAddress.setVisibility(View.VISIBLE);
        rvDir.setVisibility(View.GONE);
    }

    @Override
    public void disableButtonAddAddress(Boolean flagButton) {
        flag = flagButton;
    }

    @Override
    public void generarLinearLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvDir.setLayoutManager(llm);
    }

    @Override
    public void inicializarAdaptadorRvDirecciones(MisDireccionesAdapter misDireccionesAdapter) {
        rvDir.setAdapter(misDireccionesAdapter);
    }

    @Override
    public MisDireccionesAdapter crearAdaptadorDirecciones(ArrayList<Direccion> direccions) {
        MisDireccionesAdapter misDireccionesAdapter = new MisDireccionesAdapter(direccions, MisDirecciones.this, this);
        return misDireccionesAdapter;
    }
}
