package co.kubo.indiesco.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseAuthToken;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener {

    static final String TAG = "Login";
    @BindView(R.id.tvTerminosCondiciones)
    TextView tvTerminosCondiciones;
    @BindView(R.id.btnIniciarSesion)
    Button btnIniciarSesion;
    @BindView(R.id.btnCrearCuenta)
    Button btnCrearCuenta;
    private DialogProgress dialogProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        tvTerminosCondiciones.setOnClickListener(this);
        btnCrearCuenta.setOnClickListener(this);
        btnIniciarSesion.setOnClickListener(this);
        obtenerAuthToken();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvTerminosCondiciones:
                Intent inTerminos = new Intent(Login.this, Terminos.class);
                startActivity(inTerminos);
                break;
            case R.id.btnCrearCuenta:
                Intent inRegistro = new Intent(this, Registro.class);
                startActivity(inRegistro);
                break;
            case R.id.btnIniciarSesion:
                Intent inIniciar = new Intent(this, IniciarSesion.class);
                startActivity(inIniciar);
                break;
            default: break;
        }//switch
    }//onClick

    public void obtenerAuthToken(){
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(Login.this);
            dialogProgress.show();
        }
        /**Para obtener Access token*/
        String timeStr = String.valueOf(System.currentTimeMillis()/1000);
        ObtenerAccessToken obAcces = new ObtenerAccessToken(getApplicationContext());
        String num = obAcces.encrypt(getResources().getString(R.string.KEY_TOKEN_API),getResources().getString(R.string.IV),getResources().getString(R.string.SECRET_API)+"/"+timeStr);
        num = num.replace("\n","");
        final String[] token = new String[1];
        token[0] = "";
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Call<ResponseAuthToken> responseAutTokenCall = endpoints.obtenerTokenAuth(num);
        responseAutTokenCall.enqueue(new Callback<ResponseAuthToken>() {
            @Override
            public void onResponse(Call<ResponseAuthToken> call, Response<ResponseAuthToken> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){
                    case "100":
                        token[0] =  response.body().getData().getAuth_token();
                        SharedPreferenceManager.setAuthToken(getApplicationContext(), token[0]);
                        break;
                    case "102":
                        Log.e(TAG, "Hash no valido");
                        break;
                    default: break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseAuthToken> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "onFailure obtenerAuthToken");
                token[0] = "";
            }
        });
    }//public void obtenerAuthToken
}//Login
