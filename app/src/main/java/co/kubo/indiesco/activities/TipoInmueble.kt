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
import co.kubo.indiesco.modelo.Usuario
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter
import co.kubo.indiesco.restAPI.modelo.ResponseInmueble
import co.kubo.indiesco.utils.SharedPreferenceManager
import co.kubo.indiesco.utils.Singleton
import co.kubo.indiesco.utils.Utils
import kotlinx.android.synthetic.main.activity_tipo_inmueble.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class TipoInmueble : AppCompatActivity(), IShowOption, View.OnClickListener {

    val TAG = "TipoInmueble"
    lateinit var dialogProgress: DialogProgress
    lateinit var llm: LinearLayoutManager
    private lateinit var adapter: AdapterInmuebles
    val utils = Utils()
    private var flag = 2
    val singleton = Singleton.getInstance()
    var inmuebles = ArrayList<InmuebleVO>()

    private fun listarTiposInmuebles() {
        dialogProgress = DialogProgress(this@TipoInmueble)
        dialogProgress.show()
        val authToken = SharedPreferenceManager.getAuthToken(applicationContext)
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        var usuario = Usuario()
        usuario = SharedPreferenceManager.getInfoUsuario(applicationContext)
        val responseInmuebleCall = endpoints.listarInmuebles(authToken)
        responseInmuebleCall.enqueue(object : Callback<ResponseInmueble> {
            override fun onResponse(call: Call<ResponseInmueble>, response: Response<ResponseInmueble>) {

                val code = response.body()!!.code
                when (code) {
                    "100" -> {
                        llNoInmueble.visibility = View.GONE
                        rvInmueble.visibility = View.VISIBLE
                        val inmueblesPorServicio = ArrayList<InmuebleVO>()
                        response.body()!!.data!!.forEach { inmueble ->
                            if (inmueble.idServicio == singleton.idTipoServicio)
                                inmueblesPorServicio.add(inmueble)
                        }
                        //inmuebles = response.body()!!.data
                        inmuebles = inmueblesPorServicio
                        singleton.data = inmuebles
                        adapter = AdapterInmuebles(inmuebles, this@TipoInmueble, this@TipoInmueble)
                        rvInmueble.adapter = adapter
                    }
                    "102" -> {
                        Log.e(TAG, "Cod: 102 No hay datos")
                        llNoInmueble.visibility = View.VISIBLE
                        rvInmueble.visibility = View.GONE
                    }
                    "120" -> Log.e(TAG, "Cod: 120 auth token invalido")
                }//switch
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
            }

            override fun onFailure(call: Call<ResponseInmueble>, t: Throwable) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
                Log.e(TAG, "listarTiposInmuebles, onFailure")
            }
        })
    }

    override fun option(flag: Int, pos: Int) {
        this.flag = flag
        llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
        rlNext.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
    }

    private fun setListeners() {
        rlNext.setOnClickListener(this)
        imgBotonVolver.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlNext -> {
                if (validation()) {
                    if (flag == 0) {
                        val intent = Intent(this, AddService::class.java)
                        intent.putExtra("type", flag)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, AddService2::class.java)
                        intent.putExtra("type", flag)
                        startActivity(intent)
                    }
                }
            }
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    fun validation(): Boolean {
        if (!utils.checkInternetConnection(this@TipoInmueble, true)) {
            return false
        }
        if (flag == 2) {
            Toast.makeText(applicationContext, "Debe elegir un tipo de vivienda", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipo_inmueble)
        setListeners()
        llm = LinearLayoutManager(this)
        rvInmueble.layoutManager = llm
        listarTiposInmuebles()
    }
}
