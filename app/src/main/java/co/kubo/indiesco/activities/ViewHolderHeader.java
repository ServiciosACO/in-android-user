package co.kubo.indiesco.activities;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;

/**
 * Created by estacion on 22/02/18.
 */

public class ViewHolderHeader extends RecyclerView.ViewHolder {

    @BindView(R.id.tvNotificaciones)
    TextView tvNotificaciones;
    @BindView(R.id.llNotificaciones)
    LinearLayout llNotificaciones;


    public void setTvHeader(String header) {
        tvNotificaciones.setText(header);
    }

    public ViewHolderHeader(View view) {
        super(view);
        ButterKnife.bind(this,view);
    }
}