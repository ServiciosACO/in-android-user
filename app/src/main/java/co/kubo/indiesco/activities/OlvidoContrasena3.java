package co.kubo.indiesco.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;

public class OlvidoContrasena3 extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.editpass1)
    TextInputEditText editpass1;
    @BindView(R.id.editpass2)
    TextInputEditText editpass2;
    @BindView(R.id.fabSiguiente)
    FloatingActionButton fabSiguiente;
    @BindView(R.id.tvNotificacionPass)
    TextView tvNotificacionPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido_contrasena3);
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            case R.id.fabSiguiente:
                //llamo al servicio para guardar la contraseña y me lleva al Home
                Intent in = new Intent(this, Home.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
                break;
        }//switch
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
