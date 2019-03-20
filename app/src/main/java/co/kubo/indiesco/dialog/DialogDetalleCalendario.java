package co.kubo.indiesco.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.ActivityHistorialEmpleado;
import co.kubo.indiesco.modelo.Personal;
import co.kubo.indiesco.utils.Singleton;
import co.kubo.indiesco.utils.Utils;

/**
 * Created by estacion on 27/02/18.
 */

public class DialogDetalleCalendario extends Dialog implements View.OnClickListener {

    MapView mMapView;
    GoogleMap map;
    private Activity activity;
    private String lat, lng, nServicio, dir, ciudad, tipoTipo, fecha, hora, valor, dimension, estado;
    private RespuestaListener respuestaListener;
    Utils utils = new Utils();
    private boolean band;
    ArrayList<Personal> personalList;

    private Singleton general = Singleton.getInstance();

    public DialogDetalleCalendario(Activity activity, String lat, String lng, String nServicio, String dir,
                                   String ciudad, String dimension,  String tipoTipo, String fecha, String hora,
                                   String valor, String estado,
                                   RespuestaListener respuestaListener, ArrayList<Personal> personalList) {
        super(activity, R.style.ThemeTransparent);
        this.activity = activity;
        this.lat = lat;
        this.lng = lng;
        this.nServicio = nServicio;
        this.dir = dir;
        this.ciudad = ciudad;
        this.dimension = dimension;
        this.tipoTipo = tipoTipo;
        this.fecha = fecha;
        this.hora = hora;
        this.valor = valor;
        this.estado = estado;
        this.respuestaListener = respuestaListener;
        this.personalList = personalList;
    }

    public interface RespuestaListener {
        void onCancelarServicio();
        void onSalir();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.item_dialog_calendario);
        Window window = this.getWindow();
        if (window != null){
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams
                    .MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }//if

        mMapView = (MapView) findViewById(R.id.mapView);
        MapsInitializer.initialize(activity);
        mMapView.onCreate(onSaveInstanceState()); //ojo probar savedInstanceState
        mMapView.onResume();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng posisiabsen = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)); ////your lat lng
                googleMap.addMarker(new MarkerOptions().position(posisiabsen));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisiabsen, 15f));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 100, null);
            }
        });

        TextView tvDirDetalle = (TextView) findViewById(R.id.tvDirDetalle);
        TextView tvCiudadDetalle = (TextView) findViewById(R.id.tvCiudadDetalle);
        TextView tvTipoTipo = (TextView) findViewById(R.id.tvTipoTipo);
        TextView tvFechaDetalle = (TextView) findViewById(R.id.tvFechaDetalle);
        TextView tvHoraDetalle = (TextView) findViewById(R.id.tvHoraDetalle);
        TextView tvPrecioServicioDet = (TextView) findViewById(R.id.tvPrecioServicioDet);
        TextView tvVerEncargadoCalendario = (TextView) findViewById(R.id.tvVerEncargadoCalendario);

        LinearLayout llSalir = (LinearLayout) findViewById(R.id.llSalir);
        llSalir.setOnClickListener(this);
        Button btnCancelarServicio = (Button) findViewById(R.id.btnCancelarServicio);
        btnCancelarServicio.setOnClickListener(this);

        if (estado.equals("cancelado_usuario"))
            btnCancelarServicio.setVisibility(View.GONE);

        tvDirDetalle.setText(dir);
        tvCiudadDetalle.setText(ciudad);
        tvFechaDetalle.setText(utils.StringToDate2(fecha).replace(" ", "/"));
        tvHoraDetalle.setText(hora);
        DecimalFormat formateador = new DecimalFormat("###,###");
        tvPrecioServicioDet.setText(formateador.format(Double.parseDouble(String.valueOf(valor))) + " COP");
        switch (tipoTipo){
            case "1":
                tvTipoTipo.setText("Casa - " + dimension + activity.getResources().getString(R.string.cuadrado));
                break;
            case "2":
                tvTipoTipo.setText("Oficina - " + dimension + activity.getResources().getString(R.string.cuadrado));
                break;
            case "3":
                tvTipoTipo.setText("Finca - " + dimension + activity.getResources().getString(R.string.cuadrado));
                break;
        }//switch

        tvVerEncargadoCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (personalList.size()>0){
                    general.setArrayListPersonalHistorial(personalList);
                    Intent in = new Intent(activity, ActivityHistorialEmpleado.class);
                    activity.startActivity(in);
                }else{
                    Toast.makeText(getContext(), "Este servicio no tiene encargados asignados.", Toast.LENGTH_LONG).show();

                }

            }
        });

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
    }//protected void onCreate
    @Override
    public void onBackPressed() {
        salir();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancelarServicio:
                band = true;
                salir();
            case R.id.llSalir:
                band = false;
                salir();
                break;
        }//switch
    }
    private void salir(){
        if (band){
            respuestaListener.onCancelarServicio();
            dismiss();
        }else{
            respuestaListener.onSalir();
            dismiss();
        }
    }//private void salir
}
