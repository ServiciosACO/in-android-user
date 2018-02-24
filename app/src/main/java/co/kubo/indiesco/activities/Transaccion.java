package co.kubo.indiesco.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.dialog.DialogDosOpciones;

public class Transaccion extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.webViewTransaccion)
    WebView webViewTransaccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaccion);
        ButterKnife.bind(this);
        webViewTransaccion.getSettings().setJavaScriptEnabled(true);

        Bundle param = getIntent().getExtras();
        String url = param.getString("url");

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
                Intent goHome = new Intent(Transaccion.this, Home.class);
                goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goHome);
                finish();
            }
            @Override
            public void onSalir() {}
        }).show();
    }
}
