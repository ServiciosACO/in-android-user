package co.kubo.indiesco.modelo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by estacion on 31/05/18.
 */
class InmuebleTipos {
    @SerializedName("id_tipo_inmueble")
    @Expose
    val idTipoInmueble: String? = null
    @SerializedName("inmueble")
    @Expose
    val inmueble: String? = null
    @SerializedName("imagen")
    @Expose
    val imagen: String? = null
    @SerializedName("dimesiones")
    @Expose
    val dimesiones: List<InmuebleDimensiones>? = null

    var active = false

}