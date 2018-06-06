package co.kubo.indiesco.restAPI.modelo

import co.kubo.indiesco.modelo.CodigoDescuento
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by estacion on 6/06/18.
 */
class ResponseCodigoDescuento {
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("data")
    @Expose
    var data: ArrayList<CodigoDescuento>? = null
}