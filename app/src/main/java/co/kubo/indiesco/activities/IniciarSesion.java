package co.kubo.indiesco.activities;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.utils.Utils;

public class IniciarSesion extends AppCompatActivity implements View.OnClickListener {

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
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                break;

        }//switch
    }//onClick

    private void animacion(){
        if (editEmail.getVisibility() == View.VISIBLE){
            editEmail.setVisibility(View.GONE);
            inputPassword.setVisibility(View.VISIBLE);
            inputPassword.startAnimation(anim1);
            hideSoftKeyboard();
        }else{
            //consulta el servicio si la contraseña esta bien y se va a home
            //validar si tiene servicios por calificar(Abre servicios) si no home
            Intent goHome = new Intent(this, Home.class);
            goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goHome);
            finish();
            /*inputPassword.setVisibility(View.GONE);
            editEmail.setVisibility(View.VISIBLE);
            editEmail.startAnimation(anim2);*/
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
            if (!Utils.checkInternetConnection(this, true)){
                return false;
            }
            return true;
        }else{
            if (editContraseña.getText().toString().trim().equalsIgnoreCase("")) {
                editContraseña.setError("La contraseña es requerido");
                return false;
            }
            //return (!Utils.checkInternetConnection(this, true));
            if (!Utils.checkInternetConnection(this, true)){
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
}
