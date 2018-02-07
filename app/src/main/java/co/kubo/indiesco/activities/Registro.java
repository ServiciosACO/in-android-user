package co.kubo.indiesco.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseFoto;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.restAPI.modelo.ResponseRegistro;
import co.kubo.indiesco.utils.Constantes;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import co.kubo.indiesco.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Registro";
    @BindView(R.id.imgFoto)
    ImageView imgFoto;
    @BindView(R.id.editNombre)
    EditText editNombre;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editCelular)
    EditText editCelular;
    @BindView(R.id.editDireccion)
    EditText editDireccion;
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
    @BindView(R.id.llOpcionFoto)
    LinearLayout llOpcionFoto;
    @BindView(R.id.tvGaleria)
    TextView tvGaleria;
    @BindView(R.id.tvCamara)
    TextView tvCamara;

    private int position = 0;
    private Uri imageUri;
    private Boolean opcionesCamara = false;
    private static int takeImage = 2;
    private int selectImage = 7;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static final int OPTION_CAMERA = 0;
    private Boolean bandFoto = false;
    private int scrollY, oldScrollY = 0;
    Utils utils = new Utils();
    Animation animShake;
    private File file;
    private boolean bandNombre = false, bandEmail = false, bandCel = false, bandDir = false, bandCiudad = false, bandPass1 = false, bandPass2 = false, bandOK = false;
    private String nombre = "", email = "", password = "", plataforma = "a", token = "0", telefono = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);
        animShake = AnimationUtils.loadAnimation(Registro.this, R.anim.shake);
        fabSiguiente.setOnClickListener(this);
        imgFoto.setOnClickListener(this);
        tvCamara.setOnClickListener(this);
        tvGaleria.setOnClickListener(this);
        /**Para desaparecer el FAB cuando hago scroll*/
        scrollViewRegistro.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                scrollY = scrollViewRegistro.getScrollY();
                Log.e("scroll", scrollY + " " + oldScrollY);
                if (scrollY > oldScrollY){
                    fabSiguiente.hide();
                    oldScrollY = scrollY;
                    if (scrollViewRegistro.getChildAt(0).getBottom() <= (scrollViewRegistro.getHeight() + scrollViewRegistro.getScrollY()) || scrollY <= 0)
                        fabSiguiente.show();
                }else{
                    fabSiguiente.show();
                    oldScrollY = scrollY;
                }
            }
        });

        scrollViewRegistro.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (llOpcionFoto.getVisibility() == View.VISIBLE){
                    scrollViewRegistro.setBackgroundColor(getResources().getColor(R.color.fondo_registro));
                    llOpcionFoto.setVisibility(View.GONE);
                }//if
                return false;
            }//onTouch
        });

        setlistenerEditText();
    }

    private void setlistenerEditText(){
        editNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandNombre = true;
                }else{
                    bandNombre = false;
                }
                validarFABVerde();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandEmail = true;
                }else{
                    bandEmail = false;
                }
                validarFABVerde();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editCelular.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandCel = true;
                }else{
                    bandCel = false;
                }
                validarFABVerde();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandDir = true;
                }else{
                    bandDir = false;
                }
                validarFABVerde();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editCiudad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandCiudad = true;
                }else{
                    bandCiudad = false;
                }
                validarFABVerde();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editpass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandPass1 = true;
                }else{
                    bandPass1 = false;
                }
                validarFABVerde();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editpass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandPass2 = true;
                }else{
                    bandPass2 = false;
                }
                validarFABVerde();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void validarFABVerde(){
        if (bandNombre&&bandEmail&&bandCel&&bandDir&&bandCiudad&&bandPass1&&bandPass2){
            fabSiguiente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorVerde)));
            bandOK = true;
        }else{
            fabSiguiente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_hint)));
            bandOK = false;
        }
    }//validarFABVerde

    private Boolean validacion() {
        if (!Utils.checkInternetConnection(this, true)){
            return false;
        }
        if (editNombre.getText().toString().trim().equalsIgnoreCase("")) {
            editNombre.setError("El nombre es requerido");
            return false;
        }
        if (editEmail.getText().toString().trim().equalsIgnoreCase("")) {
            editEmail.setError("El correo electrónico es requerido");
            return false;
        }
        if (!utils.isEmailValid(editEmail.getText().toString())){
            editEmail.setError("Debe ingresar un correo electrónico valido");
            return false;
        }
        if (editCelular.getText().toString().trim().equalsIgnoreCase("")) {
            editCelular.setError("El celular es requerido");
            return false;
        }
        if(editCelular.getText().toString().trim().length() != 10){
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
        if (!editpass1.getText().toString().equals(editpass2.getText().toString())) {
            editpass1.setError("Las contraseñas no coinciden");
            inputPass1.startAnimation(animShake);
            inputPass2.startAnimation(animShake);
            return false;
        }
        return true;
    }//validacion

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabSiguiente:
                if (validacion() && bandOK){
                    crearCuenta();
                }//if
                break;
            case R.id.imgFoto:
                scrollViewRegistro.setBackgroundColor(getResources().getColor(R.color.fondo_dialog));
                llOpcionFoto.setVisibility(View.VISIBLE);
                //opcionFoto();
                break;
            case R.id.llOpcionFoto:
                //scrollViewRegistro.setBackgroundColor(getResources().getColor(R.color.fondo_dialog));
                //llOpcionFoto.setVisibility(View.VISIBLE);
                break;
            case R.id.tvCamara:
                position = 0;
                opcionFoto();
                break;
            case R.id.tvGaleria:
                position = 1;
                opcionFoto();
                break;
        }//switch
    }

    private void splashRegistro(){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent in = new Intent(Registro.this, Home.class);
                setResult(RESULT_OK, in);
                //in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
            }//run
        }, Constantes.tiempoSplash);
    }//splashRegistro

    private void opcionFoto(){
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
                    File fileImage = new File(myDir, SharedPreferenceManager.getInfoUsuario(Registro.this).getId_user() + ".jpg");
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

    private void crearCuenta(){
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        final String passSha1 = Utils.sha1Encrypt(password);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Call<ResponseRegistro> responseRegistroCall = endpoints.registro(authToken, nombre, email, passSha1, token, plataforma, telefono);
        responseRegistroCall.enqueue(new Callback<ResponseRegistro>() {
            @Override
            public void onResponse(Call<ResponseRegistro> call, Response<ResponseRegistro> response) {
                String code = response.body().getCode();
                switch (code){
                    case "100": //OK
                        Usuario usuario = new Usuario();
                        usuario.setName(nombre);
                        usuario.setEmail(email);
                        usuario.setContraseña(passSha1);
                        usuario.setCelular(telefono);
                        //usuario.setDireccion(direccion);
                        //usuario.setId_ciudad(id_ciudad);
                        //usuario.setCiudad(ciudad);
                        usuario.setId_user(response.body().getData().getUid());

                        SharedPreferenceManager.setInfoUsuario(getApplicationContext(), usuario);
                        SharedPreferenceManager.setLoged(Registro.this, true);

                        crearFoto(usuario, response.body().getData().getUid(), file);

                        /*fabSiguiente.setVisibility(View.GONE);
                        scrollViewRegistro.setVisibility(View.GONE);
                        llSplashRegistro.setVisibility(View.VISIBLE);
                        splashRegistro();*/
                        break;
                    case "102": //Fallo
                        Toast.makeText(Registro.this, "Algo fallo intente de nuevo", Toast.LENGTH_LONG).show();
                        break;
                    case "120": //auth_token no valido
                        break;
                    default: break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseRegistro> call, Throwable t) {
                Log.e(TAG, "onFailure registro");
            }
        });
    }

    private void crearFoto(final Usuario usuario, String id, File file){
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
                String code = response.body().getCode();
                switch (code){
                    case "100": //OK
                        //Guardar foto
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
                    default: break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseFoto> call, Throwable t) {
                Log.e(TAG, "onFailure crearFoto");
            }
        });
    }

    public void guardarFoto(Bitmap myBitmap) throws Exception {
        File myDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", Registro.this.getPackageName());
        if (!myDir.exists()) {
            myDir.mkdir();
        }
        file = new File(myDir, "" + SharedPreferenceManager.getInfoUsuario(Registro.this).getId_user());  //getUid());
        if (file.exists())
            file.delete();
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
            File fileSec = new File(myDir, SharedPreferenceManager.getInfoUsuario(Registro.this).getId_user() + ".jpg");
            if (fileSec.exists())
                fileSec.delete();
            if (Utils.checkInternetConnection(Registro.this, true)) {
                try {
                    Picasso
                            .with(getApplicationContext())
                            .load(file)
                            .transform(new CircleTransform())
                            .into(imgFoto);

                    /*Glide
                            .with(Registro.this)
                            .load(file).apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            //.transform(new CropCircleTransformation(this)))
                            .transform(new CircleCrop()))
                            .into(imgFoto);*/
                } catch (Exception e) {
                    Log.e(TAG, "Fallo Glide foto");
                }
            }
        }
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
                        File fileImage = new File(myDir, SharedPreferenceManager.getInfoUsuario(Registro.this).getId_user() + ".jpg");
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
                }else{
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
                        File fileImage = new File(myDir, SharedPreferenceManager.getInfoUsuario(Registro.this).getId_user() + ".jpg");
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
                }else{
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
        } catch (Exception e) {}
    }//onActivityResult

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            squaredBitmap.recycle();
            return bitmap;
        }
        @Override
        public String key() {
            return "circle";
        }
    }//CircleTransform

}
