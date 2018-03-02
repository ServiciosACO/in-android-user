package co.kubo.indiesco.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.adaptadores.HistorialServiciosAdapter;
import co.kubo.indiesco.interfaces.IHistorialServiciosPresenter;
import co.kubo.indiesco.interfaces.IHistorialServiciosView;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.presenter.HistorialServiciosPresenter;

public class HistorialServicios extends AppCompatActivity implements OnClickListener, IHistorialServiciosView {

    public static final String TAG = "HistorialServicios";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.rvHistorial)
    RecyclerView rvHistorial;
    @BindView(R.id.llSinServicio)
    LinearLayout llSinServicio;

    IHistorialServiciosPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_servicios);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);
        presenter = new HistorialServiciosPresenter(this, getApplicationContext(), HistorialServicios.this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
        }//switch
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void generarLinearLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvHistorial.setLayoutManager(llm);
    }

    @Override
    public void inicializarAdaptadorRvHistorial(HistorialServiciosAdapter historialServiciosAdapter) {
        rvHistorial.setAdapter(historialServiciosAdapter);
    }

    @Override
    public HistorialServiciosAdapter crearAdaptadorHistorial(ArrayList<Historial> historials) {
        HistorialServiciosAdapter historialServiciosAdapter = new HistorialServiciosAdapter(historials, HistorialServicios.this);
        return historialServiciosAdapter;
    }

    @Override
    public void pintarSinInfo() {
        llSinServicio.setVisibility(View.VISIBLE);
        rvHistorial.setVisibility(View.GONE);
    }
}
