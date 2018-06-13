package co.kubo.indiesco.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.IAddress
import co.kubo.indiesco.adaptadores.IDimension
import co.kubo.indiesco.adaptadores.IVivieda
import co.kubo.indiesco.adaptadores.PageAdapter
import co.kubo.indiesco.dialog.DialogDosOpciones
import co.kubo.indiesco.fragment.*
import co.kubo.indiesco.modelo.Espacios
import co.kubo.indiesco.modelo.ServiceResumen
import co.kubo.indiesco.utils.Singleton
import co.kubo.indiesco.utils.Utils
import kotlinx.android.synthetic.main.activity_add_service2.*
import org.joda.time.DateTime
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class AddService2 : AppCompatActivity(), View.OnClickListener,
        IAddress, ITime, INpisos, IVivieda, IDimension {

    val df = DecimalFormat("$###,###")
    val singleton = Singleton.getInstance()

    var flagNpisos = false
    var flagAddress = false
    var flagTime = false
    var flagVivienda = false
    var flagDimensiones = false
    var totalCost = 0.0

    var flag1 = false
    var flag2 = false

    val utils = Utils()

    override fun dimensionCheck(num: Int) {
        when(num){
            2 -> flag1 = true
            3 -> flag2 = true
        }
        if (flag1 && flag2){
            flagDimensiones = true
            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
        }
    }

    override fun viviendaCheck() {
        flagVivienda = true
        llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
        rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
    }

    override fun checkNPisos(b: Boolean) {
        flagNpisos = b
        if (b){
            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
        } else {
            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
        }
    }

    override fun checkTime() {
        if (flagNpisos && flagAddress){
            flagTime = true
            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
        } else {
            flagTime = false
            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
        }
    }

    override fun AddressCheck() {
        flagAddress = true
        llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
        rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
            R.id.tvNext -> {
                var pos = viewPager2.currentItem
                when (pos) {
                    0 -> {
                        viewPager2.currentItem = 1
                    }
                    1 -> {
                        viewPager2.currentItem = 2
                    }
                    2 -> {
                        viewPager2.currentItem = 3
                    }
                    3 -> {
                        viewPager2.currentItem = 4
                    }
                    4 -> {
                        if (flagVivienda && flagDimensiones && flagNpisos && flagAddress && flagTime){
                            val df = SimpleDateFormat("yyyy-MM-dd")
                            val currentDate = df.format(Calendar.getInstance().time)
                            var splitTime = singleton.hora.split(":")
                            if (splitTime[0].toInt() < 6 || splitTime[0].toInt() > 18){
                                DialogDosOpciones(this@AddService2, "4", object : DialogDosOpciones.RespuestaListener{
                                    override fun onCancelar() {}
                                    override fun onAceptar() {}
                                    override fun onSalir() {}
                                }).show()
                            } else {
                                if (currentDate == singleton.fecha){
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
                                    when (diff){
                                        -1 -> {
                                            DialogDosOpciones(this@AddService2, "7", object : DialogDosOpciones.RespuestaListener{
                                                override fun onCancelar() {}
                                                override fun onAceptar() {}
                                                override fun onSalir() {}
                                            }).show()
                                        }
                                        1 -> {
                                            when(singleton.urgente){

                                                "si" -> {
                                                    var millisSelectedTime = selectedTime.time
                                                    var millisCurrentTime = currentTimetime.time
                                                    //this is for obtain diff in millis and divide between 60.000 to obtain diff in minutes
                                                    var diffMillis = (millisSelectedTime - millisCurrentTime) / 60000
                                                    if (diffMillis > 120){
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
                                                        serviceResumen.pisos = 0.toString()
                                                        serviceResumen.id_tipo_inmueble = singleton.idTipoInmueble
                                                        serviceResumen.tipo_cobro = "pisos"
                                                        serviceResumen.hora = singleton.hora
                                                        var espacios_aux = Espacios()
                                                        serviceResumen.espacios.add(espacios_aux)
                                                        arrayResumen.add(serviceResumen)

                                                        singleton.position = position + 1
                                                        //Ir a SolicitudServicio3
                                                        val intent = Intent(this, SolicitudServicio3 :: class.java)
                                                        startActivity(intent)
                                                        finish()
                                                    } else {
                                                        DialogDosOpciones(this@AddService2, "5", object : DialogDosOpciones.RespuestaListener{
                                                            override fun onCancelar() {}
                                                            override fun onAceptar() {}
                                                            override fun onSalir() {}
                                                        }).show()
                                                    }
                                                }
                                                "no" -> {
                                                    var millisSelectedTime = selectedTime.time
                                                    var millisCurrentTime = currentTimetime.time
                                                    //this is for obtain diff in millis and divide between 60.000 to obtain diff in minutes
                                                    var diffMillis = (millisSelectedTime - millisCurrentTime) / 60000
                                                    if (diffMillis > 300){
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
                                                        serviceResumen.pisos = 0.toString()
                                                        serviceResumen.id_tipo_inmueble = singleton.idTipoInmueble
                                                        serviceResumen.tipo_cobro = "pisos"
                                                        serviceResumen.hora = singleton.hora
                                                        var espacios_aux = Espacios()
                                                        serviceResumen.espacios.add(espacios_aux)
                                                        arrayResumen.add(serviceResumen)

                                                        singleton.position = position + 1
                                                        //Ir a SolicitudServicio3
                                                        val intent = Intent(this, SolicitudServicio3 :: class.java)
                                                        startActivity(intent)
                                                        finish()
                                                    } else {
                                                        DialogDosOpciones(this@AddService2, "6", object : DialogDosOpciones.RespuestaListener{
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
                                    serviceResumen.pisos = 0.toString()
                                    serviceResumen.id_tipo_inmueble = singleton.idTipoInmueble
                                    serviceResumen.tipo_cobro = "pisos"
                                    serviceResumen.hora = singleton.hora
                                    var espacios_aux = Espacios()
                                    serviceResumen.espacios.add(espacios_aux)
                                    arrayResumen.add(serviceResumen)

                                    singleton.position = position + 1
                                    //Ir a SolicitudServicio3
                                    val intent = Intent(this, SolicitudServicio3 :: class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        } else {
                            if (!flagVivienda){
                                Toast.makeText(applicationContext, "Elije tipo de vivienda", Toast.LENGTH_LONG).show()
                            } else if (!flagDimensiones){
                                Toast.makeText(applicationContext, "Elije las dimensiones", Toast.LENGTH_LONG).show()
                            } else if(!flagNpisos){
                                Toast.makeText(applicationContext, "Elije el número de pisos", Toast.LENGTH_LONG).show()
                            } else if (!flagAddress){
                                Toast.makeText(applicationContext, "Elije la dirección", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun validation(): Boolean {
        if (!utils.checkInternetConnection(this@AddService2, true)){
            return false
        }
        return true
    }

    override fun onBackPressed() {
        if (validation()){
            val intent = Intent (this, TipoInmueble :: class.java)
            startActivity(intent)
            finish()
        }
        super.onBackPressed()
    }

    private fun setListeners() {
        imgBotonVolver.setOnClickListener(this)
        tvNext.setOnClickListener(this)
    }

    private fun agregarFragments(flag: Int): ArrayList<Fragment> {
        val fragments = ArrayList<Fragment>()
        fragments.add(TipoViviendaFragment())
        fragments.add(DimensionesFragment())
        fragments.add(NumeroPisosFragment())
        fragments.add(ChooseAddressFragment())
        fragments.add(ServiceTimeFragment())
        return fragments
    }

    private fun setUpViewPager(flag: Int) {
        viewPager2.adapter = PageAdapter(supportFragmentManager, agregarFragments(flag))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service2)
        var params = intent.extras
        var flag = params.getInt("type", 0)
        setUpViewPager(flag)
        setListeners()

        viewPager2.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> { //Tipo vivienda
                        radiogroup.check(R.id.radioButton)
                        tvValor.visibility = View.INVISIBLE
                        llProgress.layoutParams.width = 106
                        llProgress.requestLayout()
                        if (flagVivienda){
                            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
                            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
                        } else {
                            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
                            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
                        }
                    }
                    1 -> { //Dimensiones
                        radiogroup.check(R.id.radioButton2)
                        tvValor.visibility = View.INVISIBLE
                        llProgress.layoutParams.width = 213
                        llProgress.requestLayout()
                        if (flagDimensiones){
                            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
                            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
                        } else {
                            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
                            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
                        }
                    }
                    2 -> { //Numero de pisos
                        radiogroup.check(R.id.radioButton2)
                        tvValor.visibility = View.VISIBLE
                        llProgress.layoutParams.width = 257
                        llProgress.requestLayout()
                        if (flagNpisos){
                            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
                            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
                        } else {
                            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
                            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
                        }
                    }
                    3 -> { //Direccion
                        radiogroup.check(R.id.radioButton3)
                        tvValor.visibility = View.VISIBLE
                        llProgress.layoutParams.width = 300
                        llProgress.requestLayout()
                        if (flagAddress){
                            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
                            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
                        } else {
                            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
                            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
                        }
                    }
                    4 -> { //Tiempo
                        radiogroup.check(R.id.radioButton4)
                        tvValor.visibility = View.VISIBLE
                        llProgress.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
                        llProgress.requestLayout()
                        if (flagTime){
                            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
                            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
                        } else {
                            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
                            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
                        }
                    }
                }//switch
            }//public void onPageSelected
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}
