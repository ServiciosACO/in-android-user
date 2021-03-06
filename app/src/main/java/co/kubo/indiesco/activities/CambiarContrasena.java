package co.kubo.indiesco.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
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

public class CambiarContrasena extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "CambiarContrasena";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.editpassOld)
    TextInputEditText editpassOld;
    @BindView(R.id.editpass1)
    TextInputEditText editpass1;
    @BindView(R.id.editpass2)
    TextInputEditText editpass2;
    @BindView(R.id.fabSiguiente)
    FloatingActionButton fabSiguiente;
    @BindView(R.id.inputPass1)
    TextInputLayout inputPass1;
    @BindView(R.id.inputPass2)
    TextInputLayout inputPass2;
    @BindView(R.id.tvNotificacionPass)
    TextView tvNotificacionPass;

    Utils utils = new Utils();
    private DialogProgress dialogProgress;
    private String oldPass = "", Pass1 = "", Pass2 = "";
    Animation animShake;
    boolean bandPass1 = false, bandPass2 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);
        ButterKnife.bind(this);
        animShake = AnimationUtils.loadAnimation(CambiarContrasena.this, R.anim.shake);
        imgBotonVolver.setOnClickListener(this);
        fabSiguiente.setOnClickListener(this);
        hideSoftKeyboard();
        listenerEditContrase??a();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            case R.id.fabSiguiente:
                if (validaion()){
                    fabSiguiente.setEnabled(false);
                    hideSoftKeyboard();
                    oldPass = editpassOld.getText().toString();
                    Pass1 = editpass1.getText().toString();
                    Pass2 = editpass2.getText().toString();
                    cambiarPassword();
                }
                break;
        }//switch
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goMiPerfil = new Intent(CambiarContrasena.this, MiPerfil.class);
        goMiPerfil.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goMiPerfil);
        finish();
    }

    private void cambiarPassword(){
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(CambiarContrasena.this);
            dialogProgress.show();
        }
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        Usuario usuario = new Usuario();
        usuario = SharedPreferenceManager.getInfoUsuario(getApplicationContext());
        Call<ResponseGeneral> responseGeneralCall = endpoints.actualizarContrase??a(authToken, usuario.getId_user(), Utils.sha1Encrypt(Pass1), Utils.sha1Encrypt(oldPass));
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){
                    case "100": //OK
                        Toast.makeText(CambiarContrasena.this, "Se ha actualizado la contrase??a", Toast.LENGTH_LONG).show();
                        Intent goMiPerfil = new Intent(CambiarContrasena.this, MiPerfil.class);
                        goMiPerfil.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goMiPerfil);
                        finish();
                        break;
                    case "101":
                        Toast.makeText(CambiarContrasena.this, "La contrase??a anterior no es la misma", Toast.LENGTH_LONG).show();
                        fabSiguiente.setEnabled(true);
                        break;
                    case "102": //Algo fallo
                        Toast.makeText(CambiarContrasena.this, "Algo ha fallado por favor intente de nuevo", Toast.LENGTH_LONG).show();
                        fabSiguiente.setEnabled(true);
                    break;
                    case "120": //auth_token no valido
                        break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "onFailure cambiarPassword");
            }
        });
    }

    private boolean validaion(){
        if (editpassOld.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "La contrase??a anterior es requerida", Toast.LENGTH_SHORT).show();
            //editpassOld.setError("La contrase??a anterior es requerida");
            return false;
        }
        if (editpass1.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "La contrase??a es requerida", Toast.LENGTH_SHORT).show();
            //editpass1.setError("La contrase??a es requerida");
            return false;
        }
        if (editpass2.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(this, "La contrase??a es requerida", Toast.LENGTH_SHORT).show();
            //editpass2.setError("La contrase??a es requerida");
            return false;
        }
        if (!editpass1.getText().toString().equals(editpass2.getText().toString())) {
            Toast.makeText(this, "Las contrase??as no coinciden", Toast.LENGTH_SHORT).show();
            //editpass1.setError("Las contrase??as no coinciden");
            inputPass1.startAnimation(animShake);
            inputPass2.startAnimation(animShake);
            return false;
        }
        if (editpass1.getText().toString().trim().length() < 8) {
            Toast.makeText(this, "La contrase??a debe tener m??nimo 8 d??gitos", Toast.LENGTH_SHORT).show();
            //editpass1.setError("La contrase??a debe tener m??nimo 8 d??gitos");
            return false;
        }
        if (editpass2.getText().toString().trim().length() < 8) {
            Toast.makeText(this, "La contrase??a debe tener m??nimo 8 d??gitos", Toast.LENGTH_SHORT).show();
            //editpass2.setError("La contrase??a debe tener m??nimo 8 d??gitos");
            return false;
        }
        if (!utils.checkInternetConnection(this, true)){
            return false;
        }
        return true;
    }

    private void hideSoftKeyboard(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void listenerEditContrase??a(){
        //Para validar que las contrase??as coinciden
        editpass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandPass1 = true;
                }else{
                    bandPass1 = false;
                }
                validarFAByMensajePag3();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editpass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandPass2 = true;
                }else{
                    bandPass2 = false;
                }
                validarFAByMensajePag3();
            }
            @Override
            public void afterTextChanged(Editable s) {
                tvNotificacionPass.setVisibility(View.GONE);
                if (editpass1.length() == editpass2.length() && !editpass1.getText().toString().equals(editpass2.getText().toString())){
                    tvNotificacionPass.setVisibility(View.VISIBLE);
                    tvNotificacionPass.setText("Las contrase??as no coinciden");
                    tvNotificacionPass.setTextColor(getResources().getColor(R.color.colorRojo));
                    inputPass1.startAnimation(animShake);
                    inputPass2.startAnimation(animShake);
                }
                if (editpass1.length() == editpass2.length() && editpass1.getText().toString().equals(editpass2.getText().toString())){
                    tvNotificacionPass.setVisibility(View.VISIBLE);
                    tvNotificacionPass.setText("Las contrase??as coinciden");
                    tvNotificacionPass.setTextColor(getResources().getColor(R.color.colorVerde));
                }
            }
        });
    }

    private void validarFAByMensajePag3(){
        if (bandPass1&&bandPass2){
            fabSiguiente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorVerde)));
            fabSiguiente.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_done));
        }else{
            fabSiguiente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_hint)));
            fabSiguiente.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_done));
        }//else
    }//private void validarFAByMensajePag3



}

