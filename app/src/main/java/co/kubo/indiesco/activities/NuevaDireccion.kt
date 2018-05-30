package co.kubo.indiesco.activities

import android.content.Intent
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.AdapterAutocomplete
import co.kubo.indiesco.adaptadores.AdapterAutocomplete2
import co.kubo.indiesco.dialog.DialogProgress
import co.kubo.indiesco.modelo.Usuario
import co.kubo.indiesco.modelo.direccionesGoogleVO
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral
import co.kubo.indiesco.utils.SharedPreferenceManager
import co.kubo.indiesco.utils.Utils
import kotlinx.android.synthetic.main.activity_nueva_direccion.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.gson.JsonElement
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class NuevaDireccion : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {

    val TAG = "MisDirecciones"
    lateinit var dialogProgress : DialogProgress
    val utils = Utils()

    var latitudStr = ""
    var longitudStr = ""
    var complemento = ""
    var ciudad = ""
    /**For autocomplete map*/
    private var googleMap : GoogleMap ?= null
    var latitudDireccion : Double ?= 0.0
    var longitudDireccion : Double ?= 0.0
    var cargarDireccion = false
    var bandDirCorrecto = false
    var valorDireccion = ""
    var ciudadDireccion = "Bogota"


    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
            R.id.btnAgregarDir -> {
                if (validation()){
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
        dialogProgress = DialogProgress(this@NuevaDireccion)
        dialogProgress.show()

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

    /**Autocomplete map*/
    private fun obtenerDireccion(lat: String, log: String, band: Boolean) {
        dialogProgress = DialogProgress(this)
        dialogProgress.show()
        val latlon = "$lat,$log"
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        val ubicacion : Call<JsonElement> = endpoints.obtenerDireccion(latlon, "true")
        ubicacion.enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
                valorDireccion("", band)
            }
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
                try {
                    if (response?.body() != null) {
                        var catObj = JSONObject(response.body().toString())
                        var resultado = catObj.getJSONArray("results")
                        var catObj1 = resultado.getJSONObject(0)
                        //var direccion = String(catObj1.getString("formatted_address").getBytes("ISO-8859-1"), "UTF-8")
                        var direccion = catObj1.getString("formatted_address")
                        valorDireccion(direccion, band)
                    } else {
                        valorDireccion("", band)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    valorDireccion("", band)
                }
            }
        })
    }

    fun ubicacionDireccion(placeId: String, key: String, band: Boolean){
        dialogProgress = DialogProgress(this)
        dialogProgress.show()
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        val ubicacion : Call<JsonElement> = endpoints.ubicacionDireccion(placeId, "true", key)
        ubicacion.enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
                Toast.makeText(applicationContext, "Ha ocurrido un error en la ubicación", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
                try {
                    var catObj = JSONObject(response!!.body().toString())
                    var resultado = catObj.getJSONObject("result")
                    var catLot = resultado.getJSONObject("geometry")
                    catLot = catLot.getJSONObject("location")
                    var lon = catLot.getString("lng")
                    var lat = catLot.getString("lat")
                    if (lon != "" && lat != "") {
                        guardarCoordenadas(lat, lon, band)
                    } else {
                        Toast.makeText(applicationContext, "Ha ocurrido un error en la ubicación", Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(applicationContext, "Ha ocurrido un error en la ubicación", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun direccionPalabra(lat: String, lon: String, dir: String, key: String){
        var latlon = "$lat,$lon"
        var direccion = dir.replace(" - ", "-")
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        val ubicacion : Call<JsonElement> = endpoints.direccionPalabra(direccion, "true", key,latlon,
                "1000","country:CO", "ES")
        ubicacion.enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }

            }
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
                try {
                    //vacio()
                    var catObj = JSONObject(response!!.body().toString())
                    var array = catObj.getJSONArray("predictions")
                    //llenarAutocomplete(parseo(array))
                    llenarAutocomplete2(parseo(array))
                } catch (e: JSONException) {
                    e.printStackTrace()
                    //vacio()
                }

            }
        })
    }

    fun parseo(array: JSONArray) : ArrayList<String>  {
        var lista = ArrayList<String>()
        var lista2 = ArrayList<direccionesGoogleVO>()

        for(i in 0 until array.length()){
            try {
                var obj = array.getJSONObject(i)
                var dato = direccionesGoogleVO()
                var datosCiudad = obj.getString("description").split(",")
                if (datosCiudad.size >= 2) {
                    dato.description = datosCiudad[0].trim()
                    dato.ciudad = datosCiudad[1].trim()
                } else {
                    dato.description = datosCiudad[0].trim()
                    dato.ciudad = " "
                }
                dato.placeId = obj.getString("place_id")
                lista2.add(dato)
                lista.add(obj.getString("place_id"))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
            return lista
    }


    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
    }

    fun guardarCoordenadas(lat: String, log: String, bandDir: Boolean) {
        latitudDireccion = lat.toDouble()
        longitudDireccion = log.toDouble()
        //listaDirecciones.setVisibility(View.GONE)
        if (googleMap != null) {
            googleMap!!.clear()
            googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitudDireccion!!, longitudDireccion!!), 16f))
        }
        /*if (bandSubir) {
            bandSubir = false
            animacionVista(false, 200)
        }*/
        obtenerDireccion(lat, log, bandDir)
    }

    fun valorDireccion(direccion: String ,bandPonerDir:  Boolean ) {
        cargarDireccion = true
        bandDirCorrecto = true
        try {
            var data = direccion.split(",")
            if (data != null) {
                if (data.isNotEmpty() && bandPonerDir) {
                    if (data[0].contains(" a ")) {
                        var direc = data[0].split(" a ")
                        editDireccion.setText(direc[0])
                        valorDireccion = direc[0]
                    } else {
                        editDireccion.setText(data[0])
                        valorDireccion = data[0]
                    }
                }
            }
        } catch (e: Exception) {
        }
        var geocoder = Geocoder(this, Locale.getDefault())
        /*try {
            //List<Address> addresses = geocoder.getFromLocation(latitudDireccion, longitudDireccion, 1);
            if (addresses.size() != 0) {
                ciudadDireccion = addresses.get(0).getLocality()
                if (ciudadDireccion == null) {
                    ciudadDireccion = "Bogota"
                }
            } else {
                ciudadDireccion = "Bogota"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ciudadDireccion = "Bogota"
        }*/
    }

    fun llenarAutocomplete(lista: ArrayList<Array<String>>) {
        //cargarDireccionesGoogle = true
        val adapter = AdapterAutocomplete(this, R.layout.item_direccion_google, R.id.txtItemDireccionGoogle, lista)
        editDireccion.setAdapter(adapter)
    }
    fun llenarAutocomplete2(lista: ArrayList<String>) {
        //cargarDireccionesGoogle = true
        val adapter = AdapterAutocomplete2(this, R.layout.item_direccion_google, R.id.txtItemDireccionGoogle, lista)
        editDireccion.setAdapter(adapter)
    }

}

