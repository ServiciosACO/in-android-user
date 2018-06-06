package co.kubo.indiesco.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import co.kubo.indiesco.R;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponsePendienteCalificar;
import co.kubo.indiesco.utils.Constantes;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {

    public static final String TAG = "Home";
    private boolean primeraVez = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        primeraVez = SharedPreferenceManager.getPrimeraVez(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent in = null;
                if (!primeraVez){
                    in = new Intent(Splash.this, Tour.class);
                }else{
                    if (SharedPreferenceManager.getLoged(Splash.this)){
                        in = new Intent(Splash.this, Home.class);
                    }else{
                        in = new Intent(Splash.this, Login.class);
                    }//else
                }//else
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
            }//run
        },Constantes.tiempoSplash);
    }//protected void onCreate

}
