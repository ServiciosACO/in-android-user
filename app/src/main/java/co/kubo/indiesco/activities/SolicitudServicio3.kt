package co.kubo.indiesco.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.AdapterResumen
import co.kubo.indiesco.adaptadores.IChangeLayout
import co.kubo.indiesco.dialog.DialogProgress
import co.kubo.indiesco.modelo.ServiceResumen
import co.kubo.indiesco.modelo.Usuario
import co.kubo.indiesco.restAPI.ConstantesRestApi
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter
import co.kubo.indiesco.restAPI.modelo.ResponseCodigoDescuento
import co.kubo.indiesco.restAPI.modelo.ResponseCrearServicio
import co.kubo.indiesco.utils.SharedPreferenceManager
import co.kubo.indiesco.utils.Singleton
import co.kubo.indiesco.utils.Utils
import kotlinx.android.synthetic.main.activity_solicitud_servicio3.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class SolicitudServicio3 : AppCompatActivity(), View.OnClickListener, IChangeLayout {

    val df = DecimalFormat("$###,###.##")
    lateinit var dialogProgress: DialogProgress
    val singleton = Singleton.getInstance()
    val sharedPreferenceManager = SharedPreferenceManager()
    val utils = Utils()
    var codigo = ""
    var id_codigo_descuento = 0
    var descuento = 0

    lateinit var llm: LinearLayoutManager
    private lateinit var adapter: AdapterResumen
    var total = 0.0

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
            R.id.tvAddService -> {
                val intent = Intent(this, FechaServicio::class.java)
                startActivity(intent)
            }
            R.id.tvValidateCoupon -> {
                if (validation()) {
                    if (singleton.validateCoupon) {
                        Toast.makeText(applicationContext, "Ya fue usado un cupón", Toast.LENGTH_LONG).show()
                    } else {
                        var code = editCode.text.toString()
                        validateCoupon(code)
                    }
                }
            }
            R.id.tvPayment -> {
                var resumen = singleton.resumen
                var reqBody: String
                var jsonArray = JSONArray()
                for (item in resumen.indices) {
                    var jsonObject = JSONObject()
                    jsonObject.put("id_tipo_inmueble", resumen[item].id_tipo_inmueble)
                    jsonObject.put("id_dimension", resumen[item].id_dimension)
                    jsonObject.put("pisos", resumen[item].pisos)
                    jsonObject.put("valor", total.toString())
                    jsonObject.put("id_direccion", resumen[item].id_direccion)
                    jsonObject.put("fecha_servicio", resumen[item].date)
                    jsonObject.put("urgente", resumen[item].urgente)
                    jsonObject.put("hora", resumen[item].hora)
                    jsonObject.put("comentario", resumen[item].comentario)
                    jsonObject.put("tipo_cobro", resumen[item].tipo_cobro)
                    if (resumen[item].tipo_cobro.equals("espacios")) {
                        var jsonArrayEspacios = JSONArray()
                        for (i in resumen[item].espacios.indices) {
                            var jsonObjectEspacios = JSONObject()
                            jsonObjectEspacios.put("id_espacio", resumen[item].espacios[i].id_espacio)
                            jsonArrayEspacios.put(jsonObjectEspacios)
                        }
                        var aux = jsonArrayEspacios.toString().replace("\"[", "[")
                                .replace("]\"", "]")
                                .replace("\\\"", "\"")
                        jsonObject.put("espacios", aux)
                    } else {
                        jsonObject.put("espacios", "[]")
                    }
                    jsonArray.put(jsonObject)
                }
                reqBody = jsonArray.toString().replace("\"[", "[")
                        .replace("]\"", "]")
                        .replace("\\\"", "\"")
                var cantidad_fechas = resumen.size
                crearServicio(reqBody, cantidad_fechas)
            }
        }
    }

    fun crearServicio(reqBody: String, cantidad_fechas: Int) {
        dialogProgress = DialogProgress(this)
        dialogProgress.show()
        var authToken = SharedPreferenceManager.getAuthToken(applicationContext)
        var restApiAdapter = RestApiAdapter()
        var endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        var usuario = Usuario()
        usuario = SharedPreferenceManager.getInfoUsuario(applicationContext)
        var id_user = usuario.id_user
        val responseCrearServicio: Call<ResponseCrearServicio> = endpoints.crearServicio(authToken, id_user,
                total.toInt(), id_codigo_descuento, descuento, cantidad_fechas, reqBody)
        responseCrearServicio.enqueue(object : Callback<ResponseCrearServicio> {
            override fun onFailure(call: Call<ResponseCrearServicio>?, t: Throwable?) {
                if (dialogProgress.isShowing)
                    dialogProgress.dismiss()
            }

            override fun onResponse(call: Call<ResponseCrearServicio>?, response: Response<ResponseCrearServicio>?) {
                if (dialogProgress.isShowing)
                    dialogProgress.dismiss()
                if (response!!.isSuccessful) {
                    when (response.body()!!.code) {
                        "100" -> {
                            var urlBase = ConstantesRestApi.URL_BASE
                            var id_solicitud = response.body()!!.data.idSolicitud
                            var urlTransaccion = "$urlBase/servicios/resumen_pedido/$id_user/$id_solicitud/servicio"
                            var goPago = Intent(applicationContext, Transaccion::class.java)
                            goPago.putExtra("type", "servicio")
                            goPago.putExtra("url", urlTransaccion)
                            goPago.putExtra("id_sol", id_solicitud.toString())
                            startActivity(goPago)
                        }
                        "102" ->
                            Toast.makeText(applicationContext, "No fue posible crear el servicio, intente de nuevo", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun hideSoftKeyboard() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    fun validation(): Boolean {
        if (!utils.checkInternetConnection(this@SolicitudServicio3, true)) {
            return false
        }
        if (editCode.text.isEmpty()) {
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
        var responseCodigoDescuentoCall: Call<ResponseCodigoDescuento> = endpoints.validarCupon(authToken, id, code)
        responseCodigoDescuentoCall.enqueue(object : Callback<ResponseCodigoDescuento> {
            override fun onFailure(call: Call<ResponseCodigoDescuento>?, t: Throwable?) {
                if (dialogProgress.isShowing)
                    dialogProgress.dismiss()
            }

            override fun onResponse(call: Call<ResponseCodigoDescuento>?, response: Response<ResponseCodigoDescuento>?) {
                if (dialogProgress.isShowing)
                    dialogProgress.dismiss()
                if (response!!.isSuccessful) {
                    when (response.body()!!.code) {
                        "100" -> {
                            editCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.coupon_check, 0)
                            when (response.body()!!.data!!.tipoCodigo) {
                                "1" -> {//porcentaje
                                    //tvDiscount.visibility = View.VISIBLE
                                    //tvDiscount.text = (response.body()!!.data!!.valor!!.toDouble() * -1).toString()
                                    singleton.discountValue = ((response.body()!!.data!!.valor!!.toDouble() / 100) * total) * -1
                                    var code_percent = (response.body()!!.data!!.valor!!.toDouble() / 100)
                                    total -= (total * code_percent)
                                    tvDiscount.text = "${df.format(singleton.discountValue * -1)}"

                                }
                                "2" -> {//valor
                                    //tvDiscount.visibility = View.VISIBLE
                                    //tvDiscount.text = (((response.body()!!.data!!.valor!!.toDouble()/100)*total) * -1).toString()

                                    singleton.discountValue = (response.body()!!.data!!.valor!!.toDouble() * -1)
                                    total -= response.body()!!.data!!.valor!!.toDouble()
                                    tvDiscount.text = "${df.format(response.body()!!.data!!.valor!!.toDouble())}"
                                }
                            }
                            singleton.discountCode = response.body()!!.data!!.tipoCodigo
                            singleton.validateCoupon = true
                            tvValor.text = "Total: ${df.format(total)}"
                            tvTotal.text = "${df.format(total)}"
                        }
                        "101" -> {
                            editCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.coupon_error, 0)
                            Toast.makeText(applicationContext, "El código ingresado ya fue redimido", Toast.LENGTH_LONG).show()
                        }
                        "102" -> {
                            editCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.coupon_error, 0)
                            Toast.makeText(applicationContext, "El código ingresado no es válido", Toast.LENGTH_LONG).show()
                        }
                        "103" -> {
                            editCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.coupon_error, 0)
                            Toast.makeText(applicationContext, "El código ingresado expiró", Toast.LENGTH_LONG).show()
                        }
                        "104" -> {
                            editCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.coupon_error, 0)
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
        singleton.validateCoupon = false
        val intent = Intent(this, Home::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

    fun setListeners() {
        imgBotonVolver.setOnClickListener(this)
        tvAddService.setOnClickListener(this)
        tvValidateCoupon.setOnClickListener(this)
        tvPayment.setOnClickListener(this)
    }

    override fun changeForNoService(flag: Boolean, mList: ArrayList<ServiceResumen>) {
        if (flag) {
            llServices.visibility = View.GONE
            llNoServices.visibility = View.VISIBLE
            singleton.validateCoupon = false
        } else {
            total = 0.0
            for (item in mList.indices) {
                var unitPrice = mList[item].totalCost.toDouble()
                total += unitPrice
            }
            tvValor.text = "Total: ${df.format(total)}"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitud_servicio3)
        setListeners()

        var resumen = singleton.resumen
        if (resumen.isNotEmpty() || resumen.size != 0) {
            llServices.visibility = View.VISIBLE
            llNoServices.visibility = View.GONE

            rvResumen.setHasFixedSize(false) //Set it to true if you are adding or removing items
            // in the Recycler-View and that doesn’t change it’s height or the width.
            llm = LinearLayoutManager(this)
            rvResumen.layoutManager = llm
            adapter = AdapterResumen(resumen, this, this)
            rvResumen.adapter = adapter
            for (item in resumen.indices) {
                var unitPrice = resumen[item].totalCost.toDouble()
                total += unitPrice
            }
            tvValor.text = "Total: ${df.format(total)}"
            tvSubTotal.text = "${df.format(total)}"
            tvDiscount.text = "${df.format(0)}"
            tvTotal.text = "${df.format(total)}"
        } else {
            llServices.visibility = View.GONE
            llNoServices.visibility = View.VISIBLE
        }
        hideSoftKeyboard()
    }
}
