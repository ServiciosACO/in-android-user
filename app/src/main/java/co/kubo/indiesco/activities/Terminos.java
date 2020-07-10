package co.kubo.indiesco.activities;

import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;

public class Terminos extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Terminos";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.webViewTerminos)
    WebView webViewTerminos;

    private String url = "http://www.google.com";
    private String urlPDF, urlViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);

        /**Para mostrar pdf en pagina web*/
        /*
        urlPDF = "http://kubo.co/downloads/tratamiento.pdf";
        urlViewer = "http://docs.google.com/gview?embedded=true&url=";
        url = urlViewer + urlPDF;
        webViewTerminos.getSettings().setJavaScriptEnabled(true);
        webViewTerminos.getSettings().setPluginState(WebSettings.PluginState.ON);
        webViewTerminos.setWebViewClient(new Callback());
        webViewTerminos.loadUrl(url);
         */

        /**Para mostrar solo pagina web embebido*/
        webViewTerminos.getSettings().setJavaScriptEnabled(true);
        //Para mostrar la pagina web embebida en la app
        webViewTerminos.setWebViewClient(new MyWebViewClient());
        WebSettings settings = webViewTerminos.getSettings();
        settings.setDomStorageEnabled(true);
        webViewTerminos.requestFocus();
        webViewTerminos.loadUrl(getResources().getString(R.string.terms));

    }//protected void onCreate

    /**Para mostrar pdf en pagina web*/
  /*private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return(false);
        }
    } */

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
        super.onBackPressed();
        finish();
    }
}
