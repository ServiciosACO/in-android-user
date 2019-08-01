package co.kubo.indiesco.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.dialog.DialogDirecciones;
import co.kubo.indiesco.dialog.DialogDosOpciones;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.modelo.InmuebleVO;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseInmueble;
import co.kubo.indiesco.restAPI.modelo.ResponseTasarServicio;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import co.kubo.indiesco.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SolicitudServicio extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

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
    @BindView(R.id.viewDirInactive)
    View viewDirInactive;
    @BindView(R.id.viewDirActive)
    View viewDirActive;

    Utils utils = new Utils();
    private DialogProgress dialogProgress;
    private MapFragment mapaDireccion;
    private GoogleMap googleMap;

    private ArrayList<InmuebleVO> inmuebleVOS = new ArrayList<>();
    private ArrayList<String> valorServicio = new ArrayList<>();
    private String tipoInmueble = "1", urgente = "no", dimension = "60", id_inmueble = "1", valorX = "0";
    private String id_direccion = "1", comentario = "Sin comentarios", fecha = "1 de enero de 2018", hora = "11:30AM";
    private boolean band1 = false, band2 = true, bandUrgente = true, bandTasarServicio = false, bandDir = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_servicio);
        ButterKnife.bind(this);
        mapaDireccion = (MapFragment) getFragmentManager().findFragmentById(R.id.mapaDireccion);
        if (mapaDireccion != null) {
            mapaDireccion.getMapAsync(this);
        }
        tvDir.setOnClickListener(this);
        imgBotonVolver.setOnClickListener(this);
        imgUrgente.setOnClickListener(this);
        llValor.setOnClickListener(this);
        spinnerInmueble.setPrompt("Selecciona");
        spinnerInmueble.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoInmueble = spinnerInmueble.getItemAtPosition(i).toString();
                //id_inmueble = inmuebleVOS.get(i).getIdTipoInmueble();
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
    public void onClick(final View view) {
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
                        setLatitudYLongitud(Double.parseDouble(lat), Double.parseDouble(lng));
                        tvDir.setText(dir);
                        viewDirInactive.setVisibility(View.GONE);
                        viewDirActive.setVisibility(View.VISIBLE);
                        bandDir = true;
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
            case R.id.llValor:
                //if (validacion()){
                    //crearServicio();
                //}
                break;
        }//switch
    }

    public boolean validacion(){
        if (utils.checkInternetConnection(this, true)){
            return false;
        }
        if (!bandDir){
            Toast.makeText(this, "Debe elegir una dirección valida para crear el servicio", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //<editor-fold desc="crearServicio">
    /*public void crearServicio(){
        dialogProgress = new DialogProgress(SolicitudServicio.this);
        dialogProgress.show();
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        final String id_user = usuario.getId_user();
        Call<ResponseCrearServicio> responseCrearServicioCall = endpoints.crearServicio(authToken, usuario.getId_user(), id_inmueble, dimension,
                valorX, id_direccion, fecha, urgente, hora, comentario);
        final Usuario finalUsuario = usuario;
        responseCrearServicioCall.enqueue(new Callback<ResponseCrearServicio>() {
            @Override
            public void onResponse(Call<ResponseCrearServicio> call, Response<ResponseCrearServicio> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        String id_solicitud = response.body().getData().getIdSolicitud();
                        String urlTransaccion = "http://indiescoapi.inkubo.co/servicios/resumen_pedido/" + id_user + "/" + id_solicitud;
                        Intent goPago = new Intent(SolicitudServicio.this, Transaccion.class);
                        goPago.putExtra("url", urlTransaccion);
                        goPago.putExtra("id_sol", id_solicitud);
                        startActivity(goPago);
                        break;
                    case "102":
                        Toast.makeText(SolicitudServicio.this, "No fue posible crear el servicio, intente de nuevo", Toast.LENGTH_LONG).show();
                        break;
                    case "120": //auth_token no valido
                        Log.e(TAG, "cod: 120, onResponse Token Invalido");
                        break;
                    default:break;
                }
            }
            @Override
            public void onFailure(Call<ResponseCrearServicio> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "onFailure crearServicio");
            }
        });
    }*/
    //</editor-fold>DG

    @Override
    public void onBackPressed() {
        new DialogDosOpciones(SolicitudServicio.this, "3", new DialogDosOpciones.RespuestaListener() {
            @Override
            public void onCancelar() {
            }
            @Override
            public void onAceptar() {
                finish();
            }
            @Override
            public void onSalir() {
            }
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
                        valorX = valorServicio.get(1);
                        llValor.setVisibility(View.VISIBLE);
                        DecimalFormat formateador = new DecimalFormat("###,###");
                        tvValor.setText("$" + formateador.format(Double.parseDouble(valorX)) + " COP");
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
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(SolicitudServicio.this);
            dialogProgress.show();
        }
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        Call<ResponseInmueble> responseInmuebleCall = endpoints.listarInmuebles(authToken);
        responseInmuebleCall.enqueue(new Callback<ResponseInmueble>() {
            @Override
            public void onResponse(Call<ResponseInmueble> call, Response<ResponseInmueble> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){
                    case "100":

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
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "listarTiposInmuebles, onFailure");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        if (googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setScrollGesturesEnabled(false);
        }

        final GoogleMap finalGoogleMap = googleMap;
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Double latitude = finalGoogleMap.getCameraPosition().target.latitude;
                Double longitude = finalGoogleMap.getCameraPosition().target.longitude;
                if (bandDir){
                    setLatitudYLongitud(latitude, longitude);
                    bandDir = false;
                }
            }
        });
    }

    public void setLatitudYLongitud(Double lati, Double longi) {
        googleMap.clear();
        LatLng latLng = new LatLng(lati, longi);
        MarkerOptions mp = new MarkerOptions()
                .position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin));
        googleMap.addMarker(mp);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}
