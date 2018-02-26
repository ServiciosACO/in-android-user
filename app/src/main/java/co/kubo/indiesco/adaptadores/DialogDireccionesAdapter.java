package co.kubo.indiesco.adaptadores;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.Registro;
import co.kubo.indiesco.dialog.DialogDirecciones;
import co.kubo.indiesco.interfaces.IDialogDireccionesView;
import co.kubo.indiesco.modelo.Direccion;

/**
 * Created by estacion on 20/02/18.
 */

public class DialogDireccionesAdapter extends RecyclerView.Adapter<DialogDireccionesAdapter.DialogDireccionesViewHolder> {

    public static final String TAG = "DialogDireccionesAdapter";
    private ArrayList<Direccion> direccion;
    Activity activity;
    DialogDirecciones dialogDirecciones;

    public DialogDireccionesAdapter(ArrayList<Direccion> direccion, Activity activity, DialogDirecciones dialogDirecciones) {
        this.direccion = direccion;
        this.activity = activity;
        this.dialogDirecciones = dialogDirecciones;
    }

    @Override
    public DialogDireccionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_dialog_dir, parent, false);
        return new DialogDireccionesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DialogDireccionesViewHolder holder, int position) {
        final Direccion dir = direccion.get(position);
        holder.tvItemDir.setText(dir.getDireccion());
        holder.tvItemCiudad.setText(dir.getCiudad());

        holder.llItemDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDirecciones.setDatos(dir.getDireccion(), dir.getLatitud(), dir.getLongitud());
            }
        });
    }

    @Override
    public int getItemCount() {
        return direccion.size();
    }

    public class DialogDireccionesViewHolder extends RecyclerView.ViewHolder{
        TextView tvItemDir, tvItemCiudad;
        LinearLayout llItemDir;
        public DialogDireccionesViewHolder(View itemView) {
            super(itemView);
            tvItemDir = (TextView) itemView.findViewById(R.id.tvItemDir);
            tvItemCiudad = (TextView) itemView.findViewById(R.id.tvItemCiudad);
            llItemDir = (LinearLayout) itemView.findViewById(R.id.llItemDir);
        }
    }
}

