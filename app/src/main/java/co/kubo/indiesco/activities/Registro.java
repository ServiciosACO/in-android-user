package co.kubo.indiesco.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.adaptadores.AdapterAutocomplete;
import co.kubo.indiesco.asincronasMapa.AsincronaGetDetalleDireccionGoogle;
import co.kubo.indiesco.asincronasMapa.AsincronaGetDireccionPorCoordenadas;
import co.kubo.indiesco.asincronasMapa.AsincronaGetDireccionesGoogle;
import co.kubo.indiesco.dialog.DialogImagenPerfil;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.modelo.ValidacionDirecciones;
import co.kubo.indiesco.modelo.direccionesGoogleVO;
import co.kubo.indiesco.restAPI.ConstantesRestApi;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseFoto;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.restAPI.modelo.ResponseRegistro;
import co.kubo.indiesco.restAPI.modelo.ResponseValidacion;
import co.kubo.indiesco.utils.Constantes;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import co.kubo.indiesco.utils.Singleton;
import co.kubo.indiesco.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private static final String TAG = "Registro";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.imgFoto)
    ImageView imgFoto;
    @BindView(R.id.editNombre)
    EditText editNombre;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editCelular)
    EditText editCelular;
    @BindView(R.id.editDireccion)
    AutoCompleteTextView editDireccion;
    @BindView(R.id.editComplemento)
    EditText editComplemento;
    @BindView(R.id.editCiudad)
    EditText editCiudad;
    @BindView(R.id.editpass1)
    TextInputEditText editpass1;
    @BindView(R.id.editpass2)
    TextInputEditText editpass2;
    @BindView(R.id.fabSiguiente)
    FloatingActionButton fabSiguiente;
    @BindView(R.id.scrollViewRegistro)
    ScrollView scrollViewRegistro;
    @BindView(R.id.inputPass1)
    TextInputLayout inputPass1;
    @BindView(R.id.inputPass2)
    TextInputLayout inputPass2;
    @BindView(R.id.llSplashRegistro)
    LinearLayout llSplashRegistro;
    @BindView(R.id.imagetrans)
    ImageView imagetrans;

    private DialogProgress dialogProgress;
    private MapFragment mapaDireccion;
    private GoogleMap googleMap;
    private Boolean cargoMapa = false;
    private Boolean cargarDireccion = false;
    private ListView listaDirecciones;
    private List<direccionesGoogleVO> listaDatos = new ArrayList<>();
    private Double latitudDireccion = 0.0;
    private Double longitudDireccion = 0.0;
    private boolean primeraVezMarker, cargarDireccionesGoogle, isMapa, selDireccion, bandSubio, bandPalabra, bandError, bandDireccionValida;
    private Boolean bandSubir = false;
    private Boolean bandPonerDir = true;
    private float zoomActual = 0;
    private Singleton general = Singleton.getInstance();
    private Boolean bandLista = true;
    private boolean bandPermiso = false;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 11f;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private double lati, longi;

    private int position = 0;
    private Uri imageUri;
    private Boolean opcionesCamara = false, bandX = false;
    private static int takeImage = 2;
    private int selectImage = 7;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static final int OPTION_CAMERA = 0;
    private Boolean bandFoto = false, bandY = false;
    private int scrollY, oldScrollY = 0;
    Utils utils = new Utils();
    Animation animShake;
    private File file;
    private String val;
    private boolean bandNombre = false, bandEmail = false, bandCel = false, bandDir = false, bandCiudad = false, bandPass1 = false, bandPass2 = false, bandOK = false;
    private String nombre = "", email = "", password = "", plataforma = "a", token = "0", telefono = "", foto = "http:\\/\\/indiescoapi.inkubo.co\\/imgs_usuarios\\/-";
    private String direccion = "",complemento = "-", ciudad = "";


    private List<ValidacionDirecciones> listCiudadesDisponibles = new ArrayList<>();
    private String idCiudad = "";
    boolean ciudadAvaible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);
        getLocationPermission();
        animShake = AnimationUtils.loadAnimation(Registro.this, R.anim.shake);
        fabSiguiente.setOnClickListener(this);
        imgFoto.setOnClickListener(this);
        imgBotonVolver.setOnClickListener(this);
        mapaDireccion = (MapFragment) getFragmentManager().findFragmentById(R.id.mapaDireccion);

        hideSoftKeyboard();
        /**Para desaparecer el FAB cuando hago scroll*/
        scrollViewRegistro.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                scrollY = scrollViewRegistro.getScrollY();
                Log.e("scroll", scrollY + " " + oldScrollY);
                if (scrollY > oldScrollY) {
                    fabSiguiente.hide();
                    oldScrollY = scrollY;
                    if (scrollViewRegistro.getChildAt(0).getBottom() <= (scrollViewRegistro.getHeight() + scrollViewRegistro.getScrollY()) || scrollY <= 0)
                        fabSiguiente.show();
                } else {
                    fabSiguiente.show();
                    oldScrollY = scrollY;
                }
            }
        });

        if (mapaDireccion != null) {
            mapaDireccion.getMapAsync(this);
        }

        Bundle parametros = getIntent().getExtras();
        if (parametros == null) {
            editEmail.setFocusableInTouchMode(true);
            val = "0";
        } else {
            email = parametros.getString("Email");//email
            editEmail.setText(email);
            bandX = true;
            val = "1";
        }//else

        setlistenerEditText();

        ciudadesDispo();

    }//onCreate

    private void setlistenerEditText() {
        editNombre.addTextChangedListener(new TextWatcher() {
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
        editEmail.addTextChangedListener(new TextWatcher() {
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
        editCelular.addTextChangedListener(new TextWatcher() {
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
        editDireccion.addTextChangedListener(new TextWatcher() {
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
        editCiudad.addTextChangedListener(new TextWatcher() {
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
        editpass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandPass1 = true;
                } else {
                    bandPass1 = false;
                }
                validarFABVerde();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editpass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandPass2 = true;
                } else {
                    bandPass2 = false;
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
                        scrollViewRegistro.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;
                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollViewRegistro.requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        scrollViewRegistro.requestDisallowInterceptTouchEvent(true);
                        return false;
                    default:
                        return true;
                }
            }
        });

        editDireccion.addTextChangedListener(new TextWatcher() {
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
                        if (utils.checkInternetConnection(Registro.this, true)) {
                            new AsincronaGetDireccionesGoogle(Registro.this, s.toString().trim(), 1).execute();
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
        editDireccion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                selDireccion = true;
                //editDireccion.setBackgroundResource(R.drawable.xml_edit_text_direccion);
                bandDireccionValida = true;
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editDireccion.getWindowToken(), 0);
                } catch (Exception e) {

                }
                String placeId = ((String[]) arg0.getItemAtPosition(arg2))[1];
                direccion ="";
                if (utils.checkInternetConnection(Registro.this, true)) {
                    new AsincronaGetDetalleDireccionGoogle(Registro.this, placeId, 1).execute();
                }
            }
        });
    }

    private void validarFABVerde() {
        if (bandNombre && (bandX || bandEmail) && bandCel && bandDir && bandCiudad && bandPass1 && bandPass2) {
            fabSiguiente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorVerde)));
            bandOK = true;
        } else {
            fabSiguiente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_hint)));
            bandOK = false;
        }
    }//validarFABVerde

    private Boolean validacion() {
        if (!utils.checkInternetConnection(this, true)) {
            return false;
        }
        if (editNombre.getText().toString().trim().equalsIgnoreCase("")) {
            editNombre.setError("El nombre es requerido");
            return false;
        }
        if (val.equals("0")) {
            if (editEmail.getText().toString().trim().equalsIgnoreCase("")) {
                editEmail.setError("El correo electrónico es requerido");
                return false;
            }
        }
        if (!utils.isEmailValid(editEmail.getText().toString())) {
            editEmail.setError("Debe ingresar un correo electrónico valido");
            return false;
        }
        if (editCelular.getText().toString().trim().equalsIgnoreCase("")) {
            editCelular.setError("El celular es requerido");
            return false;
        }
        if (editCelular.getText().toString().trim().length() != 10) {
            editCelular.setError("Ingrese un numero de celular de 10 digitos valido");
            return false;
        }
        if (editDireccion.getText().toString().trim().equalsIgnoreCase("")) {
            editDireccion.setError("La dirección es requerida");
            return false;
        }
        if (editCiudad.getText().toString().trim().equalsIgnoreCase("")) {
            editCiudad.setError("La ciudad es requerida");
            return false;
        }
        if (editpass1.getText().toString().trim().equalsIgnoreCase("")) {
            editpass1.setError("La contraseña es requerida");
            return false;
        }
        if (editpass2.getText().toString().trim().equalsIgnoreCase("")) {
            editpass2.setError("La contraseña es requerida");
            return false;
        }
        if (editpass1.getText().toString().trim().length() < 8) {
            Toast.makeText(this, "La contraseña debe tener mínimo 8 dígitos", Toast.LENGTH_SHORT).show();
            //editpass1.setError("La contraseña debe tener mínimo 8 dígitos");
            return false;
        }
        if (editpass2.getText().toString().trim().length() < 8) {
            Toast.makeText(this, "La contraseña debe tener mínimo 8 dígitos", Toast.LENGTH_SHORT).show();
            //editpass2.setError("La contraseña debe tener mínimo 8 dígitos");
            return false;
        }
        if (!editpass1.getText().toString().equals(editpass2.getText().toString())) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            //inputPass1.setError("Las contraseñas no coinciden");
            inputPass1.startAnimation(animShake);
            inputPass2.startAnimation(animShake);
            return false;
        }

        return true;
    }//validacion

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabSiguiente:
                if (validacion() && bandOK) {
                    fabSiguiente.setEnabled(false);
                    nombre = editNombre.getText().toString();
                    email = editEmail.getText().toString();
                    password = editpass1.getText().toString();
                    telefono = editCelular.getText().toString();
                    direccion = editDireccion.getText().toString();
                    complemento = editComplemento.getText().toString();
                    ciudad = editCiudad.getText().toString();
                    if (ciudadAvaible){
                        validarEmail();
                    }else{
                        Toast.makeText(Registro.this, "Esta ciudad no tiene disponibilidad", Toast.LENGTH_LONG).show();
                    }

                }//if
                break;
            case R.id.imgFoto:
                new DialogImagenPerfil(Registro.this, new DialogImagenPerfil.RespuestaListener() {
                    @Override
                    public void onCamara() {
                        position = 0;
                        opcionFoto();
                    }
                    @Override
                    public void onGaleria() {
                        position = 1;
                        opcionFoto();
                    }
                    @Override
                    public void onSalir() {
                        return;
                    }
                }).show();
                break;
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

    private void splashRegistro() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent in = new Intent(Registro.this, Home.class);
                setResult(RESULT_OK, in);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
            }//run
        }, Constantes.tiempoSplash);
    }//splashRegistro

    private void opcionFoto() {
        if (position == 0) {
            opcionesCamara = true;
            if (ActivityCompat.checkSelfPermission(Registro.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Registro.this, new String[]{Manifest.permission.CAMERA}, OPTION_CAMERA);
            } else {
                String[] PERMISSIONS_STORAGE_CAMERA = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                int permission_camera = ActivityCompat.checkSelfPermission(Registro.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission_camera != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Registro.this, PERMISSIONS_STORAGE_CAMERA, REQUEST_EXTERNAL_STORAGE);
                } else {
                    File myDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", Registro.this.getPackageName());
                    if (!myDir.exists()) {
                        myDir.mkdir();
                    }
                    File fileImage = new File(myDir, "foto.jpg");
                    if (fileImage.exists())
                        fileImage.delete();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageUri = FileProvider.getUriForFile(Registro.this, "co.kubo.indiesco", fileImage);
                    } else {
                        imageUri = Uri.fromFile(fileImage);
                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, takeImage);
                }
            }
        } else if (position == 1) {
            opcionesCamara = false;
            String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            int permission = ActivityCompat.checkSelfPermission(Registro.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Registro.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, selectImage);
            }
        }
    }//opcionFoto

    public void validarEmail() {
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(Registro.this);
            dialogProgress.show();
        }
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        String url = ConstantesRestApi.URL_VALIDAR_EMAIL + email;
        Call<ResponseGeneral> responseGeneralCall = endpoints.validarEmail(authToken, url);
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code) {
                    case "100": //Cuenta exitente va a login
                        Toast.makeText(Registro.this, "La cuenta de correo electronico ya existe", Toast.LENGTH_LONG).show();
                        Intent goLogin = new Intent(Registro.this, Login.class);
                        goLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goLogin);
                        finish();
                        break;
                    case "101": //Email no existe va a crear cuenta
                        hideSoftKeyboard();
                        crearCuenta();
                        break;
                    case "103": // Cuenta inactiva
                        Toast.makeText(Registro.this, "Su cuenta se encuentra inactiva y ya existe en la base de datos", Toast.LENGTH_LONG).show();
                        break;
                    case "120": //auth_token no valido
                        break;
                }//switch
            }

            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "onFailure validarEmail");
            }
        });
    }//public void validarEmail

    private void crearCuenta() {
        //if (dialogProgress == null) {
            dialogProgress = new DialogProgress(Registro.this);
            dialogProgress.show();
        //}
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        final String passSha1 = Utils.sha1Encrypt(password);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Call<ResponseRegistro> responseRegistroCall = endpoints.registro(authToken, nombre, email, passSha1, token, plataforma, telefono);
        responseRegistroCall.enqueue(new Callback<ResponseRegistro>() {
            @Override
            public void onResponse(Call<ResponseRegistro> call, Response<ResponseRegistro> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code) {
                    case "100": //OK
                        //Servicio para crear direccion
                        crearDireccion(passSha1, response.body().getData().getUid());
                        break;
                    case "102": //Fallo
                        Toast.makeText(Registro.this, "Algo fallo intente de nuevo", Toast.LENGTH_LONG).show();
                        break;
                    case "120": //auth_token no valido
                        break;
                    default:
                        break;
                }//switch
            }

            @Override
            public void onFailure(Call<ResponseRegistro> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "onFailure registro");
            }
        });
    }

    private void crearDireccion(final String passSHA1, final String uid) {

        dialogProgress = new DialogProgress(Registro.this);
        dialogProgress.show();

        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Call<ResponseGeneral> responseGeneralCall = endpoints.agregarDireccion(authToken, uid, direccion, String.valueOf(lati), String.valueOf(longi), complemento, ciudad, idCiudad);
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code) {
                    case "100": //OK
                        Usuario usuario = new Usuario();
                        usuario.setId_user(uid);
                        usuario.setName(nombre);
                        usuario.setEmail(email);
                        usuario.setContraseña(passSHA1);
                        usuario.setCelular(telefono);
                        usuario.setDireccion(direccion);
                        usuario.setLatitud(String.valueOf(lati));
                        usuario.setLongitud(String.valueOf(longi));
                        usuario.setComplemento(complemento);
                        usuario.setCiudad(ciudad);

                        if (bandFoto) { //Si cargo foto se va al servicio cargar foto
                            crearFoto(usuario, uid, file);
                        } else { //Si no cargo foto se va a Home
                            usuario.setFoto(foto);
                            SharedPreferenceManager.setInfoUsuario(getApplicationContext(), usuario);
                            SharedPreferenceManager.setLoged(Registro.this, true);
                            fabSiguiente.setVisibility(View.GONE);
                            scrollViewRegistro.setVisibility(View.GONE);
                            llSplashRegistro.setVisibility(View.VISIBLE);
                            splashRegistro();
                        }//else
                        break;
                    case "102": //Fallo
                        Toast.makeText(Registro.this, "Algo fallo intente de nuevo", Toast.LENGTH_LONG).show();
                        break;
                    case "120": //auth_token no valido
                        break;
                    default:
                        break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "onFailure registro");
            }
        });
    }

    private void crearFoto(final Usuario usuario, String id, File file) {

        dialogProgress = new DialogProgress(Registro.this);
        dialogProgress.show();

        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto", file.getName(), reqFile);
        RequestBody idUser = RequestBody.create(MediaType.parse("text/plain"), id);

        Call<ResponseFoto> ResponseFotoCall = endpoints.guardarFoto(authToken, idUser, body);
        ResponseFotoCall.enqueue(new Callback<ResponseFoto>() {
            @Override
            public void onResponse(Call<ResponseFoto> call, Response<ResponseFoto> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code) {
                    case "100": //OK
                        usuario.setFoto(response.body().getFoto());
                        SharedPreferenceManager.setInfoUsuario(getApplicationContext(), usuario);
                        SharedPreferenceManager.setLoged(Registro.this, true);
                        fabSiguiente.setVisibility(View.GONE);
                        scrollViewRegistro.setVisibility(View.GONE);
                        llSplashRegistro.setVisibility(View.VISIBLE);
                        splashRegistro();
                        break;
                    case "102": //Fallo
                        Toast.makeText(Registro.this, "Algo fallo intente de nuevo", Toast.LENGTH_LONG).show();
                        break;
                    case "120": //auth_token no valido
                        break;
                    default:
                        break;
                }//switch
            }

            @Override
            public void onFailure(Call<ResponseFoto> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "onFailure crearFoto");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case OPTION_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    int permission = ActivityCompat.checkSelfPermission(Registro.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Registro.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                    } else {
                        File myDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", Registro.this.getPackageName());
                        if (!myDir.exists()) {
                            myDir.mkdir();
                        }
                        File fileImage = new File(myDir, "foto.jpg");
                        if (fileImage.exists())
                            fileImage.delete();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            imageUri = FileProvider.getUriForFile(Registro.this, "co.kubo.indiesco", fileImage);
                        } else {
                            imageUri = Uri.fromFile(fileImage);
                        }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, takeImage);
                    }
                } else {
                    /*final DialogOpcionSenc dialogConfig = new DialogOpcionSenc(this, "-", getResources().getString(R.string.noCamara), getResources().getString(R.string.configuracion), 0);
                    dialogConfig.setPositveButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogConfig.dismiss();
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    });
                    dialogConfig.setCerrarButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogConfig.dismiss();
                        }
                    });
                    dialogConfig.show();*/
                }
                return;
            }
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (opcionesCamara) {
                        File myDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", Registro.this.getPackageName());
                        if (!myDir.exists()) {
                            myDir.mkdir();
                        }
                        File fileImage = new File(myDir, "foto.jpg");
                        if (fileImage.exists())
                            fileImage.delete();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            imageUri = FileProvider.getUriForFile(Registro.this, "co.kubo.indiesco", fileImage);
                        } else {
                            imageUri = Uri.fromFile(fileImage);
                        }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, takeImage);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, selectImage);
                    }
                } else {
                    /*final DialogOpcionSenc dialogConfig = new DialogOpcionSenc(this, "-", getResources().getString(R.string.noFotos), getResources().getString(R.string.configuracion), 0);
                    dialogConfig.setPositveButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogConfig.dismiss();
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    });
                    dialogConfig.setCerrarButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogConfig.dismiss();
                        }
                    });
                    dialogConfig.show();*/
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == selectImage)
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    InputStream is;
                    try {
                        is = getContentResolver().openInputStream(selectedImage);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        Bitmap bitmap = BitmapFactory.decodeStream(bis);
                        guardarFoto(bitmap);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "onActivityResult, FileNotFoundException");
                    }
                }//if
            if (requestCode == takeImage)
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    guardarFoto(thumbnail);
                }//if
        } catch (Exception e) {
            Log.e(TAG, "onActivityResult, Exception");
        }
    }//onActivityResult

    public void guardarFoto(Bitmap myBitmap) throws Exception {
        File myDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", Registro.this.getPackageName());
        if (!myDir.exists()) {
            myDir.mkdir();
        }
        file = new File(myDir, "foto");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream stream = new FileOutputStream(file);
        if (myBitmap.getWidth() >= myBitmap.getHeight()) {
            myBitmap = Bitmap.createBitmap(myBitmap, myBitmap.getWidth() / 2 - myBitmap.getHeight() / 2, 0, myBitmap.getHeight(), myBitmap.getHeight());
            if (myBitmap.getWidth() > 500 && myBitmap.getHeight() > 500) {
                myBitmap = Bitmap.createScaledBitmap(myBitmap, 500, 500, false);
            }
        } else {
            myBitmap = Bitmap.createBitmap(myBitmap, 0, myBitmap.getHeight() / 2 - myBitmap.getWidth() / 2, myBitmap.getWidth(), myBitmap.getWidth());
            if (myBitmap.getWidth() > 500 && myBitmap.getHeight() > 500) {
                myBitmap = Bitmap.createScaledBitmap(myBitmap, 500, 500, false);
            }
        }
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        if (file != null) {
            File fileSec = new File(myDir, "foto.jpg");
            if (fileSec.exists())
                fileSec.delete();
            if (utils.checkInternetConnection(Registro.this, true)) {
                bandFoto = true;
                try {
                    Picasso
                            .with(getApplicationContext())
                            .load(file)
                            .placeholder(getResources().getDrawable(R.drawable.registro_foto))
                            .error(getResources().getDrawable(R.drawable.registro_foto))
                            .transform(new CircleTransform())
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(imgFoto);
                } catch (Exception e) {
                    Log.e(TAG, "Fallo Picasso foto");
                }
            }
        }
    }

    private void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**Configurar direccion en el mapa*/
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
                editDireccion.setAdapter(null);
                if (utils.checkInternetConnection(Registro.this, true)) {
                    AsincronaGetDireccionPorCoordenadas asyncDir = new AsincronaGetDireccionPorCoordenadas(String.valueOf(latitudDireccion), String.valueOf(longitudDireccion), Registro.this, 1);
                    asyncDir.execute();
                }
            }
        });
        /*
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionsGranted = true;
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
            setDatos();
        } else {
            isMapa = false;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
        }
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                checkLocation();
                return false;
            }
        });
        */
    }

    private void getDeviceLocation(){
        checkLocation();
        if (gps_enabled){
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Registro.this);
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

    private void getLocationPermission(){
        Log.e(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(ContextCompat.checkSelfPermission(Registro.this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionsGranted = true;
            //initMap();
        }else{
            ActivityCompat.requestPermissions(Registro.this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }//private void getLocationPermission

    private void checkLocation() {
        LocationManager lm = (LocationManager) Registro.this.getSystemService(Context.LOCATION_SERVICE);
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

    /*@Override
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
    }//public void onRequestPermissionsResult*/

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
        editDireccion.setAdapter(adapter);
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

    public void setDireccion(String direccion, String ciudad) {
        if(direccion.length() != 0){
            editDireccion.setText(direccion);
            editCiudad.setText(ciudad);
            this.ciudad = ciudad;
            this.direccion = direccion;
            verificarDireccion(ciudad);
        }else{
            Toast.makeText(Registro.this, "No se pudo obtener su ubicacion, es necesario para poder crear cuenta", Toast.LENGTH_LONG).show();
        }
    }

    public void verificarDireccion(String ciudad){

        for(int i=0; i<listCiudadesDisponibles.size(); i++){
           if (listCiudadesDisponibles.get(i).getCity().equals(ciudad)){
                    idCiudad = listCiudadesDisponibles.get(i).getCityId();
                    ciudadAvaible = true;
                    break;
           }
        }

    }


    ///// direcciones disponibles

    private void ciudadesDispo() {
        //if (dialogProgress == null) {
        dialogProgress = new DialogProgress(Registro.this);
        dialogProgress.show();
        //}
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        final String passSha1 = Utils.sha1Encrypt(password);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Call<ResponseValidacion> responseRegistroCall = endpoints.validarDirec(authToken);
        responseRegistroCall.enqueue(new Callback<ResponseValidacion>() {
            @Override
            public void onResponse(Call<ResponseValidacion> call, Response<ResponseValidacion> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code) {
                    case "100": //OK
                        //Servicio para crear direccion
                        listCiudadesDisponibles.addAll(response.body().getData());
                        break;
                    case "102": //Fallo
                        Toast.makeText(Registro.this, "Algo fallo intente de nuevo", Toast.LENGTH_LONG).show();
                        break;
                    case "120": //auth_token no valido
                        break;
                    default:
                        break;
                }//switch
            }

            @Override
            public void onFailure(Call<ResponseValidacion> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "onFailure registro");
            }
        });
    }



}
