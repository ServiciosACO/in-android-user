package co.kubo.indiesco.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;

public class OlvidoContrasena2 extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.editCodigo1)
    TextView editCodigo1;
    @BindView(R.id.editCodigo2)
    TextView editCodigo2;
    @BindView(R.id.editCodigo3)
    TextView editCodigo3;
    @BindView(R.id.editCodigo4)
    TextView editCodigo4;
    @BindView(R.id.fabSiguiente)
    FloatingActionButton fabSiguiente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido_contrasena2);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);
        fabSiguiente.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            case R.id.fabSiguiente:
                break;
        }//switch
    }//onClick

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}//OlvidoContrasena2
