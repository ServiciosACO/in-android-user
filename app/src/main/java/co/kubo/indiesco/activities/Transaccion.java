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

    private String id_solicitud, urlPago;
    private boolean cancelarTransaccion;
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

        if (Utils.checkInternetConnection(Transaccion.this, true)){
            initWebView();
        }


        /*//Para mostrar la pagina web embebida en la app
        webViewTransaccion.loadUrl(urlPago);
        webViewTransaccion.setWebViewClient(new MyWebViewClient());
        webViewTransaccion.requestFocus();*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBotonVolver:
                if (webViewTransaccion.canGoBack()){
                    webViewTransaccion.goBack();
                }else{
                    handleBackButton();
                }
                break;

            default:break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webViewTransaccion.canGoBack()){
            webViewTransaccion.goBack();
            return true;
        }else{
            handleBackButton();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void handleBackButton(){
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
            finish();
        }
    }
    /*
    //Para mostrar la pagina web embebida en la app
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }*/

    private void initWebView() {
        //webViewTransaccion = (WebView) findViewById(R.id.webViewTransaccion);
        webViewTransaccion.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (dialogProgress == null) {
                    dialogProgress = new DialogProgress(Transaccion.this);
                    dialogProgress.show();
                }
                if (url.contains("resumen_pedido")) {
                    /*if (isProductos) {
                        // TODO: 9/08/2017 ir al historial
                        sqLite.limpiarCarrito();
                        finish();
                    } else {
                        Intent intentHome = new Intent(PagoActivity.this, HomeActivity.class);
                        intentHome.putExtra(Constantes.INTENT_EXTRA_IR_HISTORIAL, true);
                        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentHome);
                        finish();
                    }*/
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