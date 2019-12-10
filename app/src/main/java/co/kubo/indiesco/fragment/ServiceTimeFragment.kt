package co.kubo.indiesco.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.kubo.indiesco.R
import co.kubo.indiesco.utils.Singleton
import android.annotation.SuppressLint
import android.widget.*
import androidx.fragment.app.Fragment
import co.kubo.indiesco.utils.DateUtil
import co.kubo.indiesco.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by estacion on 28/05/18.
 */
class ServiceTimeFragment : Fragment(), View.OnClickListener {

    val singleton = Singleton.getInstance()
    lateinit var imgTime: ImageView
    lateinit var timePicker: TimePicker
    lateinit var toggleButton: ToggleButton
    lateinit var llUrgentService: LinearLayout
    lateinit var tvInfo: TextView
    var AM_PM = "AM"
    val TIME_PICKER_INTERVAL = 30
    lateinit var iTime: ITime
    var time = ""
    private var hourBegin: Int = 6
    private var minuteBegin: Int = 0

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_service_time, container, false)
        iTime = (activity as? ITime)!!

        singleton.urgente = "no"
        imgTime = v.findViewById(R.id.imgTime)
        timePicker = v.findViewById(R.id.timePicker)
        toggleButton = v.findViewById(R.id.toggleButton)
        toggleButton.setOnClickListener(this)
        llUrgentService = v.findViewById(R.id.llUrgentService)
        tvInfo = v.findViewById(R.id.tvInfo)
        hourBegin = if (!singleton.isBandTodayService)
            6
        else
            singleton.requestCalendarService.get(Calendar.HOUR_OF_DAY) + DateUtil.HOURS_BEFORE_SERVICE

        minuteBegin = if (!singleton.isBandTodayService) {
            0
        } else {
            if (singleton.requestCalendarService.get(Calendar.MINUTE) == 30) {
                30
            } else {
                0
            }
        }

        /*
        val df = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = df.format(Calendar.getInstance().time)

        if (currentDate == singleton.fecha) {
            val format = SimpleDateFormat("HH:mm") //this is format in military time
            val currentTime = format.format(Calendar.getInstance().time) //get current time
            val currentTimetime = format.parse(currentTime) as Date //convert string time in Date time
            val currentTimeStr = format.format(currentTimetime).split(":")

            if (currentTimeStr[0].toInt() >= 16) {
                llUrgentService.visibility = View.GONE
                tvInfo.visibility = View.GONE
            } else {
                llUrgentService.visibility = View.VISIBLE
                tvInfo.visibility = View.VISIBLE
            }
        } else {
            llUrgentService.visibility = View.GONE
            tvInfo.visibility = View.GONE
        }
        */
        llUrgentService.visibility = View.VISIBLE
        tvInfo.visibility = View.VISIBLE
        toggleButton.isEnabled = false

        setTimePickerInterval(timePicker)

        timePicker.currentHour = hourBegin
        timePicker.currentMinute = if (!singleton.isBandTodayService) {
            0
        } else {
            if (singleton.requestCalendarService.get(Calendar.MINUTE) == 30) {
                1
            } else {
                0
            }
        }

        val minute = timePicker.currentMinute.toString().length
        when (minute) {
            1 -> {
                time = "${timePicker.currentHour}:0${timePicker.currentMinute}"
            }
            2 -> {
                time = "${timePicker.currentHour}:${timePicker.currentMinute}"
            }
        }
        singleton.hora = time
        if (timePicker.currentHour < 12) {
            AM_PM = "AM"
            imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_day))
        } else {
            AM_PM = "PM"
            imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_afternoon))
        }

        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            /*
            if (hourOfDay < 12) {
                AM_PM = "AM"
                imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_day))
                singleton.idDimension
            } else {
                AM_PM = "PM"
                imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_afternoon))
            }

            */

            validateUrgentSertvice(hourOfDay, minute)

            if (hourOfDay == hourBegin) {
                timePicker.currentHour = hourBegin
                timePicker.currentMinute = if (minuteBegin == 30) {
                    1
                } else {
                    0
                }
                if (hourOfDay < 12) {
                    AM_PM = "AM"
                    imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_day))
                } else {
                    AM_PM = "PM"
                    imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_afternoon))
                }
            } else if (hourOfDay in hourBegin..11) {
                AM_PM = "AM"
                imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_day))
                singleton.idDimension
            } else if (hourOfDay in 12..DateUtil.HOUR_LIMIT_SERVICE) {
                if (hourOfDay == DateUtil.HOUR_LIMIT_SERVICE && minute > 0) {
                    timePicker.currentHour = DateUtil.HOUR_LIMIT_SERVICE
                    timePicker.currentMinute = 0
                    AM_PM = "PM"
                    imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_afternoon))
                } else {
                    AM_PM = "PM"
                    imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_afternoon))
                }
            } else if (hourOfDay > DateUtil.HOUR_LIMIT_SERVICE) {
                timePicker.currentHour = hourBegin
                timePicker.currentMinute = if (minuteBegin == 30) {
                    1
                } else {
                    0
                }
                AM_PM = "AM"
                imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_day))
                singleton.idDimension
            } else if (hourOfDay < hourBegin) {
                timePicker.currentHour = DateUtil.HOUR_LIMIT_SERVICE
                timePicker.currentMinute = 0
                AM_PM = "PM"
                imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_afternoon))
            }

            var minute = timePicker.currentMinute.toString()
            var time = ""
            when (minute) {
                "0" -> {
                    time = "${timePicker.currentHour}:00"
                }
                "1" -> {
                    //time = "${timePicker.currentHour}:${timePicker.currentMinute}"
                    time = "${timePicker.currentHour}:30"
                }
                "2" -> {
                    time = "${timePicker.currentHour}:00"
                }
                "3" -> {
                    time = "${timePicker.currentHour}:30"
                }
            }
            singleton.hora = time
        }
        return v
    }

    private fun validateUrgentSertvice(hourOfDay: Int, minute: Int) {
        val calendarRequestService = singleton.requestCalendarService
        calendarRequestService.set(Calendar.HOUR_OF_DAY, calendarRequestService.get(Calendar.HOUR_OF_DAY) + DateUtil.HOURS_BEFORE_SERVICE)
        calendarRequestService.set(Calendar.SECOND, 0)
        calendarRequestService.set(Calendar.MILLISECOND, 0)

        val compareMinutes = if (minute == 0 || minute == 2) {
            0
        } else {
            30
        }

        val compareCalendar = Calendar.getInstance()
        compareCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        compareCalendar.set(Calendar.DAY_OF_YEAR, calendarRequestService.get(Calendar.DAY_OF_YEAR))
        compareCalendar.set(Calendar.MINUTE, compareMinutes)
        compareCalendar.set(Calendar.SECOND, 0)
        compareCalendar.set(Calendar.MILLISECOND, 0)

        val difference = DateUtil.calculateDifferenceBetweenToDated(calendarRequestService.time, compareCalendar.time)
        val differenceToHours = DateUtil.convertMillisecondsToHoursDouble(difference)
        toggleButton.isChecked = difference <= 12600000L
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.toggleButton -> {
                if (toggleButton.isChecked) {
                    toggleButton.isChecked = true
                    singleton.urgente = "si"

                    val format = SimpleDateFormat("HH:mm") //this is format in military time
                    val currentTime = format.format(Calendar.getInstance().time) //get current time
                    var currentTimetime = format.parse(currentTime) as Date //convert string time in Date time
                    singleton.hora = format.format(currentTimetime)

                    timePicker.isEnabled = false

                } else {
                    toggleButton.isChecked = false
                    singleton.urgente = "no"
                    timePicker.isEnabled = true
                }
                iTime.checkTime()
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            val iTime = (activity as? ITime)!!
            setTimePickerInterval(timePicker)
            var minute = timePicker.currentMinute.toString()
            var time = ""
            when (minute) {
                "0" -> {
                    time = "${timePicker.currentHour}:00"
                }
                "1" -> {
                    //time = "${timePicker.currentHour}:${timePicker.currentMinute}"
                    time = "${timePicker.currentHour}:30"
                }
                "2" -> {
                    time = "${timePicker.currentHour}:00"
                }
                "3" -> {
                    time = "${timePicker.currentHour}:30"
                }
            }
            singleton.hora = time
            iTime.checkTime()

        } else {
            Log.e("fragment", "No visible")
        }
    }

    @SuppressLint("NewApi")
    private fun setTimePickerInterval(timePicker: TimePicker) {
        try {
            val classForid = Class.forName("com.android.internal.R\$id")
            // Field timePickerField = classForid.getField("timePicker");

            val field = classForid.getField("minute")
            val minutePicker = timePicker.findViewById(field.getInt(null)) as NumberPicker

            /*
            val fieldHour = classForid.getField("hour")
            val hourPicker = timePicker.findViewById(fieldHour.getInt(null)) as NumberPicker

            hourPicker.minValue = 6
            hourPicker.maxValue = 16
            */

            minutePicker.minValue = 0
            minutePicker.maxValue = 3
            val displayedValues = ArrayList<String>()
            run {
                var i = 0
                while (i < 60) {
                    displayedValues.add(String.format("%02d", i))
                    i += TIME_PICKER_INTERVAL
                }
            }
            var i = 0
            while (i < 60) {
                displayedValues.add(String.format("%02d", i))
                i += TIME_PICKER_INTERVAL
            }
            minutePicker.displayedValues = displayedValues.toArray(arrayOfNulls<String>(0))
            //hourPicker.displayedValues = getRanksHour().toArray(arrayOfNulls<String>(0))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

interface ITime {
    fun checkTime()
}