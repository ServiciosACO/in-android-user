package co.kubo.indiesco.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.checkSelfPermission
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.AdapterAutocomplete
import co.kubo.indiesco.dialog.DialogProgress
import co.kubo.indiesco.modelo.Usuario
import co.kubo.indiesco.modelo.direccionesGoogleVO
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral
import co.kubo.indiesco.utils.SharedPreferenceManager
import co.kubo.indiesco.utils.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_nueva_direccion.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
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
    /**For autocomplete map*/
    private var googleMap : GoogleMap ?= null
    var latitudDireccion : Double ?= 0.0
    var longitudDireccion : Double ?= 0.0
    var cargarDireccion = false
    var bandDirCorrecto = false
    var valorDireccion = ""
    var ciudadDireccion = ""
    var cargoMap = false
    var zoomActual = 0F
    var bandPonerDir = true
    var encontrarUbicacion = 0
    var mapaDireccion : MapFragment ?= null
    var gps_enabled = false
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    var band = false
    var mLocationPermissionsGranted = false
    var network_enabled = false
    var band2 = true
    val DEFAULT_ZOOM = 16F

    var page = 0

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
        val responseGeneralCall = endpoints.agregarDireccion(authToken, usuario.id_user, valorDireccion,
                latitudStr, longitudStr, complemento, ciudadDireccion)
        responseGeneralCall.enqueue(object : Callback<ResponseGeneral> {
            override fun onResponse(call: Call<ResponseGeneral>, response: Response<ResponseGeneral>) {
                if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }
                val code = response.body()!!.code
                when (code) {
                    "100" -> { //OK
                        if (page == 2){
                            Toast.makeText(applicationContext, "Se agregó la dirección con éxito", Toast.LENGTH_LONG).show()
                            onBackPressed()
                            /*
                            //Para refrescar la pantalla con la nueva data
                            val intent = Intent(this@NuevaDireccion, MisDirecciones::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            finish()
                            */
                        } else {
                            Toast.makeText(applicationContext, "Se agregó la dirección con éxito", Toast.LENGTH_LONG).show()
                            //Para refrescar la pantalla con la nueva data
                            val intent = Intent(this@NuevaDireccion, MisDirecciones::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            finish()
                        }
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

        editDireccion.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(valorDireccion: CharSequence?, start: Int, before: Int, count: Int) {
                var valorCambio = 0
                if (before > count){
                    valorCambio = before - count
                } else if (before < count){
                    valorCambio = count - before
                } else {
                    valorCambio = 0
                }
                if (valorCambio < 2){
                    if (valorDireccion.toString().trim().length >= 2){
                        if (utils.checkInternetConnection(this@NuevaDireccion, true)){
                            if (cargarDireccion) {
                                cargarDireccion = false
                                //googleMap!!.uiSettings.isScrollGesturesEnabled = false
                                direccionPalabra(latitudDireccion.toString(),longitudDireccion.toString(),
                                        valorDireccion.toString(), resources.getString(R.string.key_google_maps))
                            }
                        }
                    }
                }
            }
        })

        editDireccion.onItemClickListener = AdapterView.OnItemClickListener {
            p0, p1, p2, p3 ->
            try {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editDireccion.windowToken, 0)
            } catch (e: Exception) { }
            val placeId = p0.getItemAtPosition(p2) as Array<String>
            if (utils.checkInternetConnection(this@NuevaDireccion, true)) {
                ubicacionDireccion(placeId[1], resources.getString(R.string.google_api_key), true)
            }
            //googleMap!!.uiSettings.isScrollGesturesEnabled = true
        }
        /**Para hacer scroll en el mapa verticalmente*/
        imagetrans.setOnTouchListener { view, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    // Disallow ScrollView to intercept touch events.
                    scrollView.requestDisallowInterceptTouchEvent(true)
                    // Disable touch on transparent view
                    false
                }
                MotionEvent.ACTION_UP -> {
                    // Allow ScrollView to intercept touch events.
                    scrollView.requestDisallowInterceptTouchEvent(false)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    scrollView.requestDisallowInterceptTouchEvent(true)
                    false
                }
                else -> true
            }
        }
    }

    /**Autocomplete map*/

    fun guardarCoordenadas(lat: String, log: String, bandDir: Boolean) {
        latitudDireccion = lat.toDouble()
        longitudDireccion = log.toDouble()
        if (googleMap != null) {
            googleMap!!.clear()
            googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitudDireccion!!, longitudDireccion!!), 16f))
        }
        obtenerDireccion(lat, log, bandDir)
    }

    fun valorDireccion(direccion: String ,bandPonerDir: Boolean ) {
        cargarDireccion = true
        bandDirCorrecto = true
        try {
            var data = direccion.split(",")
            if (data != null) {
                if (data.isNotEmpty() && bandPonerDir) {
                    if (data[0].contains(" a ")) {
                        var direc = data[0].split(" a ")
                        editDireccion.setText(direc[0], false)
                        valorDireccion = direc[0]
                    } else {
                        editDireccion.setText(data[0], false)
                        valorDireccion = data[0]
                    }
                }
            }
        } catch (e: Exception) {
        }
        var geocoder = Geocoder(this, Locale.getDefault())
        try {
            var addresses = geocoder.getFromLocation(latitudDireccion!!, longitudDireccion!!, 1)
            if (addresses.isNotEmpty()) {
                ciudadDireccion = addresses[0].locality
                if (ciudadDireccion == null) {
                    //ciudadDireccion = "Bogotá"
                    ciudadDireccion = ""
                }
            } else {
                ciudadDireccion = ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ciudadDireccion = ""
        }
    }

    fun llenarAutocomplete(lista: ArrayList<Array<String>>) {
        cargarDireccion = true
        val adapter = AdapterAutocomplete(this, R.layout.item_direccion_google, R.id.txtItemDireccionGoogle, lista)
        editDireccion.setAdapter(adapter)
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        if (googleMap != null) {
            googleMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
            //googleMap!!.uiSettings.isScrollGesturesEnabled = true
        }
        googleMap!!.setOnMapLoadedCallback {
            cargoMap = true
        }

        /*googleMap!!.setOnCameraIdleListener {
            latitudDireccion = googleMap!!.cameraPosition.target.latitude
            longitudDireccion = googleMap!!.cameraPosition.target.longitude
            editDireccion.setAdapter(null)
            if (bandPonerDir) {
                obtenerDireccion(latitudDireccion.toString(), longitudDireccion.toString(), true)
            }
            bandPonerDir = true
        }*/
        googleMap!!.setOnCameraChangeListener {
            if (cargoMap && cargarDireccion) {
                if (zoomActual == it.zoom) {
                    //cargarDireccion = false
                    var locationFinal = Location("punto final")
                    var visibleRegion = googleMap!!.projection.visibleRegion
                    var x = googleMap!!.projection.toScreenLocation(visibleRegion.farRight)
                    var y = googleMap!!.projection.toScreenLocation(visibleRegion.nearLeft)
                    var centerPoint = Point(x.x / 2, y.y / 2)
                    var centerFromPoint = googleMap!!.projection.fromScreenLocation(centerPoint)
                    if (centerFromPoint.latitude != 0.0 && centerFromPoint.longitude != 0.0) {
                        locationFinal.latitude = centerFromPoint.latitude
                        locationFinal.longitude = centerFromPoint.longitude
                    }
                    latitudDireccion = locationFinal.latitude
                    longitudDireccion = locationFinal.longitude
                    editDireccion.setAdapter(null)
                    if (bandPonerDir) {
                        obtenerDireccion(latitudDireccion.toString(), longitudDireccion.toString(), true)
                    }
                    bandPonerDir = true
                }
            }
            zoomActual = it.zoom
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_direccion)
        dialogProgress = DialogProgress(this@NuevaDireccion)
        setListeners()
        hideKeyboard()

        var params = intent.extras
        page = params.getInt("activity", 0)

        mapaDireccion = fragmentManager.findFragmentById(R.id.mapaDireccion) as MapFragment?
        if (mapaDireccion != null) {
            mapaDireccion!!.getMapAsync(this)
        }
        cargoMap = true
        cargarDireccion = false
        bandPonerDir = true
        latitudDireccion = 0.0
        longitudDireccion = 0.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            validarPermisos(android.Manifest.permission.ACCESS_FINE_LOCATION, encontrarUbicacion)
        } else {
            //trackerLocation = LocationServiceDireccion(this)
        }
        getDeviceLocation()
    }

    private fun hideKeyboard() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    // <editor-fold desc="Location coordinates">
    private fun getDeviceLocation() {
        if (!band) {
            checkLocation()
        }
        if (gps_enabled) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            try {
                if (mLocationPermissionsGranted) {
                    val location = mFusedLocationProviderClient!!.lastLocation
                    location.addOnCompleteListener{
                        if (it.isSuccessful){
                            try {
                                val currentLocation = it.result as Location
                                latitudDireccion = currentLocation.latitude
                                longitudDireccion = currentLocation.longitude
                                Log.e(TAG, currentLocation.latitude.toString() + "," + currentLocation.longitude)
                                Log.e(TAG, "onComplete: found location!")
                                moveCamera(LatLng(latitudDireccion!!, longitudDireccion!!), DEFAULT_ZOOM)

                            } catch (e: Exception) {
                                Log.e(TAG, "Exception onComplete: found location!")
                                onResume()
                            }
                        } else {
                            Log.e(TAG, "Exception getDeviceLocation")
                        }
                    }
                } else {
                    validarPermisos(android.Manifest.permission.ACCESS_FINE_LOCATION, encontrarUbicacion)
                    //getLocationPermission()
                }
            } catch (e: SecurityException) {
                Log.e(TAG, "getDeviceLocation: SecurityException: " + e.message)
            }
        }
    }

    private fun checkLocation() {
        band = true
        val lm = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ignored: Exception) { }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ignored: Exception) { }
        /*if (!gps_enabled && !network_enabled) {
            DosOpcionesDialog(getActivity(), "x", object : DosOpcionesDialog.RespuestaListener() {
                fun onOpcionNo() {
                    band = false
                }
                fun onOpcionSi() {
                    gps_enabled = true
                    network_enabled = true
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                    //getDeviceLocation();
                }
                fun onSalir() {
                    band = false
                }
            }).show()
        }//if*/
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        googleMap!!.clear()
        Log.e(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude)
        val options = MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin))
        googleMap!!.addMarker(options)
        val update = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        googleMap!!.animateCamera(update)
        hideKeyboard()
        if (band2){
            band2 = false
            guardarCoordenadas(latLng.latitude.toString(), latLng.longitude.toString(), true)
        }
        hideKeyboard()
    }
    // </editor-fold>

    // <editor-fold desc="Location Permissions">
    fun validarPermisos(perm: String,requestCode: Int) {
        if (checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, Array(1){perm}, requestCode)
            } else {
                ActivityCompat.requestPermissions(this, Array(1){perm}, requestCode)
            }
        } else {
            mLocationPermissionsGranted = true
            //trackerLocation = new LocationServiceDireccion(AgregarDireccionMapa.this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            encontrarUbicacion -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //trackerLocation = new LocationServiceDireccion(AgregarDireccionMapa.this)
                }
                return
            }
        }
    }
    // </editor-fold>

    // <editor-fold desc="Endpoints google Apis">
    private fun obtenerDireccion(lat: String, log: String, band: Boolean) {
        dialogProgress = DialogProgress(this)
        dialogProgress.show()
        bandPonerDir = true
        val latlon = "$lat,$log"
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        val ubicacion : Call<JsonElement> = endpoints.obtenerDireccion(resources.getString(R.string.key_google_maps), latlon, "true")
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

    private fun ubicacionDireccion(placeId: String, key: String, band: Boolean){
        dialogProgress = DialogProgress(this)
        dialogProgress.show()
        bandPonerDir = false
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
                        latitudDireccion = lat.toDouble()
                        longitudDireccion = lon.toDouble()
                        valorDireccion(resultado.getString("name"), true)
                        moveCamera(LatLng(latitudDireccion!!,longitudDireccion!!), DEFAULT_ZOOM)
                        //guardarCoordenadas(lat, lon, band)
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

    private fun direccionPalabra(lat: String, lon: String, dir: String, key: String){
        bandPonerDir = true
        var latlon = "$lat,$lon"
        var direccion = dir.replace(" - ", "-")
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        val ubicacion : Call<JsonElement> = endpoints.direccionPalabra(direccion, "true", key,latlon,
                "1000","country:CO", "ES")
        ubicacion.enqueue(object : Callback<JsonElement>{
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                /*if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }*/
            }
            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                /*if (dialogProgress.isShowing) {
                    dialogProgress.dismiss()
                }*/
                val direcciones = java.util.ArrayList<Array<String>>()
                try {
                    //val mainObject = JSONObject(result)
                    val mainObject = JSONObject(response!!.body().toString())
                    when (mainObject.getString("status")) {
                        "OK" -> {
                            val jsonArray = mainObject.getJSONArray("predictions")
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                if (i < 5) {
                                    val nombreDireccion = jsonObject.getString("description")
                                            .replace(", " + "Colombia", "")
                                    direcciones.add(arrayOf(nombreDireccion, jsonObject.getString("place_id")))
                                } else {
                                    break
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.toString())
                }
                llenarAutocomplete(direcciones)
            }
        })
    }
    // </editor-fold>

    // <editor-fold desc="Auxiliar methods for autocomplete">

    // </editor-fold>
}

