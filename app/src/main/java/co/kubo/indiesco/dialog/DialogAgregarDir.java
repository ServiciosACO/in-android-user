package co.kubo.indiesco.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.MisDirecciones;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Diego on 17/02/2018.
 */

public class DialogAgregarDir extends Dialog implements View.OnClickListener {

    public static final String TAG = "DialogAgregarDir";
    private Activity activity;
    private boolean respuesta;
    private RespuestaListener respuestaListener;
    private String dirX, latX = "4.674465", lngX = "-74.057422", complementoX, ciudadX;

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
        WebView webViewDir = (WebView) findViewById(R.id.webViewDir);
        EditText tvDir = (EditText) findViewById(R.id.tvDir);
        EditText tvDirComplemento = (EditText) findViewById(R.id.tvDirComplemento);
        dirX = tvDir.getText().toString();
        latX = "4.674465";
        lngX = "-74.057422";
        ciudadX = "Bogotá";
        complementoX = tvDirComplemento.getText().toString();

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

}
