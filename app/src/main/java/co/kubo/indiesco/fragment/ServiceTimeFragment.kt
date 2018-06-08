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

/**
 * Created by estacion on 28/05/18.
 */
class ServiceTimeFragment : Fragment(), View.OnClickListener {

    val singleton = Singleton.getInstance()
    lateinit var imgTime : ImageView
    lateinit var timePicker : TimePicker
    lateinit var toggleButton : ToggleButton
    var AM_PM = "AM"

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_service_time, container, false)
        val iTime = (activity as? ITime)!!

        singleton.urgente = "no"
        imgTime = v.findViewById(R.id.imgTime)
        timePicker = v.findViewById(R.id.timePicker)
        toggleButton = v.findViewById(R.id.toggleButton)
        toggleButton.setOnClickListener(this)

        var time = "${timePicker.currentHour.toString()}:${timePicker.currentMinute.toString()}"
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
            var time = "${hourOfDay.toString()}:${minute.toString()}"
            singleton.hora = time
        }
        return v
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            val iTime = (activity as? ITime)!!
            iTime.checkTime()
        } else {
            Log.e("fragment", "No visible")
        }
    }

}

interface ITime{
    fun checkTime()
}