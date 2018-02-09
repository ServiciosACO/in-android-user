package co.kubo.indiesco.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import co.kubo.indiesco.R;

import static android.widget.Button.*;

/**
 * Created by Diego on 9/02/2018.
 */

//DialogBorrarDir

public class DialogBorrarDir extends Dialog implements View.OnClickListener {

    private Activity activity;
    private boolean respuesta;
    private RespuestaListener respuestaListener;

    public DialogBorrarDir(Activity activity, RespuestaListener respuestaListener) {
        super(activity, R.style.ThemeTransparent);
        this.activity = activity;
        this.respuestaListener = respuestaListener;
    }

    public interface RespuestaListener {
        void onCancelar();
        void onAceptar();
        void onSalir();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.item_dialog_borrar_dir);
        Window window = this.getWindow();
        if (window != null){
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams
                    .MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }//if

        Button btnAceptar = (Button) findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(this);
        Button btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);
        LinearLayout llSalir = (LinearLayout) findViewById(R.id.llSalir);
        llSalir.setOnClickListener(this);

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
            case R.id.btnAceptar:
                respuesta = true;
                salir();
                break;
            case R.id.btnCancelar:
                respuesta = false;
                salir();
                break;
            case R.id.llSalir:
                dismiss();
                break;
        }//switch
    }
    private void salir(){
        dismiss();
        if (respuesta) {
            respuestaListener.onCancelar();
        } else {
            respuestaListener.onAceptar();
        }
    }//private void salir
}
