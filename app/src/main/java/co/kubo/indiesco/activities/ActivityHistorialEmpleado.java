package co.kubo.indiesco.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import co.kubo.indiesco.R;
import co.kubo.indiesco.adaptadores.CalificarEmpleadoAdapter;
import co.kubo.indiesco.adaptadores.HistorialEmpleadoAdapter;
import co.kubo.indiesco.utils.Singleton;

public class ActivityHistorialEmpleado extends AppCompatActivity {

    private ListView listEncargadosHistorial;
    ImageView imgBotonVolverCalificarHistorial;
    private HistorialEmpleadoAdapter adapter;
    private Singleton general = Singleton.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_empleado);

        listEncargadosHistorial = (ListView) findViewById(R.id.listEncargadosHistorial);
        imgBotonVolverCalificarHistorial = (ImageView) findViewById(R.id.imgBotonVolverCalificarHistorial);

        adapter = new HistorialEmpleadoAdapter(ActivityHistorialEmpleado.this, general.getArrayListPersonalHistorial());
        listEncargadosHistorial.setAdapter(adapter);

        imgBotonVolverCalificarHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
