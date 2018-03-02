package co.kubo.indiesco.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.adaptadores.NotificacionesAdapter;
import co.kubo.indiesco.interfaces.INotificacionesPresenter;
import co.kubo.indiesco.interfaces.INotificacionesView;
import co.kubo.indiesco.modelo.Notificaciones;
import co.kubo.indiesco.presenter.NotificacionesPresenter;

public class MisNotificaciones extends AppCompatActivity implements INotificacionesView, View.OnClickListener {

    public static final String TAG = "MisNotificaciones";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.rvNotificacion)
    RecyclerView rvNotificacion;
    @BindView(R.id.llSinServicio)
    LinearLayout llSinServicio;

    INotificacionesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_notificaciones);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);

        presenter = new NotificacionesPresenter(this, getApplicationContext(), MisNotificaciones.this);
    }


    @Override
    public void generarLinearLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvNotificacion.setLayoutManager(llm);
    }

    @Override
    public void inicializarAdaptadorRvNotificaciones(NotificacionesAdapter notificacionesAdapter) {
        rvNotificacion.setAdapter(notificacionesAdapter);
    }

    @Override
    public NotificacionesAdapter crearAdaptadorNotificaciones(ArrayList<Notificaciones> notificaciones) {
        NotificacionesAdapter notificacionesAdapter = new NotificacionesAdapter(notificaciones, MisNotificaciones.this);
        return notificacionesAdapter;
    }

    @Override
    public void pintarSinInfo() {
        llSinServicio.setVisibility(View.VISIBLE);
        rvNotificacion.setVisibility(View.GONE);
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
}
