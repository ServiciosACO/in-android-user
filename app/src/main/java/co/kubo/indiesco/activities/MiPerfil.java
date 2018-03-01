package co.kubo.indiesco.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.utils.SharedPreferenceManager;

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

    Context context;


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
                break;
            case R.id.imgConfig:
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
