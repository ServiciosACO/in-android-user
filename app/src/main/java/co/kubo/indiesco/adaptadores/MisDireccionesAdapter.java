package co.kubo.indiesco.adaptadores;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.ActivityNuevaDireccionSinGps;
import co.kubo.indiesco.activities.Calificar;
import co.kubo.indiesco.dialog.DialogDosOpciones;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.interfaces.IMisDireccionesView;
import co.kubo.indiesco.modelo.Direccion;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import co.kubo.indiesco.utils.Singleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Diego on 15/02/2018.
 */

public class MisDireccionesAdapter extends RecyclerView.Adapter<MisDireccionesAdapter.MisDireccionesViewHolder> {

    public static final String TAG = "MisDireccionesAdapter";
    private ArrayList<Direccion> direccion;
    Activity activity;
    private DialogProgress dialogProgress;
    IMisDireccionesView iMisDireccionesView;
    private Singleton general = Singleton.getInstance();

    public MisDireccionesAdapter(ArrayList<Direccion> direccion, Activity activity, IMisDireccionesView iMisDireccionesView) {
        this.direccion = direccion;
        this.activity = activity;
        this.iMisDireccionesView = iMisDireccionesView;
    }

    @Override
    public MisDireccionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mis_direcciones, parent, false);
        return new MisDireccionesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MisDireccionesViewHolder holder, final int position) {
        final Direccion dir = direccion.get(position);
        holder.tvDir.setText(dir.getDireccion());
        holder.tvComplemento.setText(dir.getComplemento());
        holder.tvCiudad.setText(dir.getCityRegion());

        holder.llBorrarDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogDosOpciones(activity, "0", new DialogDosOpciones.RespuestaListener() {
                    @Override
                    public void onCancelar() {
                    }

                    @Override
                    public void onAceptar() {
                        borrarDir(dir.getId_direccion(), position);
                    }

                    @Override
                    public void onSalir() {
                    }
                }).show();
            }
        });

        holder.llEditarDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                general.setVoDireccion(dir);
                Intent inCal = new Intent(activity, ActivityNuevaDireccionSinGps.class);
                inCal.putExtra("estado", "Editar");
                activity.startActivity(inCal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return direccion.size();
    }

    public class MisDireccionesViewHolder extends RecyclerView.ViewHolder {
        TextView tvDir, tvCiudad, tvComplemento;
        LinearLayout llBorrarDir;
        LinearLayout llEditarDir;
        SwipeLayout mSwipe;
        ImageView imgBorrarDir;

        public MisDireccionesViewHolder(View itemView) {
            super(itemView);
            tvDir = (TextView) itemView.findViewById(R.id.tvDir);
            tvComplemento = (TextView) itemView.findViewById(R.id.tvComplemento);
            tvCiudad = (TextView) itemView.findViewById(R.id.tvCiudad);
            llBorrarDir = (LinearLayout) itemView.findViewById(R.id.llBorrarDir);
            llEditarDir = (LinearLayout) itemView.findViewById(R.id.llEditarDir);
            mSwipe = (SwipeLayout) itemView.findViewById(R.id.mSwipe);
            imgBorrarDir = (ImageView) itemView.findViewById(R.id.imgBorrarDir);
        }
    }

    private void borrarDir(String id_dir, final int adapter_position) {
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(activity);
            dialogProgress.show();
        }
        String authToken = SharedPreferenceManager.getAuthToken(activity);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(activity);
        Call<ResponseGeneral> responseGeneralCall = endpoints.eliminarDireccion(authToken, usuario.getId_user(), id_dir);
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code) {
                    case "100":
                        Toast.makeText(activity, "Elimino la dirección con éxito", Toast.LENGTH_LONG).show();
                        direccion.remove(adapter_position);
                        notifyDataSetChanged();
                        if (direccion.isEmpty()) {
                            iMisDireccionesView.noAddresses();
                        }
                        if (direccion.size() < 5) {
                            iMisDireccionesView.disableButtonAddAddress(true);
                        }
                        break;
                    case "102":
                        Toast.makeText(activity, "Ha ocurrido un error intente de nuevo", Toast.LENGTH_SHORT).show();
                        break;
                    case "120":
                        Log.e(TAG, "borrarDir, auth_token no valido");
                        break;
                    default:
                        break;
                }//switch
            }

            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "borrarDir, onFailure");
            }
        });

    }//private void borrarDir
}
