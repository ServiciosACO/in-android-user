package co.kubo.indiesco.modelo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by estacion on 6/06/18.
 */
class RecargoVO {
    @SerializedName("id_recargo")
    @Expose
    var id_recargo: Int? = null
    @SerializedName("fecha")
    @Expose
    var fecha: String? = null
    @SerializedName("valor")
    @Expose
    var valor: String? = null
    @SerializedName("mts")
    @Expose
    var mts: String? = null
    @SerializedName("estado")
    @Expose
    var estado: String? = null
}