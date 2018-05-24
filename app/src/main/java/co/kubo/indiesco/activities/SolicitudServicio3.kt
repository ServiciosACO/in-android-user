package co.kubo.indiesco.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import co.kubo.indiesco.R
import co.kubo.indiesco.interfaces.IListeners
import co.kubo.indiesco.modelo.CrearServicio
import kotlinx.android.synthetic.main.activity_solicitud_servicio3.*

class SolicitudServicio3 : AppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
            R.id.tvAddService -> {
                val intent = Intent (this, FechaServicio :: class.java)
                startActivity(intent)
            }
            R.id.tvValidateCoupon -> {
                //call endpoint for validate coupon
            }
            R.id.tvPayment -> {
                
            }
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    fun setListeners() {
        imgBotonVolver.setOnClickListener(this)
        tvAddService.setOnClickListener(this)
        tvValidateCoupon.setOnClickListener(this)
        tvPayment.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitud_servicio3)
        setListeners()
    }
}
