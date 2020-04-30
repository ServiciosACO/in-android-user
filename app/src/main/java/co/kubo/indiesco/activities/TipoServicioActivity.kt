package co.kubo.indiesco.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.TiposServiciosAdapter
import co.kubo.indiesco.dialog.DialogProgress
import co.kubo.indiesco.modelo.Usuario
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter
import co.kubo.indiesco.restAPI.modelo.ResponseTipoServicio
import co.kubo.indiesco.utils.SharedPreferenceManager
import co.kubo.indiesco.utils.Singleton
import co.kubo.indiesco.utils.Utils
import kotlinx.android.synthetic.main.activity_tipo_servicio.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TipoServicioActivity : AppCompatActivity(), TiposServiciosAdapter.OnCheckClickListener, View.OnClickListener {

    private var flag = false
    private val singleton = Singleton.getInstance()
    private val utils = Utils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tipo_servicio)

        rlNext.setOnClickListener(this)
        imgBotonVolver.setOnClickListener(this)

        rvTipoServicio.layoutManager = GridLayoutManager(this, 2)

        obtenerTiposServicio()
    }

    private fun obtenerTiposServicio() {
        val dialogProgress = DialogProgress(this)
        dialogProgress.show()
        val authToken = SharedPreferenceManager.getAuthToken(applicationContext)
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        val responseTipoServiciosCall = endpoints.obtenerTiposDeServicion(authToken)
        responseTipoServiciosCall.enqueue(object : Callback<ResponseTipoServicio> {
            override fun onFailure(call: Call<ResponseTipoServicio>, t: Throwable) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
                llNoTipoServicio.visibility = View.VISIBLE
                rvTipoServicio.visibility = View.GONE
            }

            override fun onResponse(call: Call<ResponseTipoServicio>, response: Response<ResponseTipoServicio>) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
                val responseTipoServicio = response.body()!!
                if (response.isSuccessful) {
                    if (responseTipoServicio.code == "100") {
                        llNoTipoServicio.visibility = View.GONE
                        rvTipoServicio.visibility = View.VISIBLE
                        rvTipoServicio.adapter = TiposServiciosAdapter(responseTipoServicio.data, this@TipoServicioActivity, this@TipoServicioActivity)
                    } else {
                        llNoTipoServicio.visibility = View.VISIBLE
                        rvTipoServicio.visibility = View.GONE
                    }
                } else {
                    llNoTipoServicio.visibility = View.VISIBLE
                    rvTipoServicio.visibility = View.GONE
                }
            }

        })
    }

    override fun onCheck(tipoServicio: ResponseTipoServicio.TipoServicio) {
        this.flag = tipoServicio.isSeleccionado
        if (flag) {
            singleton.idTipoServicio = tipoServicio.idServicio
            rlNext.setBackgroundColor(ContextCompat.getColor(this, R.color.colorVerde_80))
            llProgress.setBackgroundColor(ContextCompat.getColor(this, R.color.colorVerde))
        } else {
            singleton.idTipoServicio = -1
            rlNext.setBackgroundColor(ContextCompat.getColor(this, R.color.color_hint_80))
            llProgress.setBackgroundColor(ContextCompat.getColor(this, R.color.color_hint))
        }
    }

    private fun validation(): Boolean {
        if (!utils.checkInternetConnection(this, true)) {
            return false
        }
        if (!flag) {
            Toast.makeText(applicationContext, getString(R.string.lab_seleccionar_tipo_servicio), Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlNext -> {
                if (validation()) {
                    if (flag) {
                        val intent = Intent(this, TipoInmueble::class.java)
                        startActivity(intent)
                    }
                }
            }
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
        }
    }
}