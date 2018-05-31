package co.kubo.indiesco.modelo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by estacion on 25/05/18.
 */
class InmuebleDimensiones {
    @SerializedName("id_dimension")
    @Expose
    private val idDimension: String? = null
    @SerializedName("id_tipo_inmueble")
    @Expose
    private val idTipoInmueble: String? = null
    @SerializedName("dimension")
    @Expose
    private val dimension: String? = null
    @SerializedName("precio")
    @Expose
    private val precio: String? = null
    @SerializedName("espacios")
    @Expose
    private val espacios: List<InmuebleEspacios>? = null
}