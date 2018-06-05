package co.kubo.indiesco.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import co.kubo.indiesco.R
import co.kubo.indiesco.utils.Utils
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_fecha_servicio.*

class FechaServicio : AppCompatActivity(), View.OnClickListener, OnDateSelectedListener {

    var flag = 0
    val utils = Utils()
    var dateStr = ""

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.llNext -> {
                if (validation()){
                    val intent = Intent (this, TipoInmueble :: class.java)
                    startActivity(intent)
                }
            }
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
        }
    }

    private fun validation(): Boolean {
        if (!utils.checkInternetConnection(this@FechaServicio, true)){
            return false
        }
        if (flag == 0) {
            Toast.makeText(applicationContext, "Debe elegir una fecha", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    fun setListeners() {
        llNext.setOnClickListener(this)
        imgBotonVolver.setOnClickListener(this)
        calendarView.setOnDateChangedListener(this)
    }

    override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {

        var dateStr = getSelectedDatesString()

        Toast.makeText(applicationContext, dateStr, Toast.LENGTH_LONG).show()
        llNext.setBackgroundColor(resources.getColor(R.color.colorVerde))
        flag = 1
    }

    private fun getSelectedDatesString(): String{
        val date = calendarView.selectedDate
        if (date == null){
            dateStr = ""
        } else {
            dateStr = utils.DateToString3(calendarView.selectedDate.date).replace(" ", "-")
        }
        return dateStr
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fecha_servicio)
        setListeners()
    }
}
