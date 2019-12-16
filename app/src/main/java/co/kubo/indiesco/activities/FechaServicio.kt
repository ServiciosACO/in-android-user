package co.kubo.indiesco.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.kubo.indiesco.R
import co.kubo.indiesco.utils.DateUtil
import co.kubo.indiesco.utils.Singleton
import co.kubo.indiesco.utils.Utils
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_fecha_servicio.*
import java.util.*


class FechaServicio : AppCompatActivity(), View.OnClickListener, OnDateSelectedListener {

    var flag = 0
    val utils = Utils()
    var dateStr = ""
    var singleton = Singleton.getInstance()

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.llNext -> {
                if (validation()) {
                    val intent = Intent(this, TipoInmueble::class.java)
                    startActivity(intent)
                }
            }
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
        }
    }

    private fun validation(): Boolean {
        if (!utils.checkInternetConnection(this@FechaServicio, true)) {
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
        //Toast.makeText(applicationContext, dateStr, Toast.LENGTH_LONG).show()
        val calendarSelected = date.calendar
        singleton.isBandTodayService = calendarSelected.get(Calendar.DAY_OF_YEAR) == DateUtil.roundedCurrentDate().get(Calendar.DAY_OF_YEAR)
        singleton.requestCalendarService.set(Calendar.DAY_OF_MONTH, calendarSelected.get(Calendar.DAY_OF_MONTH))
        singleton.fecha = dateStr
        llNext.setBackgroundColor(resources.getColor(R.color.colorVerde))
        flag = 1
    }

    private fun getSelectedDatesString(): String {
        val date = calendarView.selectedDate
        if (date == null) {
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
        var currentDate = Calendar.getInstance().time
        var currentDateStr = utils.DateToString3(currentDate).replace(" ", "-")
        var splitDate = currentDateStr.split("-")
        val validate = validateToday()
        if (validate) {
            calendarView.state().edit()
                    .setMinimumDate(CalendarDay.from(splitDate[0].toInt(), splitDate[1].toInt() - 1, splitDate[2].toInt()))
                    .commit()
        } else {
            calendarView.state().edit()
                    .setMinimumDate(CalendarDay.from(splitDate[0].toInt(), splitDate[1].toInt() - 1, splitDate[2].toInt() + 1))
                    .commit()
        }

    }

    private fun validateToday(): Boolean {
        val limitCalendar: Calendar = DateUtil.convertLimitToCalendar(DateUtil.HOUR_LIMIT)
        val currentCalendar: Calendar = DateUtil.roundedCurrentDate()
        return if (currentCalendar.before(limitCalendar)) {
            val difference: Long = DateUtil.calculateDifferenceBetweenToDated(currentCalendar.time, limitCalendar.time)
            val longDifference: Int = DateUtil.convertMillisecondsToHours(difference)
            if (longDifference >= DateUtil.HOURS_BEFORE_SERVICE) {
                singleton.requestCalendarService = currentCalendar
                singleton.hourMinimunService = "${(currentCalendar.get(Calendar.HOUR_OF_DAY) + 1)}:${currentCalendar.get(Calendar.MINUTE)}"
                true
            } else {
                singleton.requestCalendarService = currentCalendar
                singleton.hourMinimunService = "${(currentCalendar.get(Calendar.HOUR_OF_DAY) + 1)}:${currentCalendar.get(Calendar.MINUTE)}"
                false
            }
        } else {
            singleton.requestCalendarService = currentCalendar
            singleton.hourMinimunService = "${(currentCalendar.get(Calendar.HOUR_OF_DAY) + 1)}:${currentCalendar.get(Calendar.MINUTE)}"
            false
        }
    }
}
