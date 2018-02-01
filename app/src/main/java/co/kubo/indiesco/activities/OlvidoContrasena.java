package co.kubo.indiesco.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;

public class OlvidoContrasena extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.fabSiguiente)
    FloatingActionButton fabSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido_contrasena);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);
        fabSiguiente.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            case R.id.fabSiguiente:
                //Consulta servicio para validar email
                //si es correcto sigue
                Intent in = new Intent(this, OlvidoContrasena2.class);
                startActivity(in);
                break;
        }//switch
    }//onClick

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }//onBackPressed
}
