package co.kubo.indiesco.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.dialog.DialogImagenPerfil;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseFoto;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import co.kubo.indiesco.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiPerfil extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MiPerfil";
    @BindView(R.id.imgCambiarFoto)
    ImageView imgCambiarFoto;
    @BindView(R.id.imgFoto)
    ImageView imgFoto;
    @BindView(R.id.imgConfig)
    ImageView imgConfig;
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.tvNombrePerfil)
    TextView tvNombrePerfil;
    @BindView(R.id.tvTelefonoPerfil)
    TextView tvTelefonoPerfil;
    @BindView(R.id.tvCambiarPass)
    TextView tvCambiarPass;
    @BindView(R.id.tvDir)
    TextView tvDir;
    @BindView(R.id.tvHistorial)
    TextView tvHistorial;
    @BindView(R.id.tvTerminos)
    TextView tvTerminos;
    @BindView(R.id.tvLlamanos)
    TextView tvLlamanos;
    @BindView(R.id.btnCerrarSesion)
    Button btnCerrarSesion;

    private DialogProgress dialogProgress;
    Context context;
    private Boolean opcionesCamara = false, bandFoto = false;
    private int position;
    private Uri imageUri;
    private static final int REQUEST_EXTERNAL_STORAGE = 2, OPTION_CAMERA = 0;
    private static int takeImage = 2, selectImage = 7;
    private File file;
    Utils utils = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);
        ButterKnife.bind(this);
        imgCambiarFoto.setOnClickListener(this);
        imgFoto.setOnClickListener(this);
        imgConfig.setOnClickListener(this);
        imgBotonVolver.setOnClickListener(this);
        tvCambiarPass.setOnClickListener(this);
        tvDir.setOnClickListener(this);
        tvHistorial.setOnClickListener(this);
        tvTerminos.setOnClickListener(this);
        tvLlamanos.setOnClickListener(this);
        btnCerrarSesion.setOnClickListener(this);

        context = MiPerfil.this;

        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        tvNombrePerfil.setText(usuario.getName());
        tvTelefonoPerfil.setText(usuario.getCelular());
        Picasso
                .with(getApplicationContext())
                .load(usuario.getFoto())
                .placeholder(getResources().getDrawable(R.drawable.registro_foto))
                .error(getResources().getDrawable(R.drawable.registro_foto))
                .transform(new CircleTransform())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imgFoto);

    }//onCreate

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgCambiarFoto:
                imgCambiarFoto.setImageResource(R.drawable.btn_foto_active);
                new DialogImagenPerfil(MiPerfil.this, new DialogImagenPerfil.RespuestaListener() {
                    @Override
                    public void onCamara() {
                        position = 0;
                        opcionFoto();
                        imgCambiarFoto.setImageResource(R.drawable.btn_foto_inactive);
                    }
                    @Override
                    public void onGaleria() {
                        position = 1;
                        opcionFoto();
                        imgCambiarFoto.setImageResource(R.drawable.btn_foto_inactive);
                    }
                    @Override
                    public void onSalir() {
                        imgCambiarFoto.setImageResource(R.drawable.btn_foto_inactive);
                        return;
                    }
                }).show();
                break;
            case R.id.imgConfig:
                /*Intent inEdit = new Intent(this, EditarPerfil.class);
                startActivity(inEdit);*/
                break;
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            case R.id.tvCambiarPass:
                Intent inPassword = new Intent(MiPerfil.this, CambiarContrasena.class);
                startActivity(inPassword);
                break;
            case R.id.tvDir:
                Intent inDir = new Intent(this, MisDirecciones.class);
                startActivity(inDir);
                break;
            case R.id.tvHistorial:
                Intent inHistorial = new Intent(this, HistorialServicios.class);
                startActivity(inHistorial);
                break;
            case R.id.tvTerminos:
                Intent inTerminos = new Intent(MiPerfil.this, Terminos.class);
                startActivity(inTerminos);
                break;
            case R.id.tvLlamanos:
                String phone = "123456789";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                break;
            case R.id.btnCerrarSesion:
                SharedPreferenceManager.setLoged(MiPerfil.this, false);
                Usuario usuario = new Usuario();
                SharedPreferenceManager.setInfoUsuario(getApplicationContext(), usuario);
                finish();
                Intent in = new Intent(MiPerfil.this, Login.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                break;
        }//switch
    }

    private void opcionFoto() {
        if (position == 0) {
            opcionesCamara = true;
            if (ActivityCompat.checkSelfPermission(MiPerfil.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MiPerfil.this, new String[]{Manifest.permission.CAMERA}, OPTION_CAMERA);
            } else {
                String[] PERMISSIONS_STORAGE_CAMERA = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                int permission_camera = ActivityCompat.checkSelfPermission(MiPerfil.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission_camera != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MiPerfil.this, PERMISSIONS_STORAGE_CAMERA, REQUEST_EXTERNAL_STORAGE);
                } else {
                    File myDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", MiPerfil.this.getPackageName());
                    if (!myDir.exists()) {
                        myDir.mkdir();
                    }
                    File fileImage = new File(myDir, "foto.jpg");
                    if (fileImage.exists())
                        fileImage.delete();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageUri = FileProvider.getUriForFile(MiPerfil.this, "co.kubo.indiesco", fileImage);
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
            int permission = ActivityCompat.checkSelfPermission(MiPerfil.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MiPerfil.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, selectImage);
            }
        }
    }//opcionFoto

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case OPTION_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    int permission = ActivityCompat.checkSelfPermission(MiPerfil.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MiPerfil.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                    } else {
                        File myDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", MiPerfil.this.getPackageName());
                        if (!myDir.exists()) {
                            myDir.mkdir();
                        }
                        File fileImage = new File(myDir, "foto.jpg");
                        if (fileImage.exists())
                            fileImage.delete();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            imageUri = FileProvider.getUriForFile(MiPerfil.this, "co.kubo.indiesco", fileImage);
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
                        File myDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", MiPerfil.this.getPackageName());
                        if (!myDir.exists()) {
                            myDir.mkdir();
                        }
                        File fileImage = new File(myDir, "foto.jpg");
                        if (fileImage.exists())
                            fileImage.delete();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            imageUri = FileProvider.getUriForFile(MiPerfil.this, "co.kubo.indiesco", fileImage);
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
    }//public void onRequestPermissionsResult

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
        File myDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", MiPerfil.this.getPackageName());
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
            if (utils.checkInternetConnection(MiPerfil.this, true)) {
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
        crearFoto(file);
    }//public void guardarFoto

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void crearFoto(File file) {
        dialogProgress = new DialogProgress(MiPerfil.this);
        dialogProgress.show();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());

        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto", file.getName(), reqFile);
        RequestBody idUser = RequestBody.create(MediaType.parse("text/plain"), usuario.getId_user());

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
                        Usuario user = new Usuario();
                        user = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
                        user.setFoto(response.body().getFoto());
                        SharedPreferenceManager.setInfoUsuario(getApplicationContext(), user);
                        break;
                    case "102": //Fallo
                        Toast.makeText(MiPerfil.this, "Algo fallo intente de nuevo", Toast.LENGTH_LONG).show();
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
}
