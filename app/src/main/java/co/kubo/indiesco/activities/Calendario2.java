package co.kubo.indiesco.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.kubo.indiesco.R;
import co.kubo.indiesco.adaptadores.CalendarioAdapter;
import co.kubo.indiesco.adaptadores.CalendarioDetalleFechaAdapter;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.interfaces.ICalendario2Presenter;
import co.kubo.indiesco.interfaces.ICalendario2View;
import co.kubo.indiesco.interfaces.ICalendarioPresenter;
import co.kubo.indiesco.interfaces.ICalendarioView;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.presenter.Calendario2Presenter;
import co.kubo.indiesco.presenter.CalendarioPresenter;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseHistorial;
import co.kubo.indiesco.utils.EventDecorator;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import co.kubo.indiesco.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Calendario2 extends AppCompatActivity implements View.OnClickListener, ICalendario2View, OnDateSelectedListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    public static final String TAG = "Calendario";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.imgCal)
    ImageView imgCal;
    @BindView(R.id.rvCalendarCustom)
    RecyclerView rvCalendarCustom;
    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;
    @BindView(R.id.tvFechaSeleccionada)
    TextView tvFechaSeleccionada;
    @BindView(R.id.llOpcionCalendario)
    LinearLayout llOpcionCalendario;
    @BindView(R.id.llSinServicio)
    LinearLayout llSinServicio;

    Utils utils = new Utils();

    private ArrayList<Historial> calendario = new ArrayList<>();
    private DialogProgress dialogProgress;
    private boolean band = false;
    ICalendario2Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario2);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);
        calendarView.setOnDateChangedListener(this);
        obtenerCalendario();
        //tvFechaSeleccionada.setText(utils.DateToString(calendarView.getCurrentDate().getDate()).replace(" ", " de "));
        tvFechaSeleccionada.setText(getSelectedDatesString());
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //tvFechaSeleccionada.setText(utils.DateToString(calendarView.getCurrentDate().getDate()).replace(" ", " de "));
        tvFechaSeleccionada.setText(getSelectedDatesString());
        String fecha_1 = getSelectedDatesString();
        for (int i = 0; i < calendario.size(); i++){
            String fecha_2 = utils.StringToDate(calendario.get(i).getFecha_transaccion()).replace(" ", " de ");
            if (fecha_1.equals(fecha_2)){
                band = true;
            }
        }
        if (band){
            band = false;
            rvCalendarCustom.setVisibility(View.VISIBLE);
            llSinServicio.setVisibility(View.GONE);
            presenter = new Calendario2Presenter(this, getApplicationContext(), Calendario2.this, getSelectedDatesString());
        }else{
            rvCalendarCustom.setVisibility(View.GONE);
            llSinServicio.setVisibility(View.VISIBLE);
        }

    }

    private String getSelectedDatesString() {
        CalendarDay date = calendarView.getSelectedDate();
        if (date == null) {
            return " ";
        }
        return utils.DateToString(calendarView.getSelectedDate().getDate()).replace(" ", " de ");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBotonVolver:
                Intent in = new Intent(this, Calendario.class);
                startActivity(in);
                finish();
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
        rvCalendarCustom.setLayoutManager(llm);
    }

    @Override
    public void inicializarAdaptadorRvCalendarioPorFecha(CalendarioDetalleFechaAdapter calendarioDetalleFechaAdapter) {
        rvCalendarCustom.setAdapter(calendarioDetalleFechaAdapter);
    }

    @Override
    public CalendarioDetalleFechaAdapter crearAdaptadorCalendarioPorFecha(ArrayList<Historial> calendario) {
        CalendarioDetalleFechaAdapter calendarioDetalleFechaAdapter = new CalendarioDetalleFechaAdapter(calendario, Calendario2.this, Calendario2.this);
        return calendarioDetalleFechaAdapter;
    }

    @Override
    public void obtenerCalendario() {
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(Calendario2.this);
            dialogProgress.show();
        }
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        Call<ResponseHistorial> responseHistorialCall = endpoints.listarHistorial(authToken, usuario.getId_user(), "calendario");
        responseHistorialCall.enqueue(new Callback<ResponseHistorial>() {
            @Override
            public void onResponse(Call<ResponseHistorial> call, Response<ResponseHistorial> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){

                    case "100":
                        calendarView.removeDecorators();

                        ArrayList<CalendarDay> dates = new ArrayList<>();
                        calendario = response.body().getData();
                        for (int i = 0; i < calendario.size(); i++){
                            String[] fecha = utils.StringToDate2(calendario.get(i).getFecha_transaccion()).split(" ");
                            String day_ = fecha[0];
                            String month = fecha[1];
                            String year = fecha[2];
                            CalendarDay day = new CalendarDay(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day_));
                            dates.add(day);
                        }
                        calendarView.addDecorator(new EventDecorator(getResources().getColor(R.color.colorNaranja), dates));
                        break;
                    case "102":
                        calendarView.removeDecorators();
                        pintarSinServicio();
                        Log.e(TAG, "Cod: 102 No hay datos");
                        break;
                    case "120":
                        Log.e(TAG, "Cod: 120 auth token invalido");
                        break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseHistorial> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "obtener historial onFailure");
            }
        });
    }

    @Override
    public void pintarSinServicio() {
        rvCalendarCustom.setVisibility(View.GONE);
        llSinServicio.setVisibility(View.VISIBLE);
    }
}
