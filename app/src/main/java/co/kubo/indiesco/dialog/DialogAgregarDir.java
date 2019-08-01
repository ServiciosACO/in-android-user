package co.kubo.indiesco.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import co.kubo.indiesco.R;
import co.kubo.indiesco.asincronasMapa.AsincronaGetDireccionPorCoordenadas;
import co.kubo.indiesco.utils.Utils;

/**
 * Created by Diego on 17/02/2018.
 */

public class DialogAgregarDir extends Dialog implements View.OnClickListener, OnMapReadyCallback {

    public static final String TAG = "DialogAgregarDir";
    private Activity activity;
    private boolean respuesta;
    AutoCompleteTextView editDireccion;
    EditText tvDirComplemento;
    private MapFragment mapaDireccion;
    private GoogleMap googleMap;
    private RespuestaListener respuestaListener;
    private String dirX, latX = "4.674465", lngX = "-74.057422", complementoX, ciudadX;
    private Double latitudDireccion, longitudDireccion;
    Utils utils = new Utils();

    public DialogAgregarDir(Activity activity, RespuestaListener respuestaListener) {
        super(activity, R.style.ThemeTransparent);
        this.activity = activity;
        this.respuestaListener = respuestaListener;
    }
    public interface RespuestaListener {
        void onAgregar(String dir, String lat, String lng, String complemento, String ciudad);
        void onSalir();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.item_dialog_agregar_dir);
        Window window = this.getWindow();
        if (window != null){
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams
                    .MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }//if

        Button btnAgregarDir = (Button) findViewById(R.id.btnAgregarDir);
        btnAgregarDir.setOnClickListener(this);
        LinearLayout llSalir = (LinearLayout) findViewById(R.id.llSalir);
        llSalir.setOnClickListener(this);
        mapaDireccion = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.mapaDireccion);

        editDireccion = (AutoCompleteTextView) findViewById(R.id.editDireccion);
        tvDirComplemento = (EditText) findViewById(R.id.editDirComplemento);

        /*dirX = tvDir.getText().toString();
        latX = "4.674465";
        lngX = "-74.057422";
        ciudadX = "Bogotá";
        complementoX = tvDirComplemento.getText().toString();*/

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
            case R.id.btnAgregarDir:
                respuesta = true;
                salir();
                break;
            case R.id.llSalir:
                dismiss();
                break;
        }//switch
    }
    private void salir(){
        dismiss();
        if (respuesta)
            respuestaListener.onAgregar(dirX, latX, lngX , complementoX, ciudadX);
    }//private void salir


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        if (googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
        }

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                latitudDireccion = googleMap.getCameraPosition().target.latitude;
                longitudDireccion = googleMap.getCameraPosition().target.longitude;
                //isMapa = false;
                editDireccion.setAdapter(null);
                if (utils.checkInternetConnection(activity, true)) {
                    AsincronaGetDireccionPorCoordenadas asyncDir = new AsincronaGetDireccionPorCoordenadas(String.valueOf(latitudDireccion), String.valueOf(longitudDireccion), activity, 1);
                    asyncDir.execute();
                }
            }
        });
    }


}
