package co.kubo.indiesco.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import co.kubo.indiesco.R
import co.kubo.indiesco.dialog.DialogProgress
import co.kubo.indiesco.modelo.Usuario
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral
import co.kubo.indiesco.utils.SharedPreferenceManager

import kotlinx.android.synthetic.main.activity_excedentes3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class Excedentes3 : AppCompatActivity(), View.OnClickListener {

    val df = DecimalFormat("$###,###.##")
    var precioM2 = 0.0
    var total = 0.0
    var nMetros = 0
    lateinit var dialogProgress : DialogProgress

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
            R.id.imgPlus -> {
                try {
                    nMetros = tvQty.text.toString().toInt()
                }catch (e: Exception){
                    nMetros = 0
                }
                ++nMetros
                tvQty.text = nMetros.toString()
                calculate()
            }
            R.id.imgMinus -> {
                try {
                    nMetros = tvQty.text.toString().toInt()
                }catch (e: Exception){
                    nMetros = 0
                }
                if (nMetros > 0){
                    --nMetros
                    tvQty.text = nMetros.toString()
                    calculate()
                }
            }
            R.id.tvPayment -> {
                if (nMetros == 0){
                    Toast.makeText(applicationContext, "Debe seleccionar mínimo un metro cuadrado", Toast.LENGTH_LONG).show()
                } else {
                    crearRecargo()
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun calculate(){
        try {
            nMetros = tvQty.text.toString().toInt()
        }catch (e: Exception){
            nMetros = 0
        }
        total = nMetros * precioM2
        tvValor.text = "Total: ${df.format(total)}"
    }

    fun setListeners(){
        imgBotonVolver.setOnClickListener(this)
        imgMinus.setOnClickListener(this)
        imgPlus.setOnClickListener(this)
        tvPayment.setOnClickListener(this)
    }

    fun crearRecargo(){
        dialogProgress = DialogProgress(this)
        dialogProgress.show()
        var authToken = SharedPreferenceManager.getAuthToken(this)
        var restApiAdapter = RestApiAdapter()
        var endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        var usuario = Usuario()
        usuario = SharedPreferenceManager.getInfoUsuario(this)
        var id = usuario.id_user
        var responseGeneral : Call<ResponseGeneral> = endpoints.createRecharge(authToken,
                usuario.id_user, nMetros, total.toInt())
        responseGeneral.enqueue(object : Callback<ResponseGeneral>{
            override fun onFailure(call: Call<ResponseGeneral>?, t: Throwable?) {
                if (dialogProgress.isShowing)
                    dialogProgress.dismiss()
            }

            override fun onResponse(call: Call<ResponseGeneral>?, response: Response<ResponseGeneral>?) {
                if (dialogProgress.isShowing)
                    dialogProgress.dismiss()
                if(response!!.isSuccessful){
                    when (response.body()!!.code){
                        "100" -> {
                            Toast.makeText(applicationContext, "Recargo creado con éxito", Toast.LENGTH_LONG).show()
                            val intent = Intent(applicationContext, Excedentes :: class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excedentes3)
        setListeners()
        val param = intent.extras
        precioM2 = param.getDouble("valorMetro2", 0.0)
        calculate()
    }
}
