package co.kubo.indiesco.modelo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by estacion on 6/06/18.
 */
class CodigoDescuento {
    @SerializedName("id_codigo_descuento")
    @Expose
    var id_codigo_descuento: Int? = null
    @SerializedName("valor_codigo")
    @Expose
    var valor_codigo: Int? = null
    @SerializedName("tipo_codigo")
    @Expose
    var tipo_codigo: Int? = null
}