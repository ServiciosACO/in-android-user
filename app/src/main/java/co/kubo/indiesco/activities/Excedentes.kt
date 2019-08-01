package co.kubo.indiesco.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import co.kubo.indiesco.R
import co.kubo.indiesco.adaptadores.AdapterRecargos
import co.kubo.indiesco.dialog.DialogProgress
import co.kubo.indiesco.modelo.RecargoVO
import co.kubo.indiesco.modelo.Usuario
import co.kubo.indiesco.restAPI.adapter.RestApiAdapter
import co.kubo.indiesco.restAPI.modelo.ResponseRecargo
import co.kubo.indiesco.utils.SharedPreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_excedentes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class Excedentes : AppCompatActivity(), View.OnClickListener {

    val df = DecimalFormat("$###,###")
    lateinit var dialogProgress: DialogProgress
    var data = ArrayList<RecargoVO>()
    lateinit var adapter : AdapterRecargos
    lateinit var llm : LinearLayoutManager

    private fun getRecargos(){
        dialogProgress = DialogProgress(this)
        dialogProgress!!.show()
        val authToken = SharedPreferenceManager.getAuthToken(this)
        val restApiAdapter = RestApiAdapter()
        val endpoints = restApiAdapter.establecerConexionRestApiSinGson()
        var usuario = Usuario()
        usuario = SharedPreferenceManager.getInfoUsuario(this)
        val responseRecargo : Call<ResponseRecargo> = endpoints.getRecargos(authToken, usuario.id_user, 0)
        responseRecargo.enqueue(object : Callback<ResponseRecargo>{
            override fun onFailure(call: Call<ResponseRecargo>?, t: Throwable?) {
                if (dialogProgress.isShowing)
                    dialogProgress.dismiss()
            }

            override fun onResponse(call: Call<ResponseRecargo>?, response: Response<ResponseRecargo>?) {
                if (dialogProgress.isShowing)
                    dialogProgress.dismiss()
                if (response!!.isSuccessful){
                    when(response.body()!!.code){
                        "100" -> {
                            data = response.body()!!.data!!
                            if (data.isEmpty()){
                                llNoRecharge.visibility = View.VISIBLE
                                rvRecargo.visibility = View.GONE
                            } else{
                                llNoRecharge.visibility = View.GONE
                                rvRecargo.visibility = View.VISIBLE
                                adapter = AdapterRecargos(data, this@Excedentes)
                                rvRecargo.adapter = adapter
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.tvAddServiceRecharge -> {
                val intent = Intent(this, Excedentes2 :: class.java)
                startActivity(intent)
            }
            R.id.imgBotonVolver -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun setListeners() {
        tvAddServiceRecharge.setOnClickListener(this)
        imgBotonVolver.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excedentes)
        setListeners()

        var usuario = SharedPreferenceManager.getInfoUsuario(applicationContext)
        tvNombrePerfil.text = usuario.name
        tvTelf.text = "Teléfono: ${usuario.celular}"
        Picasso
                .with(applicationContext)
                .load(usuario.foto)
                .placeholder(resources.getDrawable(R.drawable.registro_foto))
                .error(resources.getDrawable(R.drawable.registro_foto))
                .transform(CircleTransform())
                .into(imgFotoPerfil)

        llm = LinearLayoutManager(this)
        rvRecargo!!.layoutManager = llm
        getRecargos()
    }
}
