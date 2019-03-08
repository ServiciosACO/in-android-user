package co.kubo.indiesco.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.adaptadores.CalificarEmpleadoAdapter;
import co.kubo.indiesco.modelo.Personal;
import co.kubo.indiesco.utils.Singleton;

public class ActivityCalificarPersonal extends AppCompatActivity {

    private ListView listEmpleadosCalificar;
    private TextView txtEnviarCalEmpleados;
    private ImageView imgBotonVolverCalificar;
    private LinearLayout lnCalificar;

    private Singleton general = Singleton.getInstance();
    private CalificarEmpleadoAdapter adapter;
    boolean envioCalificacion = false;
    ArrayList<Personal> arrayListPersonal;
    String servicio = "";
    String fecha = "";
    String direccion = "";
    String valor = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar_personal);
        listEmpleadosCalificar = (ListView) findViewById(R.id.listEmpleadosCalificar);
        imgBotonVolverCalificar = (ImageView) findViewById(R.id.imgBotonVolverCalificar);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            servicio = extras.getString("servicio");
            fecha = extras.getString("fecha");
            direccion = extras.getString("direccion");
            valor = extras.getString("valor");
            //The key argument here must match that used in the other activity
        }

       // arrayListPersonal.addAll(general.getArrayListPersonal());
        Gson gson = new Gson();
        String serviceCalification = gson.toJson(general.getArrayListPersonal());
        arrayListPersonal = new Gson().fromJson(serviceCalification, new TypeToken<ArrayList<Personal>>() {}.getType());

        adapter = new CalificarEmpleadoAdapter(ActivityCalificarPersonal.this, arrayListPersonal);
        listEmpleadosCalificar.setAdapter(adapter);

        LayoutInflater inflaterHeader = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflaterHeader.inflate(
                R.layout.header_calificar_personal, listEmpleadosCalificar, false);
        TextView  tvNoServicioCalificarEm = (TextView)header.findViewById(R.id.tvNoServicioCalificarEm);
        TextView  tvFechaServicioCalificarEM = (TextView)header.findViewById(R.id.tvFechaServicioCalificarEM);
        TextView  tvDirServicioCalificarEm  = (TextView)header.findViewById(R.id.tvDirServicioCalificarEm);
        TextView  tvPrecioServicioEm   = (TextView)header.findViewById(R.id.tvPrecioServicioEm);

        tvNoServicioCalificarEm.setText(servicio);
        tvFechaServicioCalificarEM.setText(fecha);
        tvDirServicioCalificarEm.setText(direccion);
        tvPrecioServicioEm.setText(valor);

        listEmpleadosCalificar.addHeaderView(header);



        LayoutInflater inflaterFooter = getLayoutInflater();
        ViewGroup footer = (ViewGroup) inflaterFooter.inflate(
                R.layout.footer_calificar_personal, listEmpleadosCalificar, false);
        txtEnviarCalEmpleados = (TextView)footer.findViewById(R.id.tvEnviarCalificacionEmpleado);
        lnCalificar = (LinearLayout) footer.findViewById(R.id.lnCalificar);
        listEmpleadosCalificar.addFooterView(footer);


        txtEnviarCalEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                envioCalificacion = true;
                finish();
                general.getArrayListPersonal().clear();
                general.getArrayListPersonal().addAll(arrayListPersonal);
            }
        });

        imgBotonVolverCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void validarCalificados(){

        boolean calificado = true;

        for(int i=0; i<arrayListPersonal.size(); i++){
            if (!arrayListPersonal.get(i).getCalificado()){
                calificado = false;
                break;
            }
        }

        if (calificado){
            lnCalificar.setBackground(getResources().getDrawable(R.drawable.rounded_button_green));
            txtEnviarCalEmpleados.setTextColor(getResources().getColor(R.color.colorBlanco));
        }else{
            lnCalificar.setBackground(getResources().getDrawable(R.drawable.border_rounded_line));
            txtEnviarCalEmpleados.setTextColor(getResources().getColor(R.color.colorVerde));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("onDestroy","onDestroy");

    }
}
