package co.kubo.indiesco.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.interfaces.IDialogDireccionesView;
import co.kubo.indiesco.interfaces.IMisDireccionesPresenter;
import co.kubo.indiesco.activities.MisDirecciones;
import co.kubo.indiesco.adaptadores.DialogDireccionesAdapter;
import co.kubo.indiesco.modelo.Direccion;
import co.kubo.indiesco.presenter.DialogDireccionesPresenter;

/**
 * Created by estacion on 20/02/18.
 */

public class DialogDirecciones extends Dialog implements View.OnClickListener, IDialogDireccionesView {
    public static final String TAG = "DialogDirecciones";
    RecyclerView rvDirDialog;
    private Activity activity;
    private boolean respuesta;
    private RespuestaListener respuestaListener;
    private String dirX, latX = "4.674465", lngX = "-74.057422", complementoX, ciudadX;
    IDialogDireccionesView iDialogDireccionesView;
    IMisDireccionesPresenter presenter;

    public DialogDirecciones(Activity activity, RespuestaListener respuestaListener) {
        super(activity, R.style.ThemeTransparent);
        this.activity = activity;
        this.respuestaListener = respuestaListener;

        presenter = new DialogDireccionesPresenter(DialogDirecciones.this, activity.getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.item_dialog_direcciones);
        Window window = this.getWindow();
        if (window != null){
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams
                    .MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }//if

        rvDirDialog = (RecyclerView) findViewById(R.id.rvDirDialog);
        LinearLayout llSalir = (LinearLayout) findViewById(R.id.llSalir);
        llSalir.setOnClickListener(this);
        TextView tvIrMisDirecciones = (TextView) findViewById(R.id.tvIrMisDirecciones);
        tvIrMisDirecciones.setOnClickListener(this);

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
            case R.id.tvIrMisDirecciones:
                Intent inDir = new Intent(activity, MisDirecciones.class);
                activity.startActivity(inDir);
                dismiss();
                break;
            case R.id.llSalir:
                dismiss();
                break;
        }//switch
    }
    private void salir(){
        dismiss();
        if (respuesta)
            respuestaListener.onSelectDir(dirX, latX, lngX , complementoX, ciudadX);
    }//private void salir

    @Override
    public void generarLinearLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvDirDialog.setLayoutManager(llm);
    }
    @Override
    public void inicializarAdaptadorRvDirecciones(DialogDireccionesAdapter dialogDireccionesAdapter) {
        rvDirDialog.setAdapter(dialogDireccionesAdapter);
    }

    @Override
    public DialogDireccionesAdapter crearAdaptadorDialogDirecciones(ArrayList<Direccion> direccions) {
        DialogDireccionesAdapter dialogDireccionesAdapter = new DialogDireccionesAdapter(direccions, activity, DialogDirecciones.this);
        return dialogDireccionesAdapter;
    }

    @Override
    public void setDatos(String dir, String lat, String lng) {
        dirX = dir;
        latX = lat;
        lngX = lng;
        respuestaListener.onSelectDir(dir, lat, lng, complementoX, ciudadX);
        dismiss();
    }

    public interface RespuestaListener {
        void onSelectDir(String dir, String lat, String lng, String complemento, String ciudad);
        void onIrMisDir();
        void onSalir();
    }

    private void otro(){

    }
}