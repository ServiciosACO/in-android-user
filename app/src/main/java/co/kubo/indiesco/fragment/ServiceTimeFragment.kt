package co.kubo.indiesco.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.ToggleButton
import co.kubo.indiesco.R

/**
 * Created by estacion on 28/05/18.
 */
class ServiceTimeFragment : Fragment(), View.OnClickListener {

    lateinit var imgTime : ImageView
    lateinit var timePicker : TimePicker
    lateinit var toggleButton : ToggleButton
    var AM_PM = "AM"

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.toggleButton -> {
                toggleButton.isChecked = toggleButton.isChecked
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_service_time, container, false)

        imgTime = v.findViewById(R.id.imgTime)
        timePicker = v.findViewById(R.id.timePicker)
        toggleButton = v.findViewById(R.id.toggleButton)
        toggleButton.setOnClickListener(this)

        if (timePicker.currentHour < 12){
            AM_PM = "AM"
            imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_day))
        } else {
            AM_PM = "PM"
            imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_afternoon))
        }

        timePicker.setOnTimeChangedListener {
            p0, p1, p2 ->
            if (p1 < 12){
                AM_PM = "AM"
                imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_day))
            } else {
                AM_PM = "PM"
                imgTime.setImageDrawable(activity!!.resources.getDrawable(R.drawable.img_afternoon))
            }
        }

        return v
    }

}