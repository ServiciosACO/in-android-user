package co.kubo.indiesco.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;

public class IniciarSesion extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.btnContinuar)
    Button btnContinuar;
    @BindView(R.id.tvOlvidoContraseña)
    TextView tvOlvidoContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);
        editEmail.setOnClickListener(this);
        btnContinuar.setOnClickListener(this);
        tvOlvidoContraseña.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            case R.id.btnContinuar:
                Intent inNext = new Intent(this, IniciarSesion2.class);
                startActivity(inNext);
                break;
            case R.id.tvOlvidoContraseña:
                Intent in = new Intent(this, OlvidoContrasena.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                break;

        }//switch
    }//onClick

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
