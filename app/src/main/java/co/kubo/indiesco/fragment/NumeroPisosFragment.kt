package co.kubo.indiesco.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import co.kubo.indiesco.R

/**
 * Created by estacion on 31/05/18.
 */
class NumeroPisosFragment : Fragment() {

    lateinit var iNpisos: INpisos
    var nPisos = ""

    private fun hideKeyboard() {
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_numero_pisos, container, false)
        iNpisos = (activity as? INpisos)!!
        var editQtyPisos = v.findViewById<EditText>(R.id.editQtyPisos)
        editQtyPisos.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if(count > 0){
                    nPisos = editQtyPisos.text.toString()
                    iNpisos.checkNPisos(true)
                } else {
                    nPisos = editQtyPisos.text.toString()
                    iNpisos.checkNPisos(false)
                }
            }
        })

        hideKeyboard()
        return v
    }

}

interface INpisos{
    fun checkNPisos(b: Boolean)
}