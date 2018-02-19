package co.kubo.indiesco.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;

public class SolicitudServicio extends AppCompatActivity {

    public static final String TAG = "SolicitudServicio";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_servicio);
        ButterKnife.bind(this);
    }
}
