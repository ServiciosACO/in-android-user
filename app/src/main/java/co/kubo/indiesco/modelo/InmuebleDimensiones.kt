package co.kubo.indiesco.modelo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by estacion on 25/05/18.
 */
class InmuebleDimensiones {
    @SerializedName("id_dimension")
    @Expose
    val idDimension: String? = null
    @SerializedName("id_tipo_inmueble")
    @Expose
    val idTipoInmueble: String? = null
    @SerializedName("dimension")
    @Expose
    val dimension: String? = null
    @SerializedName("precio")
    @Expose
    val precio: String? = null
    @SerializedName("espacios")
    @Expose
    val espacios: List<InmuebleEspacios>? = null

    var checkDim = false
}