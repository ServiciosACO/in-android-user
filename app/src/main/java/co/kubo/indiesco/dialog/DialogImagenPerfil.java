package co.kubo.indiesco.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.kubo.indiesco.R;

/**
 * Created by Diego on 7/02/2018.
 */

public class DialogImagenPerfil extends Dialog implements View.OnClickListener {

    private Activity activity;
    private boolean respuesta;
    private RespuestaListener respuestaListener;

    public DialogImagenPerfil(Activity activity, RespuestaListener respuestaListener) {
        super(activity, R.style.ThemeTransparent);
        this.activity = activity;
        this.respuestaListener = respuestaListener;
    }

    public interface RespuestaListener {
        void onCamara();
        void onGaleria();
        void onSalir();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.item_dialog_imagen_perfil);
        Window window = this.getWindow();
        if (window != null){
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams
                    .MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }//if

        LinearLayout llCamara = (LinearLayout) findViewById(R.id.llCamara);
        llCamara.setOnClickListener(this);
        LinearLayout llGaleria = (LinearLayout) findViewById(R.id.llGaleria);
        llGaleria.setOnClickListener(this);
        LinearLayout llOpImagen = (LinearLayout) findViewById(R.id.llOpImagen);
        llOpImagen.setOnClickListener(this);

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
            case R.id.llCamara:
                respuesta = true;
                salir();
                break;
            case R.id.llGaleria:
                respuesta = false;
                salir();
                break;
            case R.id.llOpImagen:
                dismiss();
                respuestaListener.onSalir();
                break;
        }//switch
    }
    private void salir(){
        dismiss();
        if (respuesta) {
            respuestaListener.onCamara();
        } else {
            respuestaListener.onGaleria();
        }
    }//private void salir
}
