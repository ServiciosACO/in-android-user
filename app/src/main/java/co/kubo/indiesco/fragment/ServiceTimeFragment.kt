package co.kubo.indiesco.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.ToggleButton
import co.kubo.indiesco.R
import co.kubo.indiesco.utils.Singleton
import android.widget.NumberPicker
import android.annotation.SuppressLint

/**
 * Created by estacion on 28/05/18.
 */
class ServiceTimeFragment : Fragment(), View.OnClickListener {

    val singleton = Singleton.getInstance()
    lateinit var imgTime : ImageView
    lateinit var timePicker : TimePicker
    lateinit var toggleButton : ToggleButton
    var AM_PM = "AM"
    val TIME_PICKER_INTERVAL = 30

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.toggleButton -> {
                if (toggleButton.isChecked){
                    toggleButton.isChecked = true
                    singleton.urgente = "si"
                }  else {
                    toggleButton.isChecked = false
                    singleton.urgente = "no"
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private fun setTimePickerInterval(timePicker: TimePicker) {
        try {
            val classForid = Class.forName("com.android.internal.R\$id")
            // Field timePickerField = classForid.getField("timePicker");

            val field = classForid.getField("minute")
            val minutePicker = timePicker.findViewById(field.getInt(null)) as NumberPicker

            val fieldHour = classForid.getField("hour")
            val hourPicker = timePicker.findViewById(fieldHour.getInt(null)) as NumberPicker

            minutePicker.minValue = 0
            minutePicker.maxValue = 3
            var displayedValues = ArrayList<String>()
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_service_time, container, false)
        val iTime = (activity as? ITime)!!

        singleton.urgente = "no"
        imgTime = v.findViewById(R.id.imgTime)
        timePicker = v.findViewById(R.id.timePicker)
        toggleButton = v.findViewById(R.id.toggleButton)
        toggleButton.setOnClickListener(this)

        timePicker.setIs24HourView(true)
        setTimePickerInterval(timePicker)

        var minute = timePicker.currentMinute.toString().length
        var time = ""
        when(minute){
            1 -> {
                time = "${timePicker.currentHour}:0${timePicker.currentMinute}"
            }
            2 -> {
                time = "${timePicker.currentHour}:${timePicker.currentMinute}"
            }
        }
        singleton.hora = time
        if (timePicker.currentHour < 12){
            AM_PM = "AM"
            imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_day))
        } else {
            AM_PM = "PM"
            imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_afternoon))
        }

        timePicker.setOnTimeChangedListener {
            view, hourOfDay, minute ->
            if (hourOfDay < 12){
                AM_PM = "AM"
                imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_day))
                singleton.idDimension
            } else {
                AM_PM = "PM"
                imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_afternoon))
            }

            var minute = timePicker.currentMinute.toString().length
            var time = ""
            when(minute){
                1 -> {
                    time = "${timePicker.currentHour}:0${timePicker.currentMinute}"
                }
                2 -> {
                    time = "${timePicker.currentHour}:${timePicker.currentMinute}"
                }
            }
            singleton.hora = time

        }
        return v
    }



    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            val iTime = (activity as? ITime)!!
            var minute = timePicker.currentMinute.toString().length
            var time = ""
            when(minute){
                1 -> {
                    time = "${timePicker.currentHour}:0${timePicker.currentMinute}"
                }
                2 -> {
                    time = "${timePicker.currentHour}:${timePicker.currentMinute}"
                }
            }
            singleton.hora = time
            iTime.checkTime()

        } else {
            Log.e("fragment", "No visible")
        }
    }

}

interface ITime{
    fun checkTime()
}