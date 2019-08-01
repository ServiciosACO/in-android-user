package co.kubo.indiesco.activities;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.EventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;

/**
 * Created by Diego on 24/02/2018.
 */

public class ViewHolderListItemCalendario extends RecyclerView.ViewHolder {

    @BindView(R.id.tvFecha)
    TextView tvFecha;
    @BindView(R.id.tvDir)
    TextView tvDir;
    @BindView(R.id.tvHora)
    TextView tvHoraCalendar;
    @BindView(R.id.llItemCalendario)
    LinearLayout llItemCalendario;

    public LinearLayout getLlItemCalendario() {
        return llItemCalendario;
    }

    /*public void setLlItemCalendario(EventListener eventListener) {
        this.llItemCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //this.llItemCalendario = llItemCalendario;
    }*/

    public void setTvFecha(String fecha) {
        tvFecha.setText(fecha);
    }
    public void setTvDir(String dir) {
        tvDir.setText(dir);
    }
    public void setTvHoraCalendar(String hora) {
        tvHoraCalendar.setText(hora);
    }

    public ViewHolderListItemCalendario(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}