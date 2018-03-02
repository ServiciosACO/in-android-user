package co.kubo.indiesco.dialog;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import co.kubo.indiesco.R;
import co.kubo.indiesco.utils.Utils;

/**
 * Created by Diego on 12/02/2018.
 */

public class DialogDetalleHistorial extends Dialog implements View.OnClickListener {

    MapView mMapView;
    GoogleMap map;
    private Activity activity;
    private String lat, lng, nServicio, dir, ciudad, tipoTipo, fecha, hora, valor, dimension, calificado, calificacion;
    private RespuestaListener respuestaListener;
    Utils utils = new Utils();

    public DialogDetalleHistorial(Activity activity, String lat, String lng, String nServicio, String dir,
                                  String ciudad, String dimension, String tipoTipo, String fecha, String hora, String valor,
                                  String calificado, String calificacion, RespuestaListener respuestaListener) {
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
        RatingBar ratingBarDet = (RatingBar) findViewById(R.id.ratingBarDet);

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
