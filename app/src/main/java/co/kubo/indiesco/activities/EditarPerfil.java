package co.kubo.indiesco.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.adaptadores.AdapterAutocomplete;
import co.kubo.indiesco.asincronasMapa.AsincronaGetDetalleDireccionGoogle;
import co.kubo.indiesco.asincronasMapa.AsincronaGetDireccionPorCoordenadas;
import co.kubo.indiesco.asincronasMapa.AsincronaGetDireccionesGoogle;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import co.kubo.indiesco.utils.Utils;
import retrofit2.Call;

public class EditarPerfil extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private static final String TAG = "EditarPerfil";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.imgFoto)
    ImageView imgFoto;
    @BindView(R.id.editNom)
    EditText editNom;
    @BindView(R.id.editCorreo)
    EditText editCorreo;
    @BindView(R.id.editTelf)
    EditText editTelf;
    @BindView(R.id.editDir)
    AutoCompleteTextView editDir;
    @BindView(R.id.editComplementoDir)
    EditText editComplementoDir;
    @BindView(R.id.editCity)
    EditText editCity;
    @BindView(R.id.fabDone)
    FloatingActionButton fabDone;
    @BindView(R.id.scrollViewEdit)
    ScrollView scrollViewEdit;
    @BindView(R.id.imagetrans)
    ImageView imagetrans;

    private DialogProgress dialogProgress;
    Utils utils = new Utils();
    private MapFragment mapaDireccion;
    private GoogleMap googleMap;
    private Double latitudDireccion = 0.0;
    private Double longitudDireccion = 0.0;
    private boolean isMapa, cargarDireccionesGoogle, bandX = false, bandDireccionValida, selDireccion;
    private boolean bandPermiso = false;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 11f;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private double lati, longi;
    private boolean bandNombre = false, bandEmail = false, bandCel = false, bandDir = false, bandCiudad = false, bandOK = false;
    private String nombre = "", email = "", telefono = "", foto = "http:\\/\\/indiescoapi.inkubo.co\\/imgs_usuarios\\/-";
    private String direccion = "",complemento = "-", ciudad = "";
    private int scrollY, oldScrollY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        ButterKnife.bind(this);
        getLocationPermission();
        fabDone.setOnClickListener(this);
        imgFoto.setOnClickListener(this);
        imgBotonVolver.setOnClickListener(this);
        mapaDireccion = (MapFragment) getFragmentManager().findFragmentById(R.id.mapaDireccion);

        hideSoftKeyboard();
        setlistenerEditText();

        /**Para desaparecer el FAB cuando hago scroll*/
        scrollViewEdit.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                scrollY = scrollViewEdit.getScrollY();
                Log.e("scroll", scrollY + " " + oldScrollY);
                if (scrollY > oldScrollY) {
                    fabDone.hide();
                    oldScrollY = scrollY;
                    if (scrollViewEdit.getChildAt(0).getBottom() <= (scrollViewEdit.getHeight() + scrollViewEdit.getScrollY()) || scrollY <= 0)
                        fabDone.show();
                } else {
                    fabDone.show();
                    oldScrollY = scrollY;
                }
            }
        });

        if (mapaDireccion != null) {
            mapaDireccion.getMapAsync(this);
        }
    }

    private void setlistenerEditText() {
        editNom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandNombre = true;
                } else {
                    bandNombre = false;
                }
                validarFABVerde();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0 || bandX) {
                    bandEmail = true;
                } else {
                    bandEmail = false;
                }
                validarFABVerde();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editTelf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandCel = true;
                } else {
                    bandCel = false;
                }
                validarFABVerde();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editDir.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandDir = true;
                } else {
                    bandDir = false;
                }
                validarFABVerde();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandCiudad = true;
                } else {
                    bandCiudad = false;
                }
                validarFABVerde();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        /**Para hacer scroll en el mapa verticalmente*/
        imagetrans.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scrollViewEdit.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;
                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollViewEdit.requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        scrollViewEdit.requestDisallowInterceptTouchEvent(true);
                        return false;
                    default:
                        return true;
                }
            }
        });

        editDir.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkLocation();
                if (!bandPermiso && gps_enabled){
                    getDeviceLocation();
                    bandPermiso = true;
                }
                if (!isMapa) {
                    bandDireccionValida = false;
                    if (s.toString().length() >= 2) {
                        if (utils.checkInternetConnection(EditarPerfil.this, true)) {
                            new AsincronaGetDireccionesGoogle(EditarPerfil.this, s.toString().trim(), 0).execute();
                            /*if (cargarDireccionesGoogle) {
                                cargarDireccionesGoogle = false;
                                new AsincronaGetDireccionesGoogle(Registro.this, s.toString().trim()).execute();
                            }*/
                        }
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editDir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                selDireccion = true;
                //editDireccion.setBackgroundResource(R.drawable.xml_edit_text_direccion);
                bandDireccionValida = true;
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editDir.getWindowToken(), 0);
                } catch (Exception e) {

                }
                String placeId = ((String[]) arg0.getItemAtPosition(arg2))[1];
                direccion ="";
                if (utils.checkInternetConnection(EditarPerfil.this, true)) {
                    new AsincronaGetDetalleDireccionGoogle(EditarPerfil.this, placeId, 0).execute();
                }
            }
        });
    }

    private void validarFABVerde() {
        if (bandNombre && (bandX || bandEmail) && bandCel && bandDir && bandCiudad) {
            fabDone.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorVerde)));
            bandOK = true;
        } else {
            fabDone.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_hint)));
            bandOK = false;
        }
    }//validarFABVerde

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            case R.id.fabDone:
                if (validacion()){

                }
                break;
            default:break;
        }//switch
    }//onClick

    private Boolean validacion() {
        if (!utils.checkInternetConnection(this, true)) {
            return false;
        }
        /*if (editNom.getText().toString().trim().equalsIgnoreCase("")) {
            editNom.setError("El nombre es requerido");
            return false;
        }
        if (!utils.isEmailValid(editCorreo.getText().toString())) {
            editCorreo.setError("Debe ingresar un correo electrónico valido");
            return false;
        }
        if (editTelf.getText().toString().trim().equalsIgnoreCase("")) {
            editTelf.setError("El celular es requerido");
            return false;
        }
        if (editTelf.getText().toString().trim().length() != 10) {
            editTelf.setError("Ingrese un numero de celular de 10 digitos valido");
            return false;
        }*/
        if (editDir.getText().toString().trim().equalsIgnoreCase("")) {
            editDir.setError("La dirección es requerida");
            return false;
        }
        if (editCity.getText().toString().trim().equalsIgnoreCase("")) {
            editCity.setError("La ciudad es requerida");
            return false;
        }
        return true;
    }//validacion

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void getLocationPermission(){
        Log.e(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(ContextCompat.checkSelfPermission(EditarPerfil.this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionsGranted = true;
            //initMap();
        }else{
            ActivityCompat.requestPermissions(EditarPerfil.this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }//private void getLocationPermission

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.e(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.e(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initMap();
                }
            }
        }
    }//public void onRequestPermissionsResult

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        if (googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
        }

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                latitudDireccion = googleMap.getCameraPosition().target.latitude;
                longitudDireccion = googleMap.getCameraPosition().target.longitude;
                isMapa = false;
                editDir.setAdapter(null);
                if (utils.checkInternetConnection(EditarPerfil.this, true)) {
                    AsincronaGetDireccionPorCoordenadas asyncDir = new AsincronaGetDireccionPorCoordenadas(String.valueOf(latitudDireccion), String.valueOf(longitudDireccion), EditarPerfil.this, 0);
                    asyncDir.execute();
                }
            }
        });
    }

    public void setDireccion(String direccion, String ciudad) {
        if(direccion.length() != 0){
            editDir.setText(direccion);
            editCity.setText(ciudad);
            this.ciudad = ciudad;
            this.direccion = direccion;
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        googleMap.clear();
        Log.e(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        googleMap.animateCamera(update);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin));
        googleMap.addMarker(options);
        hideSoftKeyboard();
    }//private void moveCamera

    /**Para colocar autocomplete para el mapa*/
    public void llenarAutocomplete(ArrayList<String[]> lista) {
        cargarDireccionesGoogle = true;
        AdapterAutocomplete adapter = new AdapterAutocomplete(this, R.layout.item_direccion_google, R.id.txtItemDireccionGoogle, lista);
        editDir.setAdapter(adapter);
    }
    public void setLatitudYLongitud(String[] latitudYLongitud) {
        googleMap.clear();
        this.lati  = Double.parseDouble(latitudYLongitud[0]);
        this.longi = Double.parseDouble(latitudYLongitud[1]);
        LatLng latLng = new LatLng(lati, longi);
        MarkerOptions mp = new MarkerOptions()
                .position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin));
        googleMap.addMarker(mp);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    private void checkLocation() {
        LocationManager lm = (LocationManager) EditarPerfil.this.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {}
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {}
        if (!gps_enabled && !network_enabled) {
            /*
            new DosOpcionesDialog(Registro.this, "x", new DosOpcionesDialog.RespuestaListener() {
                @Override
                public void onOpcionNo() {
                }
                @Override
                public void onOpcionSi() {
                    gps_enabled = true;
                    network_enabled = true;
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            }).show();
            */
        }//if
    }//private void checkLocation

    private void getDeviceLocation(){
        checkLocation();
        if (gps_enabled){
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(EditarPerfil.this);
            try{
                if(mLocationPermissionsGranted){
                    final Task location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                try{
                                    Log.e(TAG, "onComplete: found location!");
                                    Location currentLocation = (Location) task.getResult();
                                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                                    Log.e(TAG, currentLocation.getLatitude()+","+currentLocation.getLongitude());
                                    lati = currentLocation.getLatitude();
                                    longi = currentLocation.getLongitude();
                                }catch (Exception e){
                                    Log.e(TAG, "onComplete: found location!");
                                }
                            }else{
                                Log.e(TAG, "Exception getDeviceLocation");
                            }
                        }
                    });
                }else {
                    getLocationPermission();
                }
            }catch (SecurityException e){
                Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
            }
        }
    }//private void getDeviceLocation

    private void actualizarDir(){
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(EditarPerfil.this);
            dialogProgress.show();
        }
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        //Call<ResponseGeneral> responseGeneralCall = endpoints.actualizarDireccion(authToken, usuario.get)
    }//private void actualizarDir
}
