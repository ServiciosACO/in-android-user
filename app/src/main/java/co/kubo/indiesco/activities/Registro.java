package co.kubo.indiesco.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.kubo.indiesco.R;
import co.kubo.indiesco.utils.Utils;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.imgFoto)
    ImageView imgFoto;
    @BindView(R.id.editNombre)
    EditText editNombre;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editCelular)
    EditText editCelular;
    @BindView(R.id.editDireccion)
    EditText editDireccion;
    @BindView(R.id.editCiudad)
    EditText editCiudad;
    @BindView(R.id.editpass1)
    TextInputEditText editpass1;
    @BindView(R.id.editpass2)
    TextInputEditText editpass2;
    @BindView(R.id.fabSiguiente)
    FloatingActionButton fabSiguiente;
    @BindView(R.id.scrollViewRegistro)
    ScrollView scrollViewRegistro;
    @BindView(R.id.inputPass1)
    TextInputLayout inputPass1;
    @BindView(R.id.inputPass2)
    TextInputLayout inputPass2;

    private int scrollY, oldScrollY = 0;
    Utils utils = new Utils();
    Animation animShake;
    boolean bandNombre = false, bandEmail = false, bandCel = false, bandDir = false, bandCiudad = false, bandPass1 = false, bandPass2 = false, bandOK = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);
        animShake = AnimationUtils.loadAnimation(Registro.this, R.anim.shake);
        fabSiguiente.setOnClickListener(this);
        /**Para desaparecer el FAB cuando hago scroll*/
        scrollViewRegistro.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                scrollY = scrollViewRegistro.getScrollY();
                Log.e("scroll", scrollY + " " + oldScrollY);
                if (scrollY > oldScrollY){
                    fabSiguiente.hide();
                    oldScrollY = scrollY;
                    if (scrollViewRegistro.getChildAt(0).getBottom() <= (scrollViewRegistro.getHeight() + scrollViewRegistro.getScrollY()) || scrollY <= 0)
                        fabSiguiente.show();
                }else{
                    fabSiguiente.show();
                    oldScrollY = scrollY;
                }
            }
        });

        setlistenerEditText();
    }

    private void setlistenerEditText(){
        editNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandNombre = true;
                }else{
                    bandNombre = false;
                }
                validarFABVerde();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandEmail = true;
                }else{
                    bandEmail = false;
                }
                validarFABVerde();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editCelular.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandCel = true;
                }else{
                    bandCel = false;
                }
                validarFABVerde();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandDir = true;
                }else{
                    bandDir = false;
                }
                validarFABVerde();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editCiudad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() != 0) {
                    bandCiudad = true;
                }else{
                    bandCiudad = false;
                }
                validarFABVerde();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
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
                validarFABVerde();
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
                validarFABVerde();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void validarFABVerde(){
        if (bandNombre&&bandEmail&&bandCel&&bandDir&&bandCiudad&&bandPass1&&bandPass2){
            fabSiguiente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorVerde)));
            bandOK = true;
        }else{
            fabSiguiente.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_hint)));
            bandOK = false;
        }
    }//validarFABVerde

    private Boolean validacion() {
        if (!Utils.checkInternetConnection(this, true)){
            return false;
        }
        if (editNombre.getText().toString().trim().equalsIgnoreCase("")) {
            editNombre.setError("El nombre es requerido");
            return false;
        }
        if (editEmail.getText().toString().trim().equalsIgnoreCase("")) {
            editEmail.setError("El correo electrónico es requerido");
            return false;
        }
        if (!utils.isEmailValid(editEmail.getText().toString())){
            editEmail.setError("Debe ingresar un correo electrónico valido");
            return false;
        }
        if (editCelular.getText().toString().trim().equalsIgnoreCase("")) {
            editCelular.setError("El celular es requerido");
            return false;
        }
        if (editDireccion.getText().toString().trim().equalsIgnoreCase("")) {
            editDireccion.setError("La dirección es requerida");
            return false;
        }
        if (editCiudad.getText().toString().trim().equalsIgnoreCase("")) {
            editCiudad.setError("La ciudad es requerida");
            return false;
        }
        if (editpass1.getText().toString().trim().equalsIgnoreCase("")) {
            editpass1.setError("La contraseña es requerida");
            return false;
        }
        if (editpass2.getText().toString().trim().equalsIgnoreCase("")) {
            editpass2.setError("La contraseña es requerida");
            return false;
        }
        if (!editpass1.getText().toString().equals(editpass2.getText().toString())) {
            editpass2.setError("Las contraseñas no coinciden");
            inputPass1.startAnimation(animShake);
            inputPass2.startAnimation(animShake);
            return false;
        }
        return true;
    }//validacion

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabSiguiente:
                if (validacion() && bandOK){
                    Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
