package co.kubo.indiesco.activities

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.appcompat.app.AppCompatActivity
import co.kubo.indiesco.modelo.Usuario
import co.kubo.indiesco.modelo.ValidacionDirecciones
import co.kubo.indiesco.restAPI.modelo.ResponseTipoDirecciones
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
            setAddressData()
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

    private fun getRoadTypes() {
        val authToken = SharedPreferenceManager.getAuthToken(applicationContext)
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        val request: Call<ResponseTipoDirecciones> = endpoints.obtenerTypoDeDireccicones(authToken)
        request.enqueue(object : Callback<ResponseTipoDirecciones> {
            override fun onResponse(call: Call<ResponseTipoDirecciones>, response: Response<ResponseTipoDirecciones>) {
                val body = response.body()
                if (body!!.code == "100") {

                }
            }

            override fun onFailure(call: Call<ResponseTipoDirecciones>, t: Throwable) {
                Log.e("ERROR-TIPOS-DIRECCIONES", t.message)
            }
        })
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
                                Toast.makeText(this@ActivityNuevaDireccionSinGps, "Por favor ingresa una direcci??n",
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
        var responseRecargo2Call: Call<ResponseValidacion> = endpoints.createAddress(authToken, usuario.id_user, spTypes.selectedItem.toString() + " " + etFirstPart.text.toString().replace(" ", "") + " # " + etSecondPart.text.toString().replace(" ", "") + " - " + etThirdPart.text.toString().replace(" ", "") + " ", etComplement.text.toString(), cityId)
        responseRecargo2Call.enqueue(object : Callback<ResponseValidacion> {
            override fun onFailure(call: Call<ResponseValidacion>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<ResponseValidacion>?, response: Response<ResponseValidacion>?) {
                if (response!!.isSuccessful) {
                    when (response.body()!!.code) {
                        "100" -> {
                            Toast.makeText(this@ActivityNuevaDireccionSinGps, "Direcci??n creada",
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
        var responseRecargo2Call: Call<ResponseValidacion> = endpoints.updateAddress(authToken, singleton.voDireccion.id_direccion, spTypes.getSelectedItem().toString() + " " + etFirstPart.text.toString() + " # " + etSecondPart.text.toString() + " - " + etThirdPart.text.toString() + " ", etComplement.text.toString(), cityId)
        responseRecargo2Call.enqueue(object : Callback<ResponseValidacion> {
            override fun onFailure(call: Call<ResponseValidacion>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<ResponseValidacion>?, response: Response<ResponseValidacion>?) {
                if (response!!.isSuccessful) {
                    Toast.makeText(this@ActivityNuevaDireccionSinGps, "Direcci??n actualizada",
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

    private fun setAddressData() {
        val data: List<String> = singleton.voDireccion.direccion.split(" ")
        etFirstPart.setText(data[1])
        etSecondPart.setText(data[3])
        etThirdPart.setText(data[5])
        spTypes.setSelection(getPositionTypeByName(data[0]))

    }

    private fun getPositionTypeByName(name: String): Int {
        var result = -1
        for (i in str!!.indices) {
            if (name == str!![i]) {
                result = i
                break
            }
        }
        return result
    }
}
