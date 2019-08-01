package co.kubo.indiesco.adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.modelo.Direccion;
import co.kubo.indiesco.utils.Singleton;

/**
 * Created by Diego on 15/02/2018.
 */

public class MisDireccionesAdapter2 extends RecyclerView.Adapter<MisDireccionesAdapter2.MisDireccionesViewHolder> {

    public static final String TAG = "MisDireccionesAdapter";
    private ArrayList<Direccion> direccion;
    Activity activity;
    private DialogProgress dialogProgress;
    private Singleton singleton = Singleton.getInstance();
    private IAddress iAddress;

    public MisDireccionesAdapter2(ArrayList<Direccion> direccion, Activity activity, IAddress iAddress) {
        this.direccion = direccion;
        this.activity = activity;
        this.iAddress = iAddress;
    }

    @Override
    public MisDireccionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mis_direcciones2, parent, false);
        return new MisDireccionesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MisDireccionesViewHolder holder, final int position) {
        final Direccion dir = direccion.get(position);
        holder.tvDir.setText(dir.getDireccion());
        holder.tvComplemento.setText(dir.getComplemento());
        holder.tvCiudad.setText(dir.getCityRegion());

        holder.radioButton.setChecked(dir.isCheck());
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < direccion.size(); i++) {
                    direccion.get(i).setCheck(false);
                }
                notifyDataSetChanged();
                dir.setCheck(true);
                singleton.setIdDir(dir.getId_direccion());
                singleton.setDireccion(dir.getDireccion());
                iAddress.AddressCheck();
            }
        });

    }

    @Override
    public int getItemCount() {
        return direccion.size();
    }

    public class MisDireccionesViewHolder extends RecyclerView.ViewHolder {
        TextView tvDir, tvCiudad, tvComplemento;
        LinearLayout llItem;
        RadioButton radioButton;

        public MisDireccionesViewHolder(View itemView) {
            super(itemView);
            tvDir = (TextView) itemView.findViewById(R.id.tvDir);
            tvComplemento = (TextView) itemView.findViewById(R.id.tvComplemento);
            tvCiudad = (TextView) itemView.findViewById(R.id.tvCiudad);
            radioButton = (RadioButton) itemView.findViewById(R.id.radioButton);
            llItem = (LinearLayout) itemView.findViewById(R.id.llItem);
        }
    }
}

