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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.Calificar;
import co.kubo.indiesco.activities.MisDirecciones;

/**
 * Created by Diego on 25/02/2018.
 */

public class DialogPendienteCalificar extends Dialog implements View.OnClickListener {
    public static final String TAG = "DialogPendienteCalificar";
    private Activity activity;
    private boolean respuesta;
    private RespuestaListener respuestaListener;


    public DialogPendienteCalificar(Activity activity, RespuestaListener respuestaListener) {
        super(activity, R.style.ThemeTransparent);
        this.activity = activity;
        this.respuestaListener = respuestaListener;
    }

    public interface RespuestaListener {
        void onIrCalificar();
        void onSalir();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.item_dialog_pendiente_calificar);
        Window window = this.getWindow();
        if (window != null) {
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams
                    .MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }//if

        LinearLayout llSalir = (LinearLayout) findViewById(R.id.llSalir);
        llSalir.setOnClickListener(this);
        TextView tvCalificar = (TextView) findViewById(R.id.tvCalificar);
        tvCalificar.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.tvCalificar:
                respuesta = true;
                salir();
                break;
            case R.id.llSalir:
                respuesta = false;
                salir();
                break;
        }//switch
    }

    private void salir() {
        dismiss();
        if (respuesta){
            respuestaListener.onIrCalificar();
        }else{
            respuestaListener.onSalir();
            dismiss();
        }
    }//private void salir
}