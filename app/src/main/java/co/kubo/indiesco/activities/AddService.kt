package co.kubo.indiesco.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.PageAdapter
import co.kubo.indiesco.fragment.*
import kotlinx.android.synthetic.main.activity_add_service.*
import java.util.ArrayList

class AddService : AppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.imgBotonVolver -> {

            }
        }
    }

    private fun setListeners() {
        imgBotonVolver.setOnClickListener(this)
    }

    private fun agregarFragments(): ArrayList<Fragment> {
        val fragments = ArrayList<Fragment>()
        fragments.add(TipoViviendaFragment())
        fragments.add(DimensionesFragment())
        fragments.add(RoomsFragment())
        fragments.add(EspaciosPrincipalesFragment())
        fragments.add(EspaciosSecuendariosFragment())
        fragments.add(ChooseAddressFragment())
        fragments.add(DireccionFragment())
        fragments.add(ServiceTimeFragment())
        return fragments
    }

    private fun setUpViewPager() {
        viewPager.adapter = PageAdapter(supportFragmentManager, agregarFragments())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)
        setUpViewPager()
        setListeners()

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> radiogroup.check(R.id.radioButton)
                    1 -> radiogroup.check(R.id.radioButton2)
                    2,5 -> radiogroup.check(R.id.radioButton3)
                    6 -> radiogroup.check(R.id.radioButton4)
                    7 -> radiogroup.check(R.id.radioButton5)
                    else -> {
                    }
                }//switch
            }//public void onPageSelected
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }



}
