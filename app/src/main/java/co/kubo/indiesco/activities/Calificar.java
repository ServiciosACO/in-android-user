package co.kubo.indiesco.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.adaptadores.CalificarAdapter;
import co.kubo.indiesco.interfaces.ICalificarPresenter;
import co.kubo.indiesco.interfaces.ICalificarView;
import co.kubo.indiesco.modelo.PendienteCalificar;
import co.kubo.indiesco.presenter.CalificarPresenter;

public class Calificar extends AppCompatActivity implements View.OnClickListener, ICalificarView {

    public static final String TAG = "Calificar";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.rvCalificar)
    RecyclerView rvCalificar;
    ICalificarPresenter presenter;
    String sessionId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);

        sessionId  = getIntent().getStringExtra("id_servicio");

        presenter = new CalificarPresenter(this, getApplicationContext(), Calificar.this,sessionId);
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
     //   Intent goHome = new Intent(Calificar.this, Home.class);
     //   goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    //    startActivity(goHome);
        finish();
    }

    @Override
    public void generarLinearLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvCalificar.setLayoutManager(llm);
    }

    @Override
    public void inicializarAdaptadorRvCalificar(CalificarAdapter calificarAdapter) {
        rvCalificar.setAdapter(calificarAdapter);
    }

    @Override
    public CalificarAdapter crearAdaptadorCalificar(ArrayList<PendienteCalificar> calificar) {
        CalificarAdapter calificarAdapter = new CalificarAdapter(calificar, Calificar.this);
        return calificarAdapter;
    }
}
