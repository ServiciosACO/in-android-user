package co.kubo.indiesco.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import co.kubo.indiesco.R;
import co.kubo.indiesco.utils.Utils;

/**
 * Created by Diego on 12/02/2018.
 */

public class DialogDetalleHistorial extends Dialog implements View.OnClickListener {

    private Activity activity;
    private String lat, lng, nServicio, dir, ciudad, tipoTipo, fecha, hora, valor, dimension;
    private RespuestaListener respuestaListener;
    Utils utils = new Utils();

    public DialogDetalleHistorial(Activity activity, String lat, String lng, String nServicio, String dir,
                                  String ciudad, String dimension,  String tipoTipo, String fecha, String hora, String valor,
                                  RespuestaListener respuestaListener) {
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
        if (window != null){
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams
                    .MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }//if

        WebView webViewHistorial = (WebView) findViewById(R.id.webViewHistorial);
        TextView tvNoServicioDetHist = (TextView) findViewById(R.id.tvNoServicioDetHist);
        TextView tvDirDetalleHist = (TextView) findViewById(R.id.tvDirDetalleHist);
        TextView tvCiudadDetalle = (TextView) findViewById(R.id.tvCiudadDetalle);
        TextView tvTipoTipo = (TextView) findViewById(R.id.tvTipoTipo);
        TextView tvFechaDetalle = (TextView) findViewById(R.id.tvFechaDetalle);
        TextView tvHoraDetalle = (TextView) findViewById(R.id.tvHoraDetalle);
        TextView tvPrecioServicioDet = (TextView) findViewById(R.id.tvPrecioServicioDet);

        String url = "http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=15&size=280x180&sensor=false";
        webViewHistorial.loadUrl(url);
        tvNoServicioDetHist.setText(nServicio);
        tvDirDetalleHist.setText(dir);
        tvCiudadDetalle.setText(ciudad);
        tvFechaDetalle.setText(utils.StringToDate2(fecha));
        tvHoraDetalle.setText(hora);
        tvPrecioServicioDet.setText(valor);
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
