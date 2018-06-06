package co.kubo.indiesco.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.LinearLayout
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.IAddress
import co.kubo.indiesco.adaptadores.IDimension
import co.kubo.indiesco.adaptadores.PageAdapter
import co.kubo.indiesco.fragment.*
import kotlinx.android.synthetic.main.activity_add_service.*
import java.util.ArrayList
import co.kubo.indiesco.adaptadores.IVivieda


class AddService : AppCompatActivity(), View.OnClickListener, IVivieda, IDimension, IAddress {

    var flagVivienda = false
    var flagDimensiones = false
    var flagEspacios = false
    var flagAddress = false
    var flagTime = false

    var flag1 = false
    var flag2 = false

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
                        //Ir a SolicitudServicio3
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
