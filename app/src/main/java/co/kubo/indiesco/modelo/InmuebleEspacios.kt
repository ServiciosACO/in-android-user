package co.kubo.indiesco.modelo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by estacion on 31/05/18.
 */
class InmuebleEspacios {
    @SerializedName("espacio")
    @Expose
    val espacio: String? = null
    @SerializedName("tipo")
    @Expose
    val tipo: String? = null
    @SerializedName("valor")
    @Expose
    val valor: String? = null
}