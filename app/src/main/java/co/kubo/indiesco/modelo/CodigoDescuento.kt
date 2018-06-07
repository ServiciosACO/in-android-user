package co.kubo.indiesco.modelo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by estacion on 6/06/18.
 */
class CodigoDescuento {
    @SerializedName("id_codigo_descuento")
    @Expose
    val idCodigoDescuento: String? = null
    @SerializedName("tipo_codigo")
    @Expose
    val tipoCodigo: String? = null
    @SerializedName("valor")
    @Expose
    val valor: String? = null
}