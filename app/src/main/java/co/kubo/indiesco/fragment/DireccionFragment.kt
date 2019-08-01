package co.kubo.indiesco.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.kubo.indiesco.R

/**
 * Created by estacion on 28/05/18.
 */
class DireccionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_direccion, container, false)

        return v
    }

}