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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.kubo.indiesco.R;

import static android.widget.Button.*;

/**
 * Created by Diego on 9/02/2018.
 */

//DialogDosOpciones

public class DialogDosOpciones extends Dialog implements View.OnClickListener {

    private Activity activity;
    private boolean respuesta;
    private String opcion;
    private RespuestaListener respuestaListener;

    public DialogDosOpciones(Activity activity, String opcion, RespuestaListener respuestaListener) {
        super(activity, R.style.ThemeTransparent);
        this.activity = activity;
        this.opcion = opcion;
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

        ImageView imgBorrar = (ImageView) findViewById(R.id.imgBorrar);
        TextView tvMensaje = (TextView) findViewById(R.id.tvMensaje);

        switch (opcion){
            case "0": //Borrar direccion
                imgBorrar.setImageResource(R.drawable.img_deleteaddress);
                tvMensaje.setText("¿Estás seguro de que deseas eliminar esta dirección?");
                break;
            case "1": //Borrar Notificacion
                imgBorrar.setImageResource(R.drawable.img_deletenotif);
                tvMensaje.setText("¿Estás seguro de que deseas eliminar esta notificación?");
                break;
            case "2": //Cancelar servicio
                imgBorrar.setImageResource(R.drawable.img_cancel_service);
                tvMensaje.setText("¿Estás seguro de que deseas cancelar el servicio?");
                break;
            default:break;
        }//switch

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
            respuestaListener.onAceptar();
        } else {
            respuestaListener.onCancelar();
        }
    }//private void salir
}
