package co.kubo.indiesco.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import co.kubo.indiesco.R
import co.kubo.indiesco.modelo.InmuebleVO
import co.kubo.indiesco.utils.Singleton

/**
 * Created by estacion on 28/05/18.
 */
class RoomsFragment : Fragment(), View.OnClickListener {

    val singleton = Singleton.getInstance()
    var inmuebles = ArrayList<InmuebleVO>()


    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.imgMinus1 -> {

            }
            R.id.imgPlus1 -> {

            }
            R.id.imgMinus2 -> {

            }
            R.id.imgPlus2 -> {

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_rooms, container, false)
        var imgMinus1 = v.findViewById<ImageView>(R.id.imgMinus1)
        var imgPlus1  = v.findViewById<ImageView>(R.id.imgPlus1)
        var imgMinus2 = v.findViewById<ImageView>(R.id.imgMinus2)
        var imgPlus2  = v.findViewById<ImageView>(R.id.imgPlus2)
        var tvCommunRoom = v.findViewById<ImageView>(R.id.tvCommunRoom)
        var tvPpalRoom = v.findViewById<ImageView>(R.id.tvPpalRoom)

        imgMinus1.setOnClickListener(this)
        imgPlus1.setOnClickListener(this)
        imgMinus2.setOnClickListener(this)
        imgPlus2.setOnClickListener(this)

        inmuebles = singleton.data

        return v
    }

}