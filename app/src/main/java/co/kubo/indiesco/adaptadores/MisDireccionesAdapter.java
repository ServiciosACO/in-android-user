package co.kubo.indiesco.adaptadores;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.dialog.DialogDosOpciones;
import co.kubo.indiesco.modelo.Direccion;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.utils.SharedPreferenceManager;
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

    public MisDireccionesAdapter(ArrayList<Direccion> direccion, Activity activity) {
        this.direccion = direccion;
        this.activity = activity;
    }
    @Override
    public MisDireccionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mis_direcciones, parent, false);
        return new MisDireccionesViewHolder(v);
    }
    @Override
    public void onBindViewHolder(MisDireccionesViewHolder holder, int position) {
        final Direccion dir = direccion.get(position);
        holder.tvDir.setText(String.valueOf(dir.getDireccion()));
        holder.tvCiudad.setText(String.valueOf(dir.getCiudad()));
        String url = "http://maps.google.com/maps/api/staticmap?center=" + dir.getLatitud() + "," + dir.getLongitud() + "&zoom=15&size=200x200&sensor=false";
        holder.webViewMap.loadUrl(url);

        holder.llBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogDosOpciones(activity, "0", new DialogDosOpciones.RespuestaListener() {
                    @Override
                    public void onCancelar() {
                    }
                    @Override
                    public void onAceptar() {
                        borrarDir(dir.getId_direccion());
                    }
                    @Override
                    public void onSalir() {
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return direccion.size();
    }

    public class MisDireccionesViewHolder extends RecyclerView.ViewHolder{
        TextView tvDir, tvCiudad;
        WebView webViewMap;
        LinearLayout llBorrar;
        public MisDireccionesViewHolder(View itemView) {
            super(itemView);
            tvDir = (TextView) itemView.findViewById(R.id.tvDir);
            tvCiudad = (TextView) itemView.findViewById(R.id.tvCiudad);
            webViewMap = (WebView) itemView.findViewById(R.id.webViewMap);
            llBorrar = (LinearLayout) itemView.findViewById(R.id.llBorrar);
        }
    }

    private void borrarDir(String id_dir){
        String authToken = SharedPreferenceManager.getAuthToken(activity);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(activity);
        Call<ResponseGeneral> responseGeneralCall = endpoints.eliminarDireccion(authToken, usuario.getId_user(), id_dir);
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        Toast.makeText(activity, "Elimino la dirección con éxito", Toast.LENGTH_LONG).show();
                        break;
                    case "102":
                        Toast.makeText(activity, "Ha ocurrido un error intente de nuevo", Toast.LENGTH_SHORT).show();
                        break;
                    case "120":
                        Log.e(TAG, "borrarDir, auth_token no valido");
                        break;
                    default: break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                Log.e(TAG, "borrarDir, onFailure");
            }
        });

    }//private void borrarDir
}
