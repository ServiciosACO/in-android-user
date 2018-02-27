package co.kubo.indiesco.activities;

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
import co.kubo.indiesco.adaptadores.CalendarioAdapter;
import co.kubo.indiesco.adaptadores.NotificacionesAdapter;
import co.kubo.indiesco.interfaces.ICalendarioPresenter;
import co.kubo.indiesco.interfaces.ICalendarioView;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.presenter.CalendarioPresenter;
import co.kubo.indiesco.presenter.HistorialServiciosPresenter;

public class Calendario extends AppCompatActivity implements View.OnClickListener, ICalendarioView {

    public static final String TAG = "Calendario";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.rvCalendar)
    RecyclerView rvCalendar;
    ICalendarioPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);

        presenter = new CalendarioPresenter(this, getApplicationContext(), Calendario.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            default:break;
        }
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
        rvCalendar.setLayoutManager(llm);
    }

    @Override
    public void inicializarAdaptadorRvCalendario(CalendarioAdapter calendarioAdapter) {
        rvCalendar.setAdapter(calendarioAdapter);
    }

    @Override
    public CalendarioAdapter crearAdaptadorCalendario(ArrayList<Historial> calendario) {
        CalendarioAdapter calendarioAdapter = new CalendarioAdapter(calendario, Calendario.this);
        return calendarioAdapter;
    }
}
