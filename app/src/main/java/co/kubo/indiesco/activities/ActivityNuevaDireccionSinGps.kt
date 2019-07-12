package co.kubo.indiesco.activities

import android.animation.ObjectAnimator
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import co.kubo.indiesco.R
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter
import co.kubo.indiesco.restAPI.modelo.ResponseValidacion
import co.kubo.indiesco.utils.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.ArrayAdapter
import co.kubo.indiesco.modelo.Usuario
import co.kubo.indiesco.modelo.ValidacionDirecciones
import co.kubo.indiesco.utils.Singleton
import kotlinx.android.synthetic.main.activity_nueva_direccion_sin_gps.*


class ActivityNuevaDireccionSinGps : AppCompatActivity(), View.OnClickListener {

    var arrayCities = ArrayList<String>()
    val str = arrayOf("Autopista", "Avenida", "Bulevar", "Calle", "Carrera", "Carretera", "Circular", "Circunvalar", "Diagonal", "Pasaje", "Paseo", "Peatonal", "Transversal", "Troncal", "Variante", "Via")
    var ciudadesList = ArrayList<ValidacionDirecciones>()
    var estadoStr: String = ""
    var singleton = Singleton.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_direccion_sin_gps)
        validacionDirec()

        val extras = intent.extras

        if (extras != null) {
            estadoStr = extras.getString("estado")
        }

        val adapterType = ArrayAdapter<String>(this@ActivityNuevaDireccionSinGps, android.R.layout.simple_spinner_dropdown_item, str)
        spTypes.adapter = adapterType


        if (estadoStr == "Editar") {
            etEditAdrress.visibility = View.VISIBLE
            lnCrearDireccion.visibility = View.GONE
            etEditAdrress.setText(singleton.voDireccion.direccion)
            etComplement.setText(singleton.voDireccion.complemento)
            btnAgregarDirND.text = getString(R.string.title_editar_direccion)
            tvTituloHeaderND.text = getString(R.string.title_editar_direccion)
        } else {
            tvTituloHeaderND.text = getString(R.string.title_nueva_direccion)
        }

        btnAgregarDirND.setOnClickListener(this)
        btnCancelarDirND.setOnClickListener(this)
        imgBotonVolverND.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnAgregarDirND -> {
                for (i in ciudadesList.indices) {
                    if ("${ciudadesList[i].city} (${ciudadesList[i].region})" == spnCiudades.getSelectedItem().toString()) {
                        if (estadoStr == "Editar") {
                            if (etEditAdrress.text.toString() != "") {
                                editAddress(ciudadesList[i].cityId)
                            } else {
                                Toast.makeText(this@ActivityNuevaDireccionSinGps, "Por favor ingresa una dirección",
                                        Toast.LENGTH_LONG).show()
                            }

                        } else {
                            if (etFirstPart.text.toString() != "" && etSecondPart.text.toString() != "" && etThirdPart.text.toString() != "") {
                                createAddress(ciudadesList[i].cityId)
                            } else {
                                Toast.makeText(this@ActivityNuevaDireccionSinGps, "Por favor ingresa todos los datos",
                                        Toast.LENGTH_LONG).show()
                            }

                        }
                        break
                    }
                }
            }
            R.id.btnCancelarDirND -> {
                finish()
            }
            R.id.imgBotonVolverND -> {
                finish()
            }
        }
    }

    private fun validacionDirec() {

        val authToken = SharedPreferenceManager.getAuthToken(applicationContext)
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()

        var responseRecargo2Call: Call<ResponseValidacion> = endpoints.validarDirec(authToken)
        responseRecargo2Call.enqueue(object : Callback<ResponseValidacion> {
            override fun onFailure(call: Call<ResponseValidacion>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<ResponseValidacion>?, response: Response<ResponseValidacion>?) {
                if (response!!.isSuccessful) {
                    when (response.body()!!.code) {
                        "100" -> {
                            ciudadesList = response.body()!!.data
                            for (i in ciudadesList.indices) {
                                arrayCities.add("${ciudadesList[i].city} (${ciudadesList[i].region})")
                            }
                            val adapter = ArrayAdapter<String>(this@ActivityNuevaDireccionSinGps, android.R.layout.simple_spinner_dropdown_item, arrayCities)
                            spnCiudades.adapter = adapter
                            if (estadoStr == "Editar") {
                                if (getPositionCurrentCity() != -1) {
                                    spnCiudades.setSelection(getPositionCurrentCity())
                                }
                            }
                        }
                    }
                }
            }
        })
    }


    private fun createAddress(cityId: String) {

        val authToken = SharedPreferenceManager.getAuthToken(applicationContext)
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        var usuario = Usuario()
        usuario = SharedPreferenceManager.getInfoUsuario(applicationContext)
        var responseRecargo2Call: Call<ResponseValidacion> = endpoints.createAddress(authToken, usuario.id_user, spTypes.getSelectedItem().toString() + " " + etFirstPart.text.toString() + " " + etSecondPart.text.toString() + " " + etThirdPart.text.toString() + " ", etComplement.text.toString(), cityId)
        responseRecargo2Call.enqueue(object : Callback<ResponseValidacion> {
            override fun onFailure(call: Call<ResponseValidacion>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<ResponseValidacion>?, response: Response<ResponseValidacion>?) {
                if (response!!.isSuccessful) {
                    when (response.body()!!.code) {
                        "100" -> {
                            Toast.makeText(this@ActivityNuevaDireccionSinGps, "Dirección creada",
                                    Toast.LENGTH_LONG).show()
                            finish()

                        }
                    }
                }

            }
        })
    }

    private fun editAddress(cityId: String) {

        val authToken = SharedPreferenceManager.getAuthToken(applicationContext)
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        var usuario = Usuario()
        usuario = SharedPreferenceManager.getInfoUsuario(applicationContext)
        var responseRecargo2Call: Call<ResponseValidacion> = endpoints.updateAddress(authToken, singleton.voDireccion.id_direccion, etEditAdrress.text.toString(), etComplement.text.toString(), cityId)
        responseRecargo2Call.enqueue(object : Callback<ResponseValidacion> {
            override fun onFailure(call: Call<ResponseValidacion>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<ResponseValidacion>?, response: Response<ResponseValidacion>?) {
                if (response!!.isSuccessful) {
                    Toast.makeText(this@ActivityNuevaDireccionSinGps, "Dirección actualizada",
                            Toast.LENGTH_LONG).show()
                    finish()
                }

            }
        })
    }

    private fun getPositionCurrentCity(): Int {
        var result: Int = -1
        for (i in ciudadesList.indices) {
            if (ciudadesList[i].cityId == singleton.voDireccion.cityId) {
                result = i
                break
            }
        }
        return result
    }
}
