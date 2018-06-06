package co.kubo.indiesco.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import co.kubo.indiesco.R
import co.kubo.indiesco.dialog.DialogProgress
import co.kubo.indiesco.modelo.Usuario
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter
import co.kubo.indiesco.restAPI.modelo.ResponseCodigoDescuento
import co.kubo.indiesco.utils.SharedPreferenceManager
import co.kubo.indiesco.utils.Utils
import kotlinx.android.synthetic.main.activity_solicitud_servicio3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SolicitudServicio3 : AppCompatActivity(), View.OnClickListener {

    lateinit var dialogProgress : DialogProgress
    val sharedPreferenceManager = SharedPreferenceManager()
    val utils = Utils()
    var codigo = ""

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
            R.id.tvAddService -> {
                val intent = Intent (this, FechaServicio :: class.java)
                startActivity(intent)
            }
            R.id.tvValidateCoupon -> {
                if (validation()){
                    var code = editCode.text.toString()
                    validateCoupon(code)
                }
            }
            R.id.tvPayment -> {
                
            }
        }
    }

    fun validation(): Boolean{
        if (!utils.checkInternetConnection(this@SolicitudServicio3,true)){
            return false
        }
        if (editCode.text.isEmpty()){
            editCode.error = "Debe ingresar un código valido"
            return false
        }
        return true
    }

    fun validateCoupon(code: String) {
        dialogProgress = DialogProgress(this)
        dialogProgress.show()
        var authToken = SharedPreferenceManager.getAuthToken(this)
        var restApiAdapter = RestApiAdapter()
        var endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        var usuario = Usuario()
        usuario = SharedPreferenceManager.getInfoUsuario(this)
        var id = usuario.id_user
        var responseCodigoDescuentoCall : Call<ResponseCodigoDescuento> = endpoints.validarCupon(authToken, id, code)
        responseCodigoDescuentoCall.enqueue(object : Callback<ResponseCodigoDescuento>{
            override fun onFailure(call: Call<ResponseCodigoDescuento>?, t: Throwable?) {
                if (dialogProgress.isShowing)
                    dialogProgress.dismiss()
            }
            override fun onResponse(call: Call<ResponseCodigoDescuento>?, response: Response<ResponseCodigoDescuento>?) {
                if (dialogProgress.isShowing)
                    dialogProgress.dismiss()
                if (response!!.isSuccessful){
                    when (response.body()!!.code){
                        "100" -> {
                            editCode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_selected,0)
                            when (response.body()!!.data!![0].tipo_codigo){
                                1 -> { //valor
                                    tvDiscount.visibility = View.VISIBLE
                                    tvDiscount.text = (response.body()!!.data!![0].valor_codigo!! * -1).toString()
                                }
                                2 -> { //porcentaje
                                    var total = tvPayment.text.toString().toDouble()
                                    tvDiscount.visibility = View.VISIBLE
                                    tvDiscount.text = ((response.body()!!.data!![0].valor_codigo!!*total) * -1).toString()
                                }
                            }
                        }
                        "101" -> {
                            editCode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_selected,0)
                            Toast.makeText(applicationContext, "El código ingresado ya fue redimido", Toast.LENGTH_LONG).show()
                        }
                        "102" -> {
                            editCode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_selected,0)
                            Toast.makeText(applicationContext, "El código ingresado no es válido", Toast.LENGTH_LONG).show()
                        }
                        "103" -> {
                            editCode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_selected,0)
                            Toast.makeText(applicationContext, "El código ingresado expiró", Toast.LENGTH_LONG).show()
                        }
                        "104" -> {
                            editCode.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_selected,0)
                            Toast.makeText(applicationContext, "El código ingresado no es válido", Toast.LENGTH_LONG).show()
                        }
                        "120" -> {
                            //auth token no valido
                        }
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    fun setListeners() {
        imgBotonVolver.setOnClickListener(this)
        tvAddService.setOnClickListener(this)
        tvValidateCoupon.setOnClickListener(this)
        tvPayment.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitud_servicio3)
        setListeners()
    }
}
