package co.kubo.indiesco.activities;

import android.content.Intent;
import android.media.VolumeAutomation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;

public class Calificar2 extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Calificar2";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar2);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            default:break;
        }//switch
    }

    @Override
    public void onBackPressed() {
        Intent goHome = new Intent(Calificar2.this, Home.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goHome);
        finish();
    }
}