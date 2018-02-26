package co.kubo.indiesco.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.dialog.DialogDosOpciones;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import co.kubo.indiesco.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Transaccion extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Transaccion";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.webViewTransaccion)
    WebView webViewTransaccion;

    String id_solicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaccion);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);
        webViewTransaccion.getSettings().setJavaScriptEnabled(true);

        Bundle param = getIntent().getExtras();
        String url = param.getString("url");
        id_solicitud = param.getString("id_sol");

        //Para mostrar la pagina web embebida en la app
        webViewTransaccion.loadUrl(url);
        webViewTransaccion.setWebViewClient(new MyWebViewClient());
        webViewTransaccion.requestFocus();
    }

    //Para mostrar la pagina web embebida en la app
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            default:break;
        }
    }

    @Override
    public void onBackPressed() {
        new DialogDosOpciones(Transaccion.this, "2", new DialogDosOpciones.RespuestaListener() {
            @Override
            public void onCancelar() {}
            @Override
            public void onAceptar() {
                if (Utils.checkInternetConnection(Transaccion.this, true))
                cancelarServicio();
            }
            @Override
            public void onSalir() {}
        }).show();
    }

    private void cancelarServicio(){
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        Call<ResponseGeneral> responseGeneralCall = endpoints.cancelarServicio(authToken, id_solicitud, usuario.getId_user());
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        Intent goHome = new Intent(Transaccion.this, Home.class);
                        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goHome);
                        finish();
                        break;
                    case "102":
                        Toast.makeText(getApplicationContext(), "La solicitud no se puede cancelar", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Cod: 102 No se puede cancelar la solicitud");
                        break;
                    case "120":
                        Log.e(TAG, "Cod: 120 auth token invalido");
                        break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                Log.e(TAG, "cancelarServicio onFailure");
            }
        });
    }//private void cancelarServicio
}