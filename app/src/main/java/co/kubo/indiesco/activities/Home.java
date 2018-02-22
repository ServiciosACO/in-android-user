package co.kubo.indiesco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.utils.SharedPreferenceManager;

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
    @BindView(R.id.tvMiPerfil)
    TextView tvMiPerfil;
    @BindView(R.id.tvNombrePerfil)
    TextView tvNombrePerfil;
    @BindView(R.id.tvNserviciosProgramados)
    TextView tvNserviciosProgramados;
    @BindView(R.id.tvNnotificaciones)
    TextView tvNnotificaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        tvMiPerfil.setOnClickListener(this);
        llSolicitarServ.setOnClickListener(this);
        llNotificaciones.setOnClickListener(this);
        llCalendario.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvMiPerfil:
                Intent in = new Intent(Home.this, MiPerfil.class);
                startActivity(in);
                break;
            case R.id.llSolicitarServ:
                Intent inServ = new Intent(Home.this, SolicitudServicio.class);
                startActivity(inServ);
                break;
            case R.id.llNotificaciones:

                break;
            case R.id.llCalendario:

                break;
            default:break;
        }//switch
    }//onClick
}
