package co.kubo.indiesco.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.restAPI.ConstantesRestApi;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import co.kubo.indiesco.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OlvidoContrasena extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "OlvidoContrasena";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editCodigo1)
    TextView editCodigo1;
    @BindView(R.id.editCodigo2)
    TextView editCodigo2;
    @BindView(R.id.editCodigo3)
    TextView editCodigo3;
    @BindView(R.id.editCodigo4)
    TextView editCodigo4;
    @BindView(R.id.editpass1)
    TextInputEditText editpass1;
    @BindView(R.id.editpass2)
    TextInputEditText editpass2;
    @BindView(R.id.tvNotificacionPass)
    TextView tvNotificacionPass;
    @BindView(R.id.tvNotificacionCodigo)
    TextView tvNotificacionCodigo;
    @BindView(R.id.fabSiguiente)
    FloatingActionButton fabSiguiente;
    @BindView(R.id.scrollViewPagina1)
    ScrollView scrollViewPagina1;
    @BindView(R.id.scrollViewPagina2)
    ScrollView scrollViewPagina2;
    @BindView(R.id.scrollViewPagina3)
    ScrollView scrollViewPagina3;
    @BindView(R.id.inputPass1)
    TextInputLayout inputPass1;
    @BindView(R.id.inputPass2)
    TextInputLayout inputPass2;

    boolean pag1 = true, pag2 = false, pag3 = false, bandCod1 = false, bandCod2 = false,
            bandCod3 = false, bandCod4 = false, bandPass1 = false, bandPass2 = false;
    private String email = "", password = "", RepetirPassword = "", codigo = "", temp = "", codMaster = "0000";

    Utils utils = new Utils();
    Animation anim1, anim2, animShake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido_contrasena);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);
        fabSiguiente.setOnClickListener(this);
        anim1 = AnimationUtils.loadAnimation(OlvidoContrasena.this, R.anim.alpha);
        anim2 = AnimationUtils.loadAnimation(OlvidoContrasena.this, R.anim.alpha2);
        animShake = AnimationUtils.loadAnimation(OlvidoContrasena.this, R.anim.shake);

        setlistenerCodigo();

        //fabSiguiente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorVerde)));
        //fabSiguiente.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_done));

