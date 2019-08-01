package co.kubo.indiesco.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    @BindView(R.id.imgNoInternet)
    ImageView imgNoInternet;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSubTitle)
    TextView tvSubTitle;
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
    public void pintarSinInfo(int i) {
        switch (i){
            case 1:
                llSinServicio.setVisibility(View.VISIBLE);
                rvHistorial.setVisibility(View.GONE);
                imgNoInternet.setImageDrawable(getResources().getDrawable(R.drawable.img_missingcontent));
                tvSubTitle.setText("¡Oops!");
                tvSubTitle.setText("Parece que no hay nada aquí, por favor regresa más tarde");
            break;
            case 2:
                llSinServicio.setVisibility(View.VISIBLE);
                rvHistorial.setVisibility(View.GONE);
                imgNoInternet.setImageDrawable(getResources().getDrawable(R.drawable.img_nointernet));
                tvSubTitle.setText("¡Vaya!");
                tvSubTitle.setText("Parece que perdiste tu conexión a internet. Inténtalo de nuevo");
            break;
        }
    }
}
