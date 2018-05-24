package co.kubo.indiesco.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import co.kubo.indiesco.R
import co.kubo.indiesco.utils.SharedPreferenceManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_excedentes.*

class Excedentes : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excedentes)

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

    }
}
