package co.kubo.indiesco.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.ActivityHistorialEmpleado;
import co.kubo.indiesco.activities.CircleTransform;
import co.kubo.indiesco.modelo.Personal;
import co.kubo.indiesco.utils.Singleton;
import co.kubo.indiesco.utils.Utils;

/**
 * Created by Diego on 12/02/2018.
 */

public class DialogDetalleHistorial extends Dialog implements View.OnClickListener {

    MapView mMapView;
    GoogleMap map;
    private Activity activity;
    private String lat, lng, nServicio, dir, ciudad, tipoTipo, fecha, hora, valor, dimension, calificado, calificacion;
    ArrayList<Personal> personalList;
    private RespuestaListener respuestaListener;
    Utils utils = new Utils();

    private Singleton general = Singleton.getInstance();

    public DialogDetalleHistorial(Activity activity, String lat, String lng, String nServicio, String dir,
                                  String ciudad, String dimension, String tipoTipo, String fecha, String hora, String valor,
                                  String calificado, String calificacion, RespuestaListener respuestaListener, ArrayList<Personal> personalList) {
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
        this.calificado = calificado;
        this.calificacion = calificacion;
        this.respuestaListener = respuestaListener;
        this.personalList = personalList;
    }

    public DialogDetalleHistorial(@NonNull Context context) {
        super(context);
    }

    public interface RespuestaListener {
        void onSalir();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.item_dialog_detalle_historial);
        Window window = this.getWindow();
        if (window != null) {
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

        LinearLayout llSalir = (LinearLayout) findViewById(R.id.llSalir);
        llSalir.setOnClickListener(this);
        TextView tvNoServicioDetHist = (TextView) findViewById(R.id.tvNoServicioDetHist);
        TextView tvDirDetalleHist = (TextView) findViewById(R.id.tvDirDetalleHist);
        TextView tvCiudadDetalle = (TextView) findViewById(R.id.tvCiudadDetalle);
        TextView tvTipoTipo = (TextView) findViewById(R.id.tvTipoTipo);
        TextView tvFechaDetalle = (TextView) findViewById(R.id.tvFechaDetalle);
        TextView tvHoraDetalle = (TextView) findViewById(R.id.tvHoraDetalle);
        TextView tvPrecioServicioDet = (TextView) findViewById(R.id.tvPrecioServicioDet);
        TextView tvEncargado = (TextView) findViewById(R.id.tvEncargado);
        TextView tvCalificacion = (TextView) findViewById(R.id.tvCalificacion);
        TextView tvVerEncargado = (TextView) findViewById(R.id.tvVerEncargado);
        ImageView imgFotoEncargado = (ImageView) findViewById(R.id.imgFotoEncargado);
        RatingBar ratingBarDet = (RatingBar) findViewById(R.id.ratingBarDet);

        if (personalList.size()>0){
            tvEncargado.setText(personalList.get(0).getNombre());
            tvCalificacion.setText(personalList.get(0).getCalificacion());
            Picasso
                    .with(activity)
                    .load(personalList.get(0).getFoto())
                    .placeholder(activity.getResources().getDrawable(R.drawable.registro_foto))
                    .error(activity.getResources().getDrawable(R.drawable.registro_foto))
                    .transform(new CircleTransform())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imgFotoEncargado);
        }

        tvNoServicioDetHist.setText(nServicio);
        tvDirDetalleHist.setText(dir);
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

        if (calificado.equals("si")){
            ratingBarDet.setVisibility(View.VISIBLE);
            ratingBarDet.setRating(Float.valueOf(calificacion));
        }else{
            ratingBarDet.setVisibility(View.GONE);
        }

        tvVerEncargado.setOnClickListener(new View.OnClickListener() {
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
            case R.id.llSalir:
                dismiss();
                break;
        }//switch
    }
    private void salir(){
        dismiss();
    }//private void salir
}
