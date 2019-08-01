package co.kubo.indiesco.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.dialog.DialogProgress;
import co.kubo.indiesco.modelo.PendienteCalificar;
import co.kubo.indiesco.modelo.Usuario;
import co.kubo.indiesco.restAPI.ConstantesRestApi;
import co.kubo.indiesco.restAPI.Endpoints;
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.restAPI.modelo.ResponseLogin;
import co.kubo.indiesco.utils.SharedPreferenceManager;
import co.kubo.indiesco.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IniciarSesion extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "IniciarSesion";
    @BindView(R.id.imgBotonVolver)
    ImageView imgBotonVolver;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editContraseña)
    TextInputEditText editContraseña;
    @BindView(R.id.btnContinuar)
    Button btnContinuar;
    @BindView(R.id.tvOlvidoContraseña)
    TextView tvOlvidoContraseña;
    @BindView(R.id.inputPassword)
    TextInputLayout inputPassword;

    private String token = "0", plataforma = "a", email = "", contraseña = "";
    private ArrayList<PendienteCalificar> calificar = new ArrayList<>();
    private DialogProgress dialogProgress;

    Utils utils = new Utils();
    Animation anim1, anim2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        ButterKnife.bind(this);
        imgBotonVolver.setOnClickListener(this);
        editEmail.setOnClickListener(this);
        btnContinuar.setOnClickListener(this);
        tvOlvidoContraseña.setOnClickListener(this);
        anim1 = AnimationUtils.loadAnimation(IniciarSesion.this, R.anim.alpha);
        anim2 = AnimationUtils.loadAnimation(IniciarSesion.this, R.anim.alpha2);

        hideSoftKeyboard();
        setListeners();
    }

    /**Para que al presionar la tecla de ir en el teclado llame al servicio para validar el email*/
    public void setListeners(){
        editEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO){
                    hideSoftKeyboard();
                    if(validacion()) {
                        //Debe validar que existe email en ws, si no me envia a registro
                        animacion();
                    }
                    return true;
                }else{
                    return false;
                }
            }
        });
    }//public void setListeners

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgBotonVolver:
                onBackPressed();
                break;
            case R.id.btnContinuar:
                if (validacion()){
                    animacion();
                }//if
                break;
            case R.id.tvOlvidoContraseña:
                Intent in = new Intent(this, OlvidoContrasena.class);
                startActivity(in);
                break;

        }//switch
    }//onClick

    private void animacion(){
        if (editEmail.getVisibility() == View.VISIBLE){
            hideSoftKeyboard();
            email = editEmail.getText().toString();
            validarEmail(email);
        }else{
            hideSoftKeyboard();
            //consulta el servicio si la contraseña esta bien y se va a home
            //validar si tiene servicios por calificar(Abre servicios) si no home
            contraseña = editContraseña.getText().toString();
            login(contraseña);
        }//else
    }

    @Override
    public void onBackPressed() {
        if (editEmail.getVisibility() == View.VISIBLE){
            super.onBackPressed();
            finish();
        }else{
            inputPassword.setVisibility(View.GONE);
            editEmail.setVisibility(View.VISIBLE);
            editEmail.startAnimation(anim2);
        }
    }

    private Boolean validacion() {
        if (editEmail.getVisibility() == View.VISIBLE){
            if (editEmail.getText().toString().trim().equalsIgnoreCase("")) {
                editEmail.setError("El correo electrónico es requerido");
                return false;
            }
            if (!utils.isEmailValid(editEmail.getText().toString())){
                editEmail.setError("Debe ingresar un correo electrónico valido");
                return false;
            }
            //return (!Utils.checkInternetConnection(this, true));
            if (!utils.checkInternetConnection(this, true)){
                return false;
            }
            return true;
        }else{
            if (editContraseña.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "La contraseña es requerida", Toast.LENGTH_SHORT).show();
                return false;
            }
            //return (!Utils.checkInternetConnection(this, true));
            if (!utils.checkInternetConnection(this, true)){
                return false;
            }
            return true;
        }//else
    }//validacion

    private void hideSoftKeyboard(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        /*//Para usar con fragment
        try{
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }catch (Exception e){
        }*/
    }

    public void validarEmail(final String email){
        btnContinuar.setEnabled(false);
        if (dialogProgress == null) {
            dialogProgress = new DialogProgress(IniciarSesion.this);
            dialogProgress.show();
        }
        final String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        String url = ConstantesRestApi.URL_VALIDAR_EMAIL + email;
        Call<ResponseGeneral> responseGeneralCall = endpoints.validarEmail(authToken, url);
        responseGeneralCall.enqueue(new Callback<ResponseGeneral>() {
            @Override
            public void onResponse(Call<ResponseGeneral> call, Response<ResponseGeneral> response) {
                btnContinuar.setEnabled(true);
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){
                    case "100": //Cuenta exitente va a login
                        hideSoftKeyboard();
                        editEmail.setVisibility(View.GONE);
                        inputPassword.setVisibility(View.VISIBLE);
                        inputPassword.startAnimation(anim1);
                        editContraseña.setFocusable(true);
                        break;
                    case "101": //Email no existe va a crear cuenta
                        Intent goCrear = new Intent(IniciarSesion.this, Registro.class);
                        goCrear.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        goCrear.putExtra("Email", editEmail.getText().toString());
                        goCrear.putExtra("Validar", "1");
                        startActivity(goCrear);
                        finish();
                        break;
                    case "103": // Cuenta inactiva
                        Toast.makeText(IniciarSesion.this, "Su cuenta se encuentra inactiva", Toast.LENGTH_LONG).show();
                        break;
                    case "120": //auth_token no valido

                        break;
                }//switch
            }
            @Override
            public void onFailure(Call<ResponseGeneral> call, Throwable t) {
                btnContinuar.setEnabled(true);
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "onFailure validarEmail");
            }
        });
    }//public void validarEmail

    public void login(String password){
        btnContinuar.setEnabled(false);
        //if (dialogProgress == null) {
            dialogProgress = new DialogProgress(IniciarSesion.this);
            dialogProgress.show();
        //}
        String authToken = SharedPreferenceManager.getAuthToken(getApplicationContext());
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRestApiSinGson();
        String passSha1 = Utils.sha1Encrypt(password);
        Call<ResponseLogin> responseLoginCall = endpoints.login(authToken, email, passSha1, token, plataforma);
        responseLoginCall.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                btnContinuar.setEnabled(true);
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                String code = response.body().getCode();
                switch (code){
                    case "100": //Login correcto
                        Usuario usuario = new Usuario();
                        usuario.setId_user(response.body().getData().get(0).getUid());
                        usuario.setName(response.body().getData().get(0).getNombre());
                        usuario.setEmail(response.body().getData().get(0).getEmail());
                        usuario.setCelular(response.body().getData().get(0).getTelefono());
                        usuario.setEmail(response.body().getData().get(0).getEmail());
                        usuario.setCiudad(response.body().getData().get(0).getCiudad());
                        usuario.setDireccion(response.body().getData().get(0).getDireccion());
                        usuario.setFoto(response.body().getData().get(0).getFoto());
                        SharedPreferenceManager.setInfoUsuario(getApplicationContext(), usuario);
                        SharedPreferenceManager.setLoged(IniciarSesion.this, true);

                        Intent goHome = new Intent(IniciarSesion.this, Home.class);
                        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goHome);
                        finish();
                        break;
                    case "102": //contraseña o email incorrectos
                        Toast.makeText(IniciarSesion.this, "Contraseña o email incorrectos", Toast.LENGTH_LONG).show();
                        break;
                    case "120": //auth_token no valido
                        break;
                    default: break;
                }
            }
            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                btnContinuar.setEnabled(true);
                if (dialogProgress.isShowing()) {
                    dialogProgress.dismiss();
                }
                Log.e(TAG, "onFailure login");
            }
        });
    } //login
}
