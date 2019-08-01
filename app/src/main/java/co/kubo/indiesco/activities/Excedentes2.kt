package co.kubo.indiesco.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.AdapterInmuebles
import co.kubo.indiesco.adaptadores.IShowOption
import co.kubo.indiesco.dialog.DialogProgress
import co.kubo.indiesco.modelo.InmuebleVO
import co.kubo.indiesco.modelo.RecargoVO
import co.kubo.indiesco.modelo.Usuario
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter
import co.kubo.indiesco.restAPI.modelo.ResponseInmueble
import co.kubo.indiesco.utils.SharedPreferenceManager
import co.kubo.indiesco.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tipo_inmueble.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class Excedentes2 : AppCompatActivity(), View.OnClickListener, IShowOption {

    val TAG = "Excedentes2"
    val df = DecimalFormat("$###,###")
    lateinit var dialogProgress: DialogProgress
    private lateinit var adapter: AdapterInmuebles
    lateinit var llm : LinearLayoutManager
    var flag = 2
    val utils = Utils()
    var position = 0

    var inmuebles = java.util.ArrayList<InmuebleVO>()

    override fun option(flag: Int, pos : Int) {
        this.flag = flag
        position = pos
        rlNext.setBackgroundColor(resources.getColor(R.color.colorVerde))
    }

    fun validation() : Boolean{
        if (!utils.checkInternetConnection(this@Excedentes2, true)){
            return false
        }
        if (flag == 2){
            Toast.makeText(applicationContext, "Debe elegir un tipo de vivienda", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun listarTiposInmuebles() {
        dialogProgress = DialogProgress(this@Excedentes2)
        dialogProgress.show()
        val authToken = SharedPreferenceManager.getAuthToken(applicationContext)
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        var usuario = Usuario()
        usuario = SharedPreferenceManager.getInfoUsuario(applicationContext)
        val responseInmuebleCall = endpoints.listarInmuebles(authToken)
        responseInmuebleCall.enqueue(object : Callback<ResponseInmueble> {
            override fun onResponse(call: Call<ResponseInmueble>, response: Response<ResponseInmueble>) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
                val code = response.body()!!.code
                when (code) {
                    "100" -> {
                        llNoInmueble.visibility = View.GONE
                        rvInmueble.visibility = View.VISIBLE
                        inmuebles = response.body()!!.data
                        adapter = AdapterInmuebles(inmuebles, this@Excedentes2, this@Excedentes2)
                        rvInmueble.adapter = adapter
                    }
                    "102" ->{
                        Log.e(TAG, "Cod: 102 No hay datos")
                        llNoInmueble.visibility = View.VISIBLE
                        rvInmueble.visibility = View.GONE
                    }
                    "120" -> Log.e(TAG, "Cod: 120 auth token invalido")
                }//switch
            }

            override fun onFailure(call: Call<ResponseInmueble>, t: Throwable) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
                Log.e(TAG, "listarTiposInmuebles, onFailure")
            }
        })
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.rlNext -> {
                if (validation()){
                    val intent = Intent (this, Excedentes3 :: class.java)
                    intent.putExtra("valorMetro2", inmuebles[position].valor_mt2.toDouble())
                    startActivity(intent)
                }
            }
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
        }
    }

    private fun setListeners() {
        rlNext.setOnClickListener(this)
        imgBotonVolver.setOnClickListener(this)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipo_inmueble)
        setListeners()

        llProgress.visibility = View.INVISIBLE
        llm = LinearLayoutManager(this)
        rvInmueble!!.layoutManager = llm
        listarTiposInmuebles()
    }
}
