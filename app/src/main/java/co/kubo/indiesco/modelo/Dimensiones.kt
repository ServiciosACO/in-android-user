package co.kubo.indiesco.modelo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by estacion on 25/05/18.
 */
class Dimensiones {
    @SerializedName("id_precio_inmueble")
    @Expose
    val idPrecioInmueble: String? = null
    @SerializedName("id_tipo_inmueble")
    @Expose
    val idTipoInmueble: String? = null
    @SerializedName("dimension")
    @Expose
    val dimension: String? = null
    @SerializedName("precio")
    @Expose
    val precio: String? = null
    @SerializedName("accion")
    @Expose
    val accion: String? = null

}