//        fabSiguiente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorNaranja)));
//        fabSiguiente.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_next));
    }

    private void setlistenerCodigo(){
        editCodigo1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandCod1 = true;
                }else{
                    bandCod1 = false;
                }
                validarFAByMensaje();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editCodigo2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandCod2 = true;
                }else{
                    bandCod2 = false;
                }
                validarFAByMensaje();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editCodigo3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandCod3 = true;
                }else{
                    bandCod3 = false;
                }
                validarFAByMensaje();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editCodigo4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandCod4 = true;
                }else{
                    bandCod4 = false;
                }
                validarFAByMensaje();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Para validar que las contraseñas coinciden
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
            public void afterTextChanged(Editable s) {}
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

    private void validarFAByMensaje(){
        if (bandCod1&&bandCod2&&bandCod3&&bandCod4){
            fabSiguiente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorNaranja)));
            temp = editCodigo1.getText().toString().concat(editCodigo2.getText().toString().concat(editCodigo3.getText().toString().concat(editCodigo4.getText().toString())));
            if (temp.equals(codMaster)){
                tvNotificacionCodigo.setVisibility(View.VISIBLE);
                tvNotificacionCodigo.setText(getResources().getString(R.string.olvido_contraseña2_respuesta_edit));
                tvNotificacionCodigo.setTextColor(getResources().getColor(R.color.colorVerde));
                pag2 = true;
            }else{
                tvNotificacionCodigo.setVisibility(View.VISIBLE);
                tvNotificacionCodigo.setText(getResources().getString(R.string.olvido_contraseña2_respuesta_edit_2));
                tvNotificacionCodigo.setTextColor(getResources().getColor(R.color.colorRojo));
                tvNotificacionCodigo.startAnimation(animShake);
                pag2 = false;
            }//else
        }else{
            fabSiguiente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_hint)));
            tvNotificacionCodigo.setVisibility(View.GONE);
            pag2 = false;
        }
    }//validarFAByMensaje

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            case R.id.fabSiguiente:
                animacion();
                break;
        }//switch
    }//onClick

    @Override
    public void onBackPressed() {
        //Entra a la pagina 1
        if (scrollViewPagina1.getVisibility() == View.VISIBLE){
            super.onBackPressed();
            finish();
        }
        //Entra a la pagina 2
        if (scrollViewPagina2.getVisibility() == View.VISIBLE){
            scrollViewPagina2.setVisibility(View.GONE);
            scrollViewPagina1.setVisibility(View.VISIBLE);
            scrollViewPagina1.startAnimation(anim2);
            editEmail.setText("");
        }
        //Entra a la pagina 3
        if (scrollViewPagina3.getVisibility() == View.VISIBLE){
            scrollViewPagina3.setVisibility(View.GONE);
            scrollViewPagina2.setVisibility(View.VISIBLE);
            scrollViewPagina2.startAnimation(anim2);
            editCodigo1.setText("");
            editCodigo2.setText("");
            editCodigo3.setText("");
            editCodigo4.setText("");
        }//if
    }//onBackPressed

    private boolean validacion(){
        if (editEmail.getText().toString().trim().equalsIgnoreCase("")) {
            editEmail.setError("El correo electrónico es requerido");
            return false;
        }
        if (!utils.isEmailValid(editEmail.getText().toString())){
            editEmail.setError("Debe ingresar un correo electrónico valido");
            return false;
        }
        if (!Utils.checkInternetConnection(this, true)){
            return false;
        }
        return true;
    }

    private boolean validacionPassword(){
        if (editpass1.getText().toString().trim().equalsIgnoreCase("")) {
            editpass1.setError("La contraseña es requerida");
            return false;
        }
        if (editpass2.getText().toString().trim().equalsIgnoreCase("")) {
            editpass2.setError("La contraseña es requerida");
            return false;
        }
        if (!editpass1.getText().toString().equals(editpass2.getText().toString())) {
            editpass1.setError("Las contraseñas no coinciden");
            inputPass1.startAnimation(animShake);
            inputPass2.startAnimation(animShake);
            return false;
        }
        if (!Utils.checkInternetConnection(this, true)){
            return false;
        }
        return true;
    }

    private void animacion(){
        //Entra a la pagina 1
        if (scrollViewPagina1.getVisibility() == View.VISIBLE){
            //Consulta servicio para validar email
            //si es correcto sigue
            if (validacion()){
                email = editEmail.getText().toString();
                validarEmail(email);
            }
        }
        //Entra a la pagina 2
        if (scrollViewPagina2.getVisibility() == View.VISIBLE){
            if (pag2){
                scrollViewPagina2.setVisibility(View.GONE);
                scrollViewPagina3.setVisibility(View.VISIBLE);
                scrollViewPagina3.startAnimation(anim1);
                hideSoftKeyboard();
                fabSiguiente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_hint)));
                fabSiguiente.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_done));
            }else{
                Toast.makeText(this, "Debe llenar todos los campos para continuar", Toast.LENGTH_SHORT).show();
            }//else
        }
        //Entra a la pagina 3
        if (scrollViewPagina3.getVisibility() == View.VISIBLE){
            if (validacionPassword()){
                Intent goHome = new Intent(this, Home.class);
                goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goHome);
                finish();
            }else{
                Toast.makeText(this, "Debe llenar todos los campos para continuar", Toast.LENGTH_SHORT).show();
            }//else
        }//if
    }//animacion

    public void recuperarPass(final String email){
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        String url = ConstantesRestApi.URL_RECUPERAR_CONTRASEÑA + email;
        Call<ResponseGeneral> responseGeneralCall = endpoints.recuperarPassword(authToken, url);
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                String code = response.body().getCode();
                switch (code){
                    case "100": //OK
                        Toast.makeText(OlvidoContrasena.this, "El codigo ha sido enviado a su correo electrónico", Toast.LENGTH_LONG).show();
                        scrollViewPagina1.setVisibility(View.GONE);
                        scrollViewPagina2.setVisibility(View.VISIBLE);
                        scrollViewPagina2.startAnimation(anim1);
                        hideSoftKeyboard();
                        break;
                    case "102": //Algo fallo
                        Toast.makeText(OlvidoContrasena.this, "Algo ha fallado por favor intente de nuevo", Toast.LENGTH_LONG).show();
                        break;
                    case "120": //auth_token no valido
                        break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                Log.e(TAG, "onFailure recuperarPass");
            }
        });
    }//public void validarEmail

    public void validarEmail(final String email){
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        String url = ConstantesRestApi.URL_VALIDAR_EMAIL + email;
        Call<ResponseGeneral> responseGeneralCall = endpoints.validarEmail(authToken, url);
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                String code = response.body().getCode();
                switch (code){
                    case "100": //Cuenta existe llama servicio recuperarPass
                        recuperarPass(email);
                        break;
                    case "101": //Email no existe
                        Toast.makeText(OlvidoContrasena.this, "El correo eletrónico ingresado es invalido", Toast.LENGTH_LONG).show();
                        break;
                    case "103": // Cuenta inactiva
                        Toast.makeText(OlvidoContrasena.this, "Su cuenta se encuentra incativa", Toast.LENGTH_LONG).show();
                        break;
                    case "120": //auth_token no valido
                        break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                Log.e(TAG, "onFailure validarEmail");
            }
        });
    }//public void validarEmail
    private void hideSoftKeyboard(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
