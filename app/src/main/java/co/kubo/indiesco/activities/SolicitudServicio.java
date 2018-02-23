package co.kubo.indiesco.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.dialog.DialogDirecciones;
import co.kubo.indiesco.dialog.DialogDosOpciones;
import co.kubo.indiesco.modelo.Inmueble;
import co.kubo.indiesco.modelo.TasarServicio;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseInmueble;
import co.kubo.indiesco.restAPI.modelo.ResponseTasarServicio;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolicitudServicio extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "SolicitudServicio";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.tvSubTituloServicio)
    TextView tvSubTituloServicio;
    @BindView(R.id.spinnerInmueble)
    Spinner spinnerInmueble;
    @BindView(R.id.seekBarDimension)
    SeekBar seekBarDimension;
    @BindView(R.id.tvDimension)
    TextView tvDimension;
    @BindView(R.id.tvDir)
    TextView tvDir;
    @BindView(R.id.webViewMapServicio)
    WebView webViewMapServicio;
    @BindView(R.id.FechaServ)
    DatePicker FechaServ;
    @BindView(R.id.imgUrgente)
    ImageView imgUrgente;
    @BindView(R.id.horaServ)
    TimePicker horaServ;
    @BindView(R.id.llValor)
    LinearLayout llValor;
    @BindView(R.id.tvValor)
    TextView tvValor;

    private ArrayList<Inmueble> inmuebles = new ArrayList<>();
    private ArrayList<String> valorServicio = new ArrayList<>();
    private String tipoInmueble = "", urgente = "no", dimension = "60", id_inmueble = "-1";
    private boolean band1 = false, band2 = true, bandUrgente = true, bandTasarServicio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_servicio);
        ButterKnife.bind(this);
        tvDir.setOnClickListener(this);
        imgBotonVolver.setOnClickListener(this);
        imgUrgente.setOnClickListener(this);
        spinnerInmueble.setPrompt("Selecciona");
        spinnerInmueble.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoInmueble = spinnerInmueble.getItemAtPosition(i).toString();
                id_inmueble = inmuebles.get(i).getId_tipo_inmueble();
                band1 = true;
                tasarServicio();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        seekBarDimension.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorVerde), PorterDuff.Mode.SRC_IN);
        seekBarDimension.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch (i){
                    case 0:
                        tvDimension.setText(getResources().getString(R.string.dimension_1));
                        dimension = "60";
                        break;
                    case 1:
                        tvDimension.setText(getResources().getString(R.string.dimension_2));
                        dimension = "70";
                        if (!band1){
                            Toast.makeText(SolicitudServicio.this, "Recuerda seleccionar el tipo de inmueble", Toast.LENGTH_SHORT).show();
                        }else{
                            tasarServicio();
                        }
                        break;
                    case 2:
                        tvDimension.setText(getResources().getString(R.string.dimension_3));
                        dimension = "80";
                        if (!band1){
                            Toast.makeText(SolicitudServicio.this, "Recuerda seleccionar el tipo de inmueble", Toast.LENGTH_SHORT).show();
                        }else{
                            tasarServicio();
                        }
                        break;
                    case 3:
                        tvDimension.setText(getResources().getString(R.string.dimension_4));
                        dimension = "90";
                        if (!band1){
                            Toast.makeText(SolicitudServicio.this, "Recuerda seleccionar el tipo de inmueble", Toast.LENGTH_SHORT).show();
                        }else{
                            tasarServicio();
                        }
                        break;
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });



        listarTiposInmuebles();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            case R.id.imgUrgente:
                if (!bandUrgente){
                    imgUrgente.setImageResource(R.drawable.switch_inactive);
                    urgente = "no";
                    bandUrgente = true;
                    if (bandTasarServicio && band1 && band2){
                        tasarServicio();
                    }
                }else{
                    imgUrgente.setImageResource(R.drawable.switch_active);
                    urgente = "si";
                    bandUrgente = false;
                    if (band1 && band2){
                        tasarServicio();
                    }
                    bandTasarServicio = true;
                }
                break;
            case R.id.tvDir:
                new DialogDirecciones(SolicitudServicio.this, new DialogDirecciones.RespuestaListener() {
                    @Override
                    public void onSelectDir(String dir, String lat, String lng, String complemento, String ciudad) {
                        String url = "http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=15&size=200x200&sensor=false";
                        webViewMapServicio.loadUrl(url);
                    }
                    @Override
                    public void onIrMisDir() {
                        Intent inDir = new Intent(SolicitudServicio.this, MisDirecciones.class);
                        startActivity(inDir);
                    }
                    @Override
                    public void onSalir() {
                    }
                }).show();
                break;
        }//switch
    }

    @Override
    public void onBackPressed() {
        new DialogDosOpciones(SolicitudServicio.this, "2", new DialogDosOpciones.RespuestaListener() {
            @Override
            public void onCancelar() {}
            @Override
            public void onAceptar() {
                finish();
            }
            @Override
            public void onSalir() {}
        }).show();
    }//public void onBackPressed

    private void tasarServicio(){
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        final Call<ResponseTasarServicio> responseTasarServicioCall = endpoints.tasarServicio(authToken, id_inmueble, dimension, urgente);
        responseTasarServicioCall.enqueue(new Callback<ResponseTasarServicio>() {
            @Override
            public void onResponse(Call<ResponseTasarServicio> call, Response<ResponseTasarServicio> response) {
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        valorServicio = response.body().getData();
                        String valor = valorServicio.get(1);
                        llValor.setVisibility(View.VISIBLE);
                        tvValor.setText("$" + valor);
                        break;
                    case "102":
                        Log.e(TAG, "tasarServicio, Cod: 102 No hay datos");
                        break;
                    case "120":
                        Log.e(TAG, "tasarServicio, Cod: 120 auth token invalido");
                        break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseTasarServicio> call, Throwable t) {
                Log.e(TAG, "tasarServicio, onFailure");
            }
        });
    }

    private void listarTiposInmuebles(){
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        Call<ResponseInmueble> responseInmuebleCall = endpoints.listarInmuebles(authToken);
        responseInmuebleCall.enqueue(new Callback<ResponseInmueble>() {
            @Override
            public void onResponse(Call<ResponseInmueble> call, Response<ResponseInmueble> response) {
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        inmuebles = response.body().getData();
                        ArrayList<String> tipoInm = new ArrayList<>();
                        for (int i = 0; i < inmuebles.size(); i++){
                            tipoInm.add(inmuebles.get(i).getInmueble());
                        }//for
                        ArrayAdapter<String> inmuebleArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, tipoInm);
                        spinnerInmueble.setAdapter(inmuebleArrayAdapter);
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
            public void onFailure(Call<ResponseInmueble> call, Throwable t) {
                Log.e(TAG, "listarTiposInmuebles, onFailure");
            }
        });
    }
}
