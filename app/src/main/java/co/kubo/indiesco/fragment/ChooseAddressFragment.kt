package co.kubo.indiesco.fragment

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.IAddress
import co.kubo.indiesco.adaptadores.MisDireccionesAdapter
import co.kubo.indiesco.adaptadores.MisDireccionesAdapter2
import co.kubo.indiesco.dialog.DialogProgress
import co.kubo.indiesco.modelo.Direccion
import co.kubo.indiesco.modelo.Usuario
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter
import co.kubo.indiesco.restAPI.modelo.ResponseDireccion
import co.kubo.indiesco.utils.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

/**
 * Created by estacion on 28/05/18.
 */
class ChooseAddressFragment : Fragment(), View.OnClickListener {

    var TAG = "ChooseAddressFragment"
    var rvAddress : RecyclerView ?= null
    private var direccion: ArrayList<Direccion>? = null
    private var dialogProgress : DialogProgress ?= null
    lateinit var llm : LinearLayoutManager
    lateinit var adapter : MisDireccionesAdapter2
    lateinit var iAddress : IAddress

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.fabAgregar -> {

            }
        }
    }

    fun obtenerDirecciones() {
        dialogProgress = DialogProgress(activity)
        dialogProgress!!.show()
        val authToken = SharedPreferenceManager.getAuthToken(context)
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        var usuario = Usuario()
        usuario = SharedPreferenceManager.getInfoUsuario(context)
        val responseDireccionCall = endpoints.listarDireccion(authToken, usuario.id_user)
        responseDireccionCall.enqueue(object : Callback<ResponseDireccion> {
            override fun onResponse(call: Call<ResponseDireccion>, response: Response<ResponseDireccion>) {
                if (dialogProgress!!.isShowing) {
                    dialogProgress!!.dismiss()
                }
                val code = response.body()!!.code
                when (code) {
                    "100" -> {
                        direccion = response.body()!!.data
                        adapter = MisDireccionesAdapter2(direccion, activity, iAddress)
                        rvAddress!!.adapter = adapter
                    }
                    "102" -> {
                        Toast.makeText(context, "Algo fallo intente de nuevo", Toast.LENGTH_LONG).show()
                        Log.e(TAG, "Cod: 102 No hay datos")
                    }
                    "120" -> Log.e(TAG, "Cod: 120 auth token invalido")
                }//switch
            }

            override fun onFailure(call: Call<ResponseDireccion>, t: Throwable) {
                if (dialogProgress!!.isShowing) {
                    dialogProgress!!.dismiss()
                }
                Log.e(TAG, "obtener direcciones onFailure")
            }
        })
    }

    private fun hideKeyboard() {
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_choose_address, container, false)
        iAddress = (activity as? IAddress)!!

        var fabAgregar = v.findViewById<FloatingActionButton>(R.id.fabAgregar)
        rvAddress = v.findViewById<RecyclerView>(R.id.rvAddress)
        fabAgregar.setOnClickListener(this)

        llm = LinearLayoutManager(activity)
        rvAddress!!.layoutManager = llm

        obtenerDirecciones()
        return v
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            hideKeyboard()
        } else {
            Log.e("fragment", "No visible")
        }
    }

}