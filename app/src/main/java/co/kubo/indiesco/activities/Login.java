package co.kubo.indiesco.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;

public class Login extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tvTerminosCondiciones)
    TextView tvTerminosCondiciones;
    @BindView(R.id.btnIniciarSesion)
    Button btnIniciarSesion;
    @BindView(R.id.btnCrearCuenta)
    Button btnCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        tvTerminosCondiciones.setOnClickListener(this);
        btnCrearCuenta.setOnClickListener(this);
        btnIniciarSesion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvTerminosCondiciones:
                break;
            case R.id.btnCrearCuenta:
                break;
            case R.id.btnIniciarSesion:
                Intent inIniciar = new Intent(this, IniciarSesion.class);
                startActivity(inIniciar);
                break;
            default: break;
        }//switch
    }//onClick
}//Login
