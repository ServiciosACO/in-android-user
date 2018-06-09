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
import co.kubo.indiesco.adaptadores.*
import co.kubo.indiesco.fragment.*
import co.kubo.indiesco.modelo.Espacios
import co.kubo.indiesco.modelo.InmuebleEspacios
import co.kubo.indiesco.modelo.ServiceResumen
import co.kubo.indiesco.utils.Singleton
import kotlinx.android.synthetic.main.activity_add_service.*
import java.text.DecimalFormat
import java.util.ArrayList


class AddService : AppCompatActivity(), View.OnClickListener, IVivieda,
        IDimension, IAddress, IEspacios, ITime {

    val df = DecimalFormat("$###,###")
    val singleton = Singleton.getInstance()
    var flagVivienda = false
    var flagDimensiones = false
    var flagEspacios = false
    var flagAddress = false
    var flagTime = false
    var totalCost = 0.0

    var flag1 = false
    var flag2 = false

    override fun checkTime() {
        if (flagVivienda && flagDimensiones && flagEspacios && flagAddress && flagEspacios){
            flagTime = true
            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
        } else {
            flagTime = false
            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
        }
    }

    override fun espaciosCheck(flag: Boolean, posInm: Int, posDim: Int) {
        if (flag){
            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
        } else {
            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
        }
        flagEspacios = flag
        var data = singleton.data
        var total = 0.0
        for (item in data[0].tiposInmuebles[posInm].dimesiones!![posDim].espacios!!.indices){
            if (data[0].tiposInmuebles[posInm].dimesiones!![posDim].espacios!![item].qty != 0
                    && data[0].tiposInmuebles[posInm].dimesiones!![posDim].espacios!![item].tipo == "valor"){
                var cost = data[0].tiposInmuebles[posInm].dimesiones!![posDim].espacios!![item].qty *
                        data[0].tiposInmuebles[posInm].dimesiones!![posDim].espacios!![item].valor!!.toDouble()
                total += cost
            }
        }
        for (item in data[0].tiposInmuebles[posInm].dimesiones!![posDim].espacios!!.indices){
            if (data[0].tiposInmuebles[posInm].dimesiones!![posDim].espacios!![item].qty != 0
                    && data[0].tiposInmuebles[posInm].dimesiones!![posDim].espacios!![item].tipo == "porcentaje"){
                var percent = data[0].tiposInmuebles[posInm].dimesiones!![posDim].espacios!![item].qty *
                        data[0].tiposInmuebles[posInm].dimesiones!![posDim].espacios!![item].valor!!.toDouble()
                var percent_aux = percent + 1
                total *= percent_aux
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
        llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
        rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
        flagVivienda = true
    }

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
            R.id.tvNext -> {
                var pos = viewPager.currentItem
                when (pos){
                    0 -> {
                        viewPager.currentItem = 1
                    }
                    1 -> {
                        viewPager.currentItem = 2
                    }
                    2 -> {
                        viewPager.currentItem = 3
                    }
                    3 -> {
                        viewPager.currentItem = 4
                    }
                    4 -> {
                        if (flagVivienda && flagDimensiones && flagEspacios && flagAddress && flagEspacios && flagTime){
                            var position = singleton.position
                            var arrayResumen = singleton.resumen
                            var serviceResumen = ServiceResumen()
                            serviceResumen.category = singleton.data[0].categoria
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
                            val intent = Intent(this, SolicitudServicio3 :: class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "Debe elegir todas las opciones para crear el servicio", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun setListeners() {
        imgBotonVolver.setOnClickListener(this)
        tvNext.setOnClickListener(this)
    }

    private fun agregarFragments(flag: Int): ArrayList<Fragment> {
        val fragments = ArrayList<Fragment>()
        when (flag){
            0 ->{
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

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

                    2 -> { //Espacios
                        radiogroup.check(R.id.radioButton3)
                        tvValor.visibility = View.VISIBLE
                        llProgress.layoutParams.width = 257
                        llProgress.requestLayout()
                        if (flagEspacios){
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
                    4 -> { //Time
                        radiogroup.check(R.id.radioButton5)
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
                    else -> {
                    }
                }//switch
            }//public void onPageSelected
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }



}
