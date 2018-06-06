package co.kubo.indiesco.adaptadores;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.modelo.Direccion;
import co.kubo.indiesco.modelo.RecargoVO;
import co.kubo.indiesco.utils.Singleton;

/**
 * Created by Diego on 15/02/2018.
 */

public class AdapterRecargos extends RecyclerView.Adapter<AdapterRecargos.RecargoViewHolder> {

    public static final String TAG = "AdapterRecargos";
    DecimalFormat df = new DecimalFormat("$###,###.##");
    private ArrayList<RecargoVO> recargo;
    Activity activity;

    public AdapterRecargos(ArrayList<RecargoVO> recargo, Activity activity) {
        this.recargo = recargo;
        this.activity = activity;
    }
    @Override
    public RecargoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recharge, parent, false);
        return new RecargoViewHolder(v);
    }
    @Override
    public void onBindViewHolder(RecargoViewHolder holder, final int position) {
        final RecargoVO temp = recargo.get(position);
        holder.tvDate.setText(temp.getFecha());
        holder.tvMeasure.setText(temp.getMts() + " " + activity.getResources().getString(R.string.cuadrado));
        holder.tvRecharge.setText("Recargo: " + df.format(Integer.parseInt(temp.getValor())));
    }
    @Override
    public int getItemCount() {
        return recargo.size();
    }

    public class RecargoViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate, tvMeasure, tvRecharge;

        public RecargoViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvMeasure = (TextView) itemView.findViewById(R.id.tvMeasure);
            tvRecharge = (TextView) itemView.findViewById(R.id.tvRecharge);
        }
    }
}
