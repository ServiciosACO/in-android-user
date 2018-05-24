package co.kubo.indiesco.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import co.kubo.indiesco.R
import co.kubo.indiesco.dialog.DialogProgress
import co.kubo.indiesco.modelo.Usuario
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral
import co.kubo.indiesco.utils.SharedPreferenceManager
import co.kubo.indiesco.utils.Utils
import kotlinx.android.synthetic.main.activity_nueva_direccion.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NuevaDireccion : AppCompatActivity(), View.OnClickListener {

    val TAG = "MisDirecciones"
    lateinit var dialogProgress : DialogProgress
    val utils = Utils()
    var direccion = ""
    var latitudStr = ""
    var longitudStr = ""
    var complemento = ""
    var ciudad = ""

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
            R.id.btnAgregarDir -> {
                if (validation()){
                    direccion = editDireccion.text.trim().toString()
                    if (editDirComplemento.text.toString().isEmpty()){
                        complemento = "-"
                    }else{
                        complemento = editDirComplemento.text.toString()
                    }
                    //lat
                    //lng
                    //ciudad
                    agregarDireccion()
                }
            }
        }
    }

    fun validation() : Boolean{
        if (!utils.checkInternetConnection(this@NuevaDireccion, true)){
            return false
        }
        if (editDireccion.text.trim().toString() == ""){
            editDireccion.error = "Debe ingresar una dirección válida"
            return false
        }
        return true
    }

    fun agregarDireccion() {
        if (dialogProgress == null) {
            dialogProgress = DialogProgress(this@NuevaDireccion)
            dialogProgress.show()
        } else {
            dialogProgress.show()
        }
        val authToken = SharedPreferenceManager.getAuthToken(applicationContext)
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        var usuario = Usuario()
        usuario = SharedPreferenceManager.getInfoUsuario(applicationContext)
        val responseGeneralCall = endpoints.agregarDireccion(authToken, usuario.id_user, direccion,
                latitudStr, longitudStr, complemento, ciudad)
        responseGeneralCall.enqueue(object : Callback<ResponseGeneral> {
            override fun onResponse(call: Call<ResponseGeneral>, response: Response<ResponseGeneral>) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
                val code = response.body()!!.code
                when (code) {
                    "100" -> { //OK
                        Toast.makeText(applicationContext, "Se agregó la dirección con éxito", Toast.LENGTH_LONG).show()
                        //Para refrescar la pantalla con la nueva data
                        val intent = Intent(this@NuevaDireccion, MisDirecciones::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    }
                    "102" -> { //Fallo
                        Toast.makeText(applicationContext, "Algo fallo intente de nuevo", Toast.LENGTH_LONG).show()
                    }
                    "120" -> { //auth_token no valido
                    }
                    else -> {
                    }
                }//switch
            }

            override fun onFailure(call: Call<ResponseGeneral>, t: Throwable) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
                Log.e(TAG, "onFailure agregarDir")
            }
        })
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun setListeners() {
        imgBotonVolver.setOnClickListener(this)
        btnAgregarDir.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_direccion)
        dialogProgress = DialogProgress(this@NuevaDireccion)
        setListeners()
    }
}
