package co.kubo.indiesco.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import co.kubo.indiesco.R
import co.kubo.indiesco.modelo.InmuebleVO
import co.kubo.indiesco.utils.Singleton
import kotlinx.android.synthetic.main.fragment_numero_pisos.*

/**
 * Created by estacion on 31/05/18.
 */
class NumeroPisosFragment : Fragment() {

    lateinit var iNpisos: INpisos
    var nMetros = ""
    var singleton = Singleton.getInstance()
    var inmuebles = ArrayList<InmuebleVO>()
    var min = 0
    var max = 0
    var range = "0-0"

    private fun hideKeyboard() {
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_numero_pisos, container, false)
        iNpisos = (activity as? INpisos)!!
        var editQtyMetros = v.findViewById<EditText>(R.id.editQtyMetros)
        inmuebles = singleton.data
        try {
            range = inmuebles[singleton.posCat.toInt()].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].rangos!!
        } catch (e : Exception){
            range = "0-0"
        }

        var splitRange = range!!.split("-")
        min = splitRange[0].toInt()
        max = splitRange[1].toInt()
        editQtyMetros.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if(text!!.isNotEmpty()){
                    if (text.toString().toInt() in min..max){
                        nMetros = editQtyMetros.text.toString()
                        singleton.setnMetros(nMetros)
                        iNpisos.checkNPisos(true)
                    } else {
                        if (text.length >= 2){
                            Toast.makeText(activity, "El valor ingresado no está dentro del rango de dimensiones elegido", Toast.LENGTH_SHORT).show()
                            singleton.setnMetros("0")
                            iNpisos.checkNPisos(false)
                        }
                    }
                } else {
                    nMetros = editQtyMetros.text.toString()
                    singleton.setnMetros("0")
                    iNpisos.checkNPisos(false)
                }
            }
        })
        hideKeyboard()
        return v
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            inmuebles = singleton.data
            try {
                range = inmuebles[singleton.posCat.toInt()].tiposInmuebles[singleton.posTipoInmueble.toInt()].dimesiones!![singleton.posDimension.toInt()].rangos!!
            } catch (e : Exception){
                range = "0-0"
            }
            var splitRange = range!!.split("-")
            min = splitRange[0].toInt()
            max = splitRange[1].toInt()
            if (editQtyMetros.text.toString().isNotEmpty()) {
                if (editQtyMetros.text.toString().toInt() in min..max) {
                    nMetros = editQtyMetros.text.toString()
                    singleton.setnMetros(nMetros)
                    iNpisos.checkNPisos(true)
                } else {
                    if (editQtyMetros.text.toString().length >= 2) {
                        Toast.makeText(activity, "El valor ingresado no está dentro del rango de dimensiones elegido", Toast.LENGTH_SHORT).show()
                        nMetros = editQtyMetros.text.toString()
                        singleton.setnMetros("0")
                        iNpisos.checkNPisos(false)
                    }
                }
            } else {
                nMetros = editQtyMetros.text.toString()
                singleton.setnMetros("0")
                iNpisos.checkNPisos(false)
            }
        } else {
            Log.e("fragmentNPisos", "No visible")
        }
    }

}

interface INpisos{
    fun checkNPisos(b: Boolean)
}