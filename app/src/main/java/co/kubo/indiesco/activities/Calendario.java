package co.kubo.indiesco.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.adaptadores.CalendarioAdapter;
import co.kubo.indiesco.interfaces.ICalendarioPresenter;
import co.kubo.indiesco.interfaces.ICalendarioView;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.presenter.CalendarioPresenter;

public class Calendario extends AppCompatActivity implements View.OnClickListener, ICalendarioView {

    public static final String TAG = "Calendario";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.imgCal)
    ImageView imgCal;
    @BindView(R.id.rvCalendar)
    RecyclerView rvCalendar;
    @BindView(R.id.llSinServicio)
    LinearLayout llSinServicio;


    ICalendarioPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);
        imgCal.setOnClickListener(this);

        //Se envia tipo cero al constructor para mostrar toda la lista de servicios en el calendario
        presenter = new CalendarioPresenter(this, getApplicationContext(), Calendario.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBotonVolver:
                Intent inHome = new Intent(this, Home.class);
                startActivity(inHome);
                finish();
                break;
            case R.id.imgCal:
                Intent in = new Intent(this, Calendario2.class);
                startActivity(in);
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
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
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

    @Override
    public void pintarSinServicio() {
        llSinServicio.setVisibility(View.VISIBLE);
        rvCalendar.setVisibility(View.GONE);
    }
}
