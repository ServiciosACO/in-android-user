package co.kubo.indiesco.activities;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
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

        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        tvNombrePerfil.setText(usuario.getName());
        tvTelefonoPerfil.setText(usuario.getCelular());
        Picasso
                .with(getApplicationContext())
                .load(usuario.getFoto())
                .transform(new CircleTransform())
                .into(imgFoto);

    }//onCreate

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgCambiarFoto:
                break;
            case R.id.imgFoto:
                break;
            case R.id.imgConfig:
                break;
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            case R.id.tvCambiarPass:
                break;
            case R.id.tvDir:
                break;
            case R.id.tvHistorial:
                break;
            case R.id.tvTerminos:
                break;
            case R.id.tvLlamanos:
                break;
            case R.id.btnCerrarSesion:
                Intent in = new Intent(MiPerfil.this, Login.class);
                SharedPreferenceManager.setLoged(MiPerfil.this, false);
                Usuario usuario = new Usuario();
                SharedPreferenceManager.setInfoUsuario(getApplicationContext(), usuario);
                startActivity(in);
                finish();
                break;
        }//switch
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
