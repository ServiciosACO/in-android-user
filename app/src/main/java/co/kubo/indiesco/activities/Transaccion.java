package co.kubo.indiesco.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.dialog.DialogDosOpciones;
import co.kubo.indiesco.dialog.DialogProgress;
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

    Utils utils = new Utils();
    private String id_solicitud, urlPago;
    private boolean cancelarTransaccion, bandVolver = false;
    private DialogProgress dialogProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaccion);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);
        webViewTransaccion.getSettings().setJavaScriptEnabled(true);

        Bundle param = getIntent().getExtras();
        urlPago = param.getString("url");
        id_solicitud = param.getString("id_sol");
        cancelarTransaccion = true;

        if (utils.checkInternetConnection(Transaccion.this, true)){
            initWebView();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBotonVolver:
                opcionVolver();
                break;
            default:break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        opcionVolver();
        return super.onKeyDown(keyCode, event);
    }

    private void opcionVolver(){
        if (cancelarTransaccion){
            if (webViewTransaccion.canGoBack()){
                webViewTransaccion.goBack();
            }else{
                if (utils.checkInternetConnection(this, true)) {
                    new DialogDosOpciones(Transaccion.this, "2", new DialogDosOpciones.RespuestaListener() {
                        @Override
                        public void onCancelar() {
                        }
                        @Override
                        public void onAceptar() {
                            cancelarServicio();
                        }
                        @Override
                        public void onSalir() {
                        }
                    }).show();
                }
            }
        }else{
            irHome();
        }
    }

    /*private void handleBackButton(){
        if (cancelarTransaccion){
            if (Utils.checkInternetConnection(this, true)){
                new DialogDosOpciones(Transaccion.this, "2", new DialogDosOpciones.RespuestaListener() {
                    @Override
                    public void onCancelar() {
                    }
                    @Override
                    public void onAceptar() {
                        cancelarServicio();
                    }
                    @Override
                    public void onSalir() {
                    }
                }).show();
            }
        }else{
            irHome();
        }
    }*/

    private void irHome(){
        Intent intentHome = new Intent(Transaccion.this, Home.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentHome);
        finish();
    }

    private void initWebView() {
        //webViewTransaccion = (WebView) findViewById(R.id.webViewTransaccion);
        webViewTransaccion.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (dialogProgress == null) {
                    dialogProgress = new DialogProgress(Transaccion.this);
                    dialogProgress.show();
                }
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                if (url.contains("response")) {
                    cancelarTransaccion = false;
                    if (bandVolver){
                        irHome();
                    }
                    bandVolver = true;
                }
                super.onPageFinished(view, url);
            }
        });

        webViewTransaccion.setWebChromeClient(new WebChromeClient());
        WebSettings settings = webViewTransaccion.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        webViewTransaccion.loadUrl(urlPago);
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
                        irHome();
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