package co.kubo.indiesco.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;

public class IniciarSesion2 extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.editEmail)
    TextInputEditText editEmail;
    @BindView(R.id.btnIniciarSesion)
    Button btnIniciarSesion;
    @BindView(R.id.tvOlvidoContraseña)
    TextView tvOlvidoContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion2);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);
        editEmail.setOnClickListener(this);
        btnIniciarSesion.setOnClickListener(this);
        tvOlvidoContraseña.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            case R.id.btnIniciarSesion:
                //Validar si tiene servicios pendientes por calificar
                //Si no iniciar en Home
                Intent inInit = new Intent(this, Home.class);
                inInit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(inInit);
                break;
            case R.id.tvOlvidoContraseña:
                Intent in = new Intent(this, OlvidoContrasena.class);
                startActivity(in);
                break;
        }//switch
    }//onClick

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
