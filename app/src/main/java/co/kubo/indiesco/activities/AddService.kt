package co.kubo.indiesco.activities

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.*
import co.kubo.indiesco.dialog.DialogDosOpciones
import co.kubo.indiesco.fragment.*
import co.kubo.indiesco.modelo.Espacios
import co.kubo.indiesco.modelo.ServiceResumen
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter
import co.kubo.indiesco.restAPI.modelo.ResponseTotalToPay
import co.kubo.indiesco.utils.SharedPreferenceManager
import co.kubo.indiesco.utils.Singleton
import co.kubo.indiesco.utils.Utils
import kotlinx.android.synthetic.main.activity_add_service.*
import kotlinx.android.synthetic.main.activity_add_service.imgBotonVolver
import kotlinx.android.synthetic.main.activity_add_service.llProgress
import kotlinx.android.synthetic.main.activity_add_service.radiogroup
import kotlinx.android.synthetic.main.activity_add_service.rlValor
import kotlinx.android.synthetic.main.activity_add_service.tvNext
import kotlinx.android.synthetic.main.activity_add_service.tvValor
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sin


class AddService : AppCompatActivity(), View.OnClickListener, IVivieda,
        IDimension, IAddress, IEspacios, ITime {

    val df = DecimalFormat("$###,###.##")
    val singleton = Singleton.getInstance()
    var flagVivienda = false
    var flagDimensiones = false
    var flagEspacios = false
    var flagAddress = false
    var flagTime = false
    var totalCost = 0.0

    var flag1 = false
    var flag2 = false

    var utils = Utils()

    override fun checkTime() {
        if (flagVivienda && flagDimensiones && flagEspacios && flagAddress && flagEspacios) {
            flagTime = true
            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
            if (singleton.urgente == "si") {
                if (!singleton.isUrgentCalculate) {
                    val halfCost = totalCost / 2.0
                    val newTotalCost = halfCost + totalCost
                    totalCost = newTotalCost
                    tvValor.text = "Total: ${df.format(newTotalCost)}"
                    singleton.isUrgentCalculate = true
                }
            } else {
                getTotalPagarEspacios(getEspaciosOcupados())
                singleton.isUrgentCalculate = false
            }
        } else {
            flagTime = false
            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
            getTotalPagarEspacios(getEspaciosOcupados())
            singleton.isUrgentCalculate = false
        }
        //calculateTotal()

        //getTotalPagarEspacios(getEspaciosOcupados())

    }

    fun getEspaciosOcupados(): ArrayList<String> {
        val data = singleton.data
        val inmuebleEspacios = data[0].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].espacios
        val espacios = ArrayList<String>()
        inmuebleEspacios!!.forEach {
            if (it.qty > 0) {
                espacios.add("${it.id_espacio},${it.qty}")
            }
        }
        return espacios
    }

    override fun espaciosCheck(flag: Boolean, posInm: Int, posDim: Int) {
        if (flag) {
            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
        } else {
            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
        }
        flagEspacios = flag
        //calculateTotal()
        getTotalPagarEspacios(getEspaciosOcupados())
    }

    fun calculateTotal() {
        var data = singleton.data
        var total = 0.0
        for (item in data[0].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].espacios!!.indices) {
            if (data[0].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].espacios!![item].qty != 0
                    && data[0].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].espacios!![item].tipo == "valor") {
                var cost = data[0].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].espacios!![item].qty *
                        data[0].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].espacios!![item].valor!!.toDouble()
                total += cost
            }
        }
        var percentAux = 0.0
        for (item in data[0].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].espacios!!.indices) {
            if (data[0].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].espacios!![item].qty != 0
                    && data[0].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].espacios!![item].tipo == "porcentaje") {
                var percent = data[0].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].espacios!![item].qty *
                        data[0].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].espacios!![item].valor!!.toDouble()
                percentAux += percent
                /*var percent = data[0].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].espacios!![item].qty *
                        data[0].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].espacios!![item].valor!!.toDouble()
                var percent_aux = percent + 1
                total *= percent_aux*/
            }
        }

        try {
            when (singleton.getnPisos()) {
                "1" -> {
                    percentAux += singleton.priceFloorOne
                }
                "2" -> {
                    percentAux += singleton.priceFloorTwo
                }
                "3" -> {
                    percentAux += singleton.priceFloorThree
                }
            }
        } catch (e: Exception) {
        }
        total += (total * percentAux)
        when (singleton.urgente) {
            "si" -> {
                total += (total * 0.5)
                singleton.flagUrgente = true
            }
            "no" -> {
                if (singleton.flagUrgente) {
                    singleton.flagUrgente = false
                }
            }
        }

        tvValor.text = "Total: ${df.format(total)}"
        totalCost = total
    }

    override fun AddressCheck() {
        flagAddress = true
        llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
        rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
    }

    override fun dimensionCheck(num: Int) {
        when (num) {
            2 -> flag1 = true
            3 -> flag2 = true
        }
        if (flag1 && flag2) {
            flagDimensiones = true
            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
        }
        calculateTotal()
    }

    override fun viviendaCheck() {
        llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
        rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
        flagVivienda = true
    }

    fun getTotalPagar(espacios: ArrayList<String>) {
        val authToken = SharedPreferenceManager.getAuthToken(applicationContext)
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        val request: Call<ResponseTotalToPay> = endpoints.obtenerTotalPagar(authToken, singleton.idCategoria, singleton.idDimension, singleton.getnPisos(), espacios, "")
        request.enqueue(object : Callback<ResponseTotalToPay> {
            override fun onFailure(call: Call<ResponseTotalToPay>, t: Throwable) {
                Log.e("ERROR-RESP-TOTAL-PAY", t.message)
            }

            override fun onResponse(call: Call<ResponseTotalToPay>, response: Response<ResponseTotalToPay>) {
                Log.i("RESPONSE-TOTAL-PAY", response.body().toString())
                if (response.body()!!.code == "100") {
                    totalCost = response.body()!!.data.price
                }
            }
        })
    }

    fun getTotalPagarEspacios(espacios: ArrayList<String>) {
        val authToken = SharedPreferenceManager.getAuthToken(applicationContext)
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        val request: Call<ResponseTotalToPay> = endpoints.obtenerTotalPagar(authToken, singleton.idCategoria, singleton.idDimension, singleton.getnPisos(), espacios, "")
        request.enqueue(object : Callback<ResponseTotalToPay> {
            override fun onFailure(call: Call<ResponseTotalToPay>, t: Throwable) {
                Log.e("ERROR-RESP-TOTAL-PAY", t.message)
            }

            override fun onResponse(call: Call<ResponseTotalToPay>, response: Response<ResponseTotalToPay>) {
                Log.i("RESPONSE-TOTAL-PAY", response.body().toString())
                if (response.body()!!.code == "100") {
                    tvValor.text = "Total: ${df.format(response.body()!!.data.price)}"
                    totalCost = response.body()!!.data.price
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
            R.id.tvNext -> {
                var pos = viewPager.currentItem
                when (pos) {
                    0 -> {
                        if (flagVivienda) {
                            viewPager.currentItem = 1
                        } else {
                            Toast.makeText(applicationContext, "Elije tipo de vivienda", Toast.LENGTH_LONG).show()
                        }

                    }
                    1 -> {
                        if (flagDimensiones) {
                            viewPager.currentItem = 2
                        } else {
                            Toast.makeText(applicationContext, "Elije el rango de dimensiones y/o el número de pisos", Toast.LENGTH_LONG).show()
                        }

                    }
                    2 -> {
                        if (flagEspacios) {
                            viewPager.currentItem = 3
                        } else {
                            Toast.makeText(applicationContext, "Elije los espacios", Toast.LENGTH_LONG).show()
                        }

                    }
                    3 -> {
                        if (flagAddress) {
                            viewPager.currentItem = 4
                        } else {
                            Toast.makeText(applicationContext, "Elije la dirección", Toast.LENGTH_LONG).show()
                        }
                    }
                    4 -> {
                        if (flagVivienda && flagDimensiones && flagEspacios && flagAddress && flagEspacios && flagTime) {
                            val df = SimpleDateFormat("yyyy-MM-dd")
                            val currentDate = df.format(Calendar.getInstance().time)
                            var splitTime = singleton.hora.split(":")

                            if (singleton.urgente == "no") {
                                if (splitTime[0].toInt() < 6 || splitTime[0].toInt() > 18) {
                                    DialogDosOpciones(this@AddService, "4", object : DialogDosOpciones.RespuestaListener {
                                        override fun onCancelar() {}
                                        override fun onAceptar() {}
                                        override fun onSalir() {}
                                    }).show()
                                } else {
                                    if (currentDate == singleton.fecha) {
                                        //current time
                                        val format = SimpleDateFormat("HHmm") //this is format in military time
                                        val currentTime = format.format(Calendar.getInstance().time) //get current time
                                        var currentTimetime = format.parse(currentTime) as Date //convert string time in Date time

                                        //selected time
                                        var strHora = splitTime[0] + splitTime[1] //string time
                                        if (splitTime[0].length == 1)
                                            strHora = "0$strHora"
                                        val selectedTime = format.parse(strHora) as Date //to convert String military time in time

                                        val jdSelectedTime = DateTime(selectedTime)
                                        val jdCurrentTime = DateTime(currentTimetime)

                                        var diff = jdSelectedTime.compareTo(jdCurrentTime)
                                        when (diff) {
                                            -1 -> {
                                                DialogDosOpciones(this@AddService, "7", object : DialogDosOpciones.RespuestaListener {
                                                    override fun onCancelar() {}
                                                    override fun onAceptar() {}
                                                    override fun onSalir() {}
                                                }).show()
                                            }
                                            1 -> {
                                                when (singleton.urgente) {
                                                    "si" -> {
                                                        /**
                                                         * @author tony barajas
                                                         * Cambio propuesto el 17/9/18 eliminar el dialog para
                                                         * indicar que se realizara una visita antes
                                                         */
                                                        /*DialogDosOpciones(this@AddService, "9", object : DialogDosOpciones.RespuestaListener {
                                                            override fun onCancelar() {}
                                                            override fun onAceptar() {
                                                                var millisSelectedTime = selectedTime.time
                                                                var millisCurrentTime = currentTimetime.time
                                                                //this is for obtain diff in millis and divide between 60.000 to obtain diff in minutes
                                                                var diffMillis = (millisSelectedTime - millisCurrentTime) / 60000
                                                                if (diffMillis > 120) {
                                                                    var position = singleton.position
                                                                    var arrayResumen = singleton.resumen
                                                                    var serviceResumen = ServiceResumen()
                                                                    serviceResumen.category = singleton.categoria
                                                                    serviceResumen.date = singleton.fecha
                                                                    serviceResumen.address = singleton.direccion
                                                                    serviceResumen.id_direccion = singleton.idDir
                                                                    serviceResumen.dimension = singleton.dimension
                                                                    serviceResumen.id_dimension = singleton.idDimension
                                                                    serviceResumen.totalCost = totalCost.toString()
                                                                    serviceResumen.urgente = singleton.urgente
                                                                    serviceResumen.comentario = "ok"
                                                                    serviceResumen.pisos = singleton.getnPisos()
                                                                    serviceResumen.id_tipo_inmueble = singleton.idTipoInmueble
                                                                    serviceResumen.tipo_cobro = "espacios"
                                                                    serviceResumen.hora = singleton.hora

                                                                    var data = singleton.data
                                                                    var posInm = singleton.posTipoInmueble
                                                                    var posDim = singleton.posDimension
                                                                    var arrayEspacios = ArrayList<Espacios>()
                                                                    for (item in data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!!.indices) {
                                                                        if (data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].qty != 0) {
                                                                            var espacios_aux = Espacios()
                                                                            espacios_aux.id_espacio = data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].id_espacio!!
                                                                            serviceResumen.espacios.add(espacios_aux)
                                                                        }
                                                                    }
                                                                    arrayResumen.add(serviceResumen)
                                                                    singleton.position = position + 1
                                                                    //Ir a SolicitudServicio3
                                                                    val intent = Intent(applicationContext, SolicitudServicio3::class.java)
                                                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                                    startActivity(intent)
                                                                    finish()
                                                                } else {
                                                                    DialogDosOpciones(this@AddService, "5", object : DialogDosOpciones.RespuestaListener {
                                                                        override fun onCancelar() {}
                                                                        override fun onAceptar() {}
                                                                        override fun onSalir() {}
                                                                    }).show()
                                                                }
                                                            }
                                                            override fun onSalir() {}
                                                        }).show()*/

                                                        var millisSelectedTime = selectedTime.time
                                                        var millisCurrentTime = currentTimetime.time
                                                        //this is for obtain diff in millis and divide between 60.000 to obtain diff in minutes
                                                        var diffMillis = (millisSelectedTime - millisCurrentTime) / 60000
                                                        if (diffMillis > 120) {
                                                            var position = singleton.position
                                                            var arrayResumen = singleton.resumen
                                                            var serviceResumen = ServiceResumen()
                                                            serviceResumen.category = singleton.categoria
                                                            serviceResumen.date = singleton.fecha
                                                            serviceResumen.address = singleton.direccion
                                                            serviceResumen.id_direccion = singleton.idDir
                                                            serviceResumen.dimension = singleton.dimension
                                                            serviceResumen.id_dimension = singleton.idDimension
                                                            serviceResumen.totalCost = totalCost.toString()
                                                            serviceResumen.urgente = singleton.urgente
                                                            serviceResumen.comentario = "ok"
                                                            serviceResumen.pisos = singleton.getnPisos()
                                                            serviceResumen.id_tipo_inmueble = singleton.idTipoInmueble
                                                            serviceResumen.tipo_cobro = "espacios"
                                                            serviceResumen.hora = singleton.hora
                                                            serviceResumen.idTipoServicio = singleton.idTipoServicio

                                                            var data = singleton.data
                                                            var posInm = singleton.posTipoInmueble
                                                            var posDim = singleton.posDimension
                                                            for (item in data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!!.indices) {
                                                                if (data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].qty != 0) {
                                                                    var espaciosAux = Espacios()
                                                                    espaciosAux.id_espacio = data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].id_espacio!!
                                                                    espaciosAux.total = data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].qty
                                                                    serviceResumen.espacios.add(espaciosAux)
                                                                }
                                                            }
                                                            arrayResumen.add(serviceResumen)
                                                            singleton.position = position + 1
                                                            //Ir a SolicitudServicio3
                                                            val intent = Intent(applicationContext, SolicitudServicio3::class.java)
                                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                            startActivity(intent)
                                                            finish()
                                                        } else {
                                                            DialogDosOpciones(this@AddService, "5", object : DialogDosOpciones.RespuestaListener {
                                                                override fun onCancelar() {}
                                                                override fun onAceptar() {}
                                                                override fun onSalir() {}
                                                            }).show()
                                                        }
                                                    }
                                                    "no" -> {
                                                        /**
                                                         * @author tony barajas
                                                         * Cambio propuesto el 17/9/18 eliminar el dialog para
                                                         * indicar que se realizara una visita antes
                                                         */
                                                        /*DialogDosOpciones(this@AddService, "9", object : DialogDosOpciones.RespuestaListener {
                                                            override fun onCancelar() {}
                                                            override fun onAceptar() {
                                                                var millisSelectedTime = selectedTime.time
                                                                var millisCurrentTime = currentTimetime.time
                                                                //this is for obtain diff in millis and divide between 60.000 to obtain diff in minutes
                                                                var diffMillis = (millisSelectedTime - millisCurrentTime) / 60000
                                                                if (diffMillis > 300) {
                                                                    var position = singleton.position
                                                                    var arrayResumen = singleton.resumen
                                                                    var serviceResumen = ServiceResumen()
                                                                    serviceResumen.category = singleton.categoria
                                                                    serviceResumen.date = singleton.fecha
                                                                    serviceResumen.address = singleton.direccion
                                                                    serviceResumen.id_direccion = singleton.idDir
                                                                    serviceResumen.dimension = singleton.dimension
                                                                    serviceResumen.id_dimension = singleton.idDimension
                                                                    serviceResumen.totalCost = totalCost.toString()
                                                                    serviceResumen.urgente = singleton.urgente
                                                                    serviceResumen.comentario = "ok"
                                                                    serviceResumen.pisos = singleton.getnPisos()
                                                                    serviceResumen.id_tipo_inmueble = singleton.idTipoInmueble
                                                                    serviceResumen.tipo_cobro = "espacios"
                                                                    serviceResumen.hora = singleton.hora

                                                                    var data = singleton.data
                                                                    var posInm = singleton.posTipoInmueble
                                                                    var posDim = singleton.posDimension
                                                                    var arrayEspacios = ArrayList<Espacios>()
                                                                    for (item in data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!!.indices) {
                                                                        if (data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].qty != 0) {
                                                                            var espacios_aux = Espacios()
                                                                            espacios_aux.id_espacio = data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].id_espacio!!
                                                                            serviceResumen.espacios.add(espacios_aux)
                                                                        }
                                                                    }
                                                                    arrayResumen.add(serviceResumen)
                                                                    singleton.position = position + 1
                                                                    //Ir a SolicitudServicio3
                                                                    val intent = Intent(applicationContext, SolicitudServicio3::class.java)
                                                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                                    startActivity(intent)
                                                                    finish()
                                                                } else {
                                                                    DialogDosOpciones(this@AddService, "6", object : DialogDosOpciones.RespuestaListener {
                                                                        override fun onCancelar() {}
                                                                        override fun onAceptar() {}
                                                                        override fun onSalir() {}
                                                                    }).show()
                                                                }
                                                            }
                                                            override fun onSalir() {}
                                                        }).show()*/

                                                        var millisSelectedTime = selectedTime.time
                                                        var millisCurrentTime = currentTimetime.time
                                                        //this is for obtain diff in millis and divide between 60.000 to obtain diff in minutes
                                                        var diffMillis = (millisSelectedTime - millisCurrentTime) / 60000
                                                        if (diffMillis > 300) {
                                                            var position = singleton.position
                                                            var arrayResumen = singleton.resumen
                                                            var serviceResumen = ServiceResumen()
                                                            serviceResumen.category = singleton.categoria
                                                            serviceResumen.date = singleton.fecha
                                                            serviceResumen.address = singleton.direccion
                                                            serviceResumen.id_direccion = singleton.idDir
                                                            serviceResumen.dimension = singleton.dimension
                                                            serviceResumen.id_dimension = singleton.idDimension
                                                            serviceResumen.totalCost = totalCost.toString()
                                                            serviceResumen.urgente = singleton.urgente
                                                            serviceResumen.comentario = "ok"
                                                            serviceResumen.pisos = singleton.getnPisos()
                                                            serviceResumen.id_tipo_inmueble = singleton.idTipoInmueble
                                                            serviceResumen.tipo_cobro = "espacios"
                                                            serviceResumen.hora = singleton.hora
                                                            serviceResumen.idTipoServicio = singleton.idTipoServicio

                                                            var data = singleton.data
                                                            var posInm = singleton.posTipoInmueble
                                                            var posDim = singleton.posDimension
                                                            var arrayEspacios = ArrayList<Espacios>()
                                                            for (item in data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!!.indices) {
                                                                if (data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].qty != 0) {
                                                                    var espacios_aux = Espacios()
                                                                    espacios_aux.id_espacio = data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].id_espacio!!
                                                                    espacios_aux.total = data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].qty
                                                                    serviceResumen.espacios.add(espacios_aux)
                                                                }
                                                            }
                                                            arrayResumen.add(serviceResumen)
                                                            singleton.position = position + 1
                                                            //Ir a SolicitudServicio3
                                                            val intent = Intent(applicationContext, SolicitudServicio3::class.java)
                                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                            startActivity(intent)
                                                            finish()
                                                        } else {
                                                            DialogDosOpciones(this@AddService, "6", object : DialogDosOpciones.RespuestaListener {
                                                                override fun onCancelar() {}
                                                                override fun onAceptar() {}
                                                                override fun onSalir() {}
                                                            }).show()
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        /**
                                         * @author tony barajas
                                         * Cambio propuesto el 17/9/18 eliminar el dialog para
                                         * indicar que se realizara una visita antes
                                         */
                                        /*DialogDosOpciones(this@AddService, "9", object : DialogDosOpciones.RespuestaListener {
                                            override fun onCancelar() {}
                                            override fun onAceptar() {
                                                var position = singleton.position
                                                var arrayResumen = singleton.resumen
                                                var serviceResumen = ServiceResumen()
                                                serviceResumen.category = singleton.categoria
                                                serviceResumen.date = singleton.fecha
                                                serviceResumen.address = singleton.direccion
                                                serviceResumen.id_direccion = singleton.idDir
                                                serviceResumen.dimension = singleton.dimension
                                                serviceResumen.id_dimension = singleton.idDimension
                                                serviceResumen.totalCost = totalCost.toString()
                                                serviceResumen.urgente = singleton.urgente
                                                serviceResumen.comentario = "ok"
                                                serviceResumen.pisos = singleton.getnPisos()
                                                serviceResumen.id_tipo_inmueble = singleton.idTipoInmueble
                                                serviceResumen.tipo_cobro = "espacios"
                                                serviceResumen.hora = singleton.hora

                                                var data = singleton.data
                                                var posInm = singleton.posTipoInmueble
                                                var posDim = singleton.posDimension
                                                var arrayEspacios = ArrayList<Espacios>()
                                                for (item in data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!!.indices) {
                                                    if (data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].qty != 0) {
                                                        var espacios_aux = Espacios()
                                                        espacios_aux.id_espacio = data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].id_espacio!!
                                                        serviceResumen.espacios.add(espacios_aux)
                                                    }
                                                }
                                                arrayResumen.add(serviceResumen)
                                                singleton.position = position + 1
                                                //Ir a SolicitudServicio3
                                                val intent = Intent(applicationContext, SolicitudServicio3::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                startActivity(intent)
                                                finish()
                                            }
                                            override fun onSalir() {}
                                        }).show()*/

                                        var position = singleton.position
                                        var arrayResumen = singleton.resumen
                                        var serviceResumen = ServiceResumen()
                                        serviceResumen.category = singleton.categoria
                                        serviceResumen.date = singleton.fecha
                                        serviceResumen.address = singleton.direccion
                                        serviceResumen.id_direccion = singleton.idDir
                                        serviceResumen.dimension = singleton.dimension
                                        serviceResumen.id_dimension = singleton.idDimension
                                        serviceResumen.totalCost = totalCost.toString()
                                        serviceResumen.urgente = singleton.urgente
                                        serviceResumen.comentario = "ok"
                                        serviceResumen.pisos = singleton.getnPisos()
                                        serviceResumen.id_tipo_inmueble = singleton.idTipoInmueble
                                        serviceResumen.tipo_cobro = "espacios"
                                        serviceResumen.hora = singleton.hora
                                        serviceResumen.idTipoServicio = singleton.idTipoServicio

                                        var data = singleton.data
                                        var posInm = singleton.posTipoInmueble
                                        var posDim = singleton.posDimension
                                        var arrayEspacios = ArrayList<Espacios>()
                                        for (item in data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!!.indices) {
                                            if (data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].qty != 0) {
                                                var espacios_aux = Espacios()
                                                espacios_aux.id_espacio = data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].id_espacio!!
                                                espacios_aux.total = data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].qty
                                                serviceResumen.espacios.add(espacios_aux)
                                            }
                                        }
                                        arrayResumen.add(serviceResumen)
                                        singleton.position = position + 1
                                        //Ir a SolicitudServicio3
                                        val intent = Intent(applicationContext, SolicitudServicio3::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            } else { //Si el servicio es urgente // validar si esta dentro de 6am a 4pm
                                if (splitTime[0].toInt() >= 6 || splitTime[0].toInt() < 16) {

                                    /**
                                     * @author tony barajas
                                     * Cambio propuesto el 17/9/18 eliminar el dialog para
                                     * indicar que se realizara una visita antes
                                     */
                                    /*DialogDosOpciones(this@AddService, "9", object : DialogDosOpciones.RespuestaListener {
                                        override fun onCancelar() {}
                                        override fun onAceptar() {
                                            var position = singleton.position
                                            var arrayResumen = singleton.resumen
                                            var serviceResumen = ServiceResumen()
                                            serviceResumen.category = singleton.categoria
                                            serviceResumen.date = singleton.fecha
                                            serviceResumen.address = singleton.direccion
                                            serviceResumen.id_direccion = singleton.idDir
                                            serviceResumen.dimension = singleton.dimension
                                            serviceResumen.id_dimension = singleton.idDimension
                                            serviceResumen.totalCost = totalCost.toString()
                                            serviceResumen.urgente = singleton.urgente
                                            serviceResumen.comentario = "ok"
                                            serviceResumen.pisos = singleton.getnPisos()
                                            serviceResumen.id_tipo_inmueble = singleton.idTipoInmueble
                                            serviceResumen.tipo_cobro = "espacios"
                                            serviceResumen.hora = singleton.hora

                                            var data = singleton.data
                                            var posInm = singleton.posTipoInmueble
                                            var posDim = singleton.posDimension
                                            var arrayEspacios = ArrayList<Espacios>()
                                            for (item in data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!!.indices) {
                                                if (data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].qty != 0) {
                                                    var espacios_aux = Espacios()
                                                    espacios_aux.id_espacio = data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].id_espacio!!
                                                    serviceResumen.espacios.add(espacios_aux)
                                                }
                                            }
                                            arrayResumen.add(serviceResumen)
                                            singleton.position = position + 1
                                            //Ir a SolicitudServicio3
                                            val intent = Intent(applicationContext, SolicitudServicio3::class.java)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(intent)
                                            finish()
                                        }
                                        override fun onSalir() {}
                                    }).show()*/

                                    var position = singleton.position
                                    var arrayResumen = singleton.resumen
                                    var serviceResumen = ServiceResumen()
                                    serviceResumen.category = singleton.categoria
                                    serviceResumen.date = singleton.fecha
                                    serviceResumen.address = singleton.direccion
                                    serviceResumen.id_direccion = singleton.idDir
                                    serviceResumen.dimension = singleton.dimension
                                    serviceResumen.id_dimension = singleton.idDimension
                                    serviceResumen.totalCost = totalCost.toString()
                                    serviceResumen.urgente = singleton.urgente
                                    serviceResumen.comentario = "ok"
                                    serviceResumen.pisos = singleton.getnPisos()
                                    serviceResumen.id_tipo_inmueble = singleton.idTipoInmueble
                                    serviceResumen.tipo_cobro = "espacios"
                                    serviceResumen.hora = singleton.hora
                                    serviceResumen.idTipoServicio = singleton.idTipoServicio

                                    var data = singleton.data
                                    var posInm = singleton.posTipoInmueble
                                    var posDim = singleton.posDimension
                                    var arrayEspacios = ArrayList<Espacios>()
                                    for (item in data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!!.indices) {
                                        if (data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].qty != 0) {
                                            var espaciosAux = Espacios()
                                            espaciosAux.id_espacio = data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].id_espacio!!
                                            espaciosAux.total = data[0].tiposInmuebles[posInm.toInt()].dimesiones!![posDim.toInt()].espacios!![item].qty
                                            serviceResumen.espacios.add(espaciosAux)
                                        }
                                    }
                                    arrayResumen.add(serviceResumen)
                                    singleton.position = position + 1
                                    //Ir a SolicitudServicio3
                                    val intent = Intent(applicationContext, SolicitudServicio3::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                } else {
                                    DialogDosOpciones(this@AddService, "4", object : DialogDosOpciones.RespuestaListener {
                                        override fun onCancelar() {}
                                        override fun onAceptar() {}
                                        override fun onSalir() {}
                                    }).show()
                                }
                            }
                        } else {
                            if (!flagVivienda) {
                                Toast.makeText(applicationContext, "Elije tipo de vivienda", Toast.LENGTH_LONG).show()
                            } else if (!flagDimensiones) {
                                Toast.makeText(applicationContext, "Elije el rango de dimensiones y/o el número de pisos", Toast.LENGTH_LONG).show()
                            } else if (!flagEspacios) {
                                Toast.makeText(applicationContext, "Elije los espacios", Toast.LENGTH_LONG).show()
                            } else if (!flagAddress) {
                                Toast.makeText(applicationContext, "Elije la dirección", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        var pos = viewPager.currentItem
        when (pos) {
            0 -> {
                if (validation()) {
                    val intent = Intent(this, TipoInmueble::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }
            }
            1 -> {
                singleton.posDimension = "0"
                viewPager.currentItem = 0
            }
            2 -> {
                viewPager.currentItem = 1
            }
            3 -> {
                viewPager.currentItem = 2
            }
            4 -> {
                viewPager.currentItem = 3
            }
        }
    }

    private fun validation(): Boolean {
        if (!utils.checkInternetConnection(this@AddService, true)) {
            return false
        }
        return true
    }

    private fun setListeners() {
        imgBotonVolver.setOnClickListener(this)
        tvNext.setOnClickListener(this)
    }

    private fun agregarFragments(flag: Int): ArrayList<Fragment> {
        val fragments = ArrayList<Fragment>()
        when (flag) {
            0 -> {
                fragments.add(TipoViviendaFragment())
                fragments.add(DimensionesFragment())
                //fragments.add(RoomsFragment())
                fragments.add(EspaciosPrincipalesFragment())
                //fragments.add(EspaciosSecuendariosFragment())
                fragments.add(ChooseAddressFragment())
                //fragments.add(DireccionFragment())
                fragments.add(ServiceTimeFragment())
            }
            1 -> {
                fragments.add(NumeroPisosFragment())
                fragments.add(ChooseAddressFragment())
                //fragments.add(DireccionFragment())
                fragments.add(ServiceTimeFragment())
            }
        }
        return fragments
    }

    private fun setUpViewPager(flag: Int) {
        viewPager.adapter = PageAdapter(supportFragmentManager, agregarFragments(flag))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)

        var params = intent.extras
        var flag = params.getInt("type", 0)
        setUpViewPager(flag)
        setListeners()

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = (size.x) / 5
        val height = size.y
        viewPager.setPagingEnabled(false)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> { //Tipo vivienda
                        radiogroup.check(R.id.radioButton)
                        tvValor.visibility = View.INVISIBLE
                        llProgress.layoutParams.width = width
                        llProgress.requestLayout()
                        if (flagVivienda) {
                            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
                            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
                        } else {
                            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
                            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
                        }
                    }
                    1 -> { //Dimensiones
                        radiogroup.check(R.id.radioButton2)
                        tvValor.visibility = View.VISIBLE
                        llProgress.layoutParams.width = width * 2
                        llProgress.requestLayout()
                        if (flagDimensiones) {
                            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
                            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
                        } else {
                            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
                            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
                        }
                    }
                    2 -> { //Espacios
                        radiogroup.check(R.id.radioButton3)
                        tvValor.visibility = View.VISIBLE
                        llProgress.layoutParams.width = width * 3
                        llProgress.requestLayout()
                        if (flagEspacios) {
                            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
                            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
                        } else {
                            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
                            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
                        }
                    }
                    3 -> { //Direccion
                        radiogroup.check(R.id.radioButton4)
                        tvValor.visibility = View.VISIBLE
                        llProgress.layoutParams.width = width * 4
                        llProgress.requestLayout()
                        if (flagAddress) {
                            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
                            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
                        } else {
                            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
                            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
                        }
                    }
                    4 -> { //Time
                        radiogroup.check(R.id.radioButton5)
                        tvValor.visibility = View.VISIBLE
                        llProgress.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
                        llProgress.requestLayout()
                        if (flagTime) {
                            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
                            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
                        } else {
                            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
                            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
                        }
                    }
                    else -> {
                    }
                }//switch
            }//public void onPageSelected

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


}
