package co.kubo.indiesco.activities;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.text.Line;

import java.util.EventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;

/**
 * Created by estacion on 22/02/18.
 */

public class ViewHolderListItem extends RecyclerView.ViewHolder {
    @BindView(R.id.tvHoraNot)
    TextView tvHoraNot;
    @BindView(R.id.tvMensajeNot)
    TextView tvMensajeNot;
    @BindView(R.id.llBorrar)
    LinearLayout llBorrar;

    public LinearLayout getLlBorrar() {
        return llBorrar;
    }

    public void setLlBorrar(LinearLayout llBorrar) {
        this.llBorrar = llBorrar;
    }

    public void setTvHora(String name) {
        tvHoraNot.setText(name);
    }

    public void setTvMsj(String cell) {
        tvMensajeNot.setText(cell);
    }

    public ViewHolderListItem(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}