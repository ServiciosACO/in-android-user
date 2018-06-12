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
import co.kubo.indiesco.adaptadores.IVivieda
import co.kubo.indiesco.adaptadores.PageAdapter
import co.kubo.indiesco.fragment.*
import co.kubo.indiesco.modelo.Espacios
import co.kubo.indiesco.modelo.ServiceResumen
import co.kubo.indiesco.utils.Singleton
import kotlinx.android.synthetic.main.activity_add_service2.*
import java.text.DecimalFormat

class AddService2 : AppCompatActivity(), View.OnClickListener, IAddress, ITime, INpisos, IVivieda {

    val df = DecimalFormat("$###,###")
    val singleton = Singleton.getInstance()

    var flagNpisos = false
    var flagAddress = false
    var flagTime = false
    var flagVivienda = false
    var totalCost = 0.0

    var flag1 = false
    var flag2 = false

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
                        if (flagVivienda && flagNpisos && flagAddress && flagTime){
                            var position = singleton.position
                            var arrayResumen = singleton.resumen
                            var serviceResumen = ServiceResumen()
                            serviceResumen.category = singleton.categoria
                            serviceResumen.date = singleton.fecha
                            serviceResumen.address = singleton.direccion
                            serviceResumen.id_direccion = singleton.idDir
                            serviceResumen.dimension = ""
                            serviceResumen.id_dimension = ""
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
        fragments.add(TipoViviendaFragment())
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
                    0 -> { //Tipo de inmueble
                        radiogroup.check(R.id.radioButton)
                        tvValor.visibility = View.INVISIBLE
                        llProgress.layoutParams.width = 100
                        llProgress.requestLayout()
                        if (flagVivienda){
                            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
                            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
                        } else {
                            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
                            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
                        }
                    }
                    1 -> { //Numero de pisos
                        radiogroup.check(R.id.radioButton2)
                        tvValor.visibility = View.VISIBLE
                        llProgress.layoutParams.width = 220
                        llProgress.requestLayout()
                        if (flagNpisos){
                            llProgress.setBackgroundColor(resources.getColor(R.color.colorVerde))
                            rlValor.setBackgroundColor(resources.getColor(R.color.colorVerde_80))
                        } else {
                            llProgress.setBackgroundColor(resources.getColor(R.color.color_hint))
                            rlValor.setBackgroundColor(resources.getColor(R.color.color_hint_80))
                        }
                    }
                    2 -> { //Direccion
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
                    3 -> { //Tiempo
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
