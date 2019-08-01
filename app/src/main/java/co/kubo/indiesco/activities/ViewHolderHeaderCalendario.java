package co.kubo.indiesco.activities;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;

/**
 * Created by Diego on 24/02/2018.
 */

public class ViewHolderHeaderCalendario extends RecyclerView.ViewHolder {

    @BindView(R.id.tvCalendario)
    TextView tvCalendario;
    @BindView(R.id.llCalendario)
    LinearLayout llCalendario;


    public void setTvHeader(String header) {
        tvCalendario.setText(header);
    }

    public ViewHolderHeaderCalendario(View view) {
        super(view);
        ButterKnife.bind(this,view);
    }
}
