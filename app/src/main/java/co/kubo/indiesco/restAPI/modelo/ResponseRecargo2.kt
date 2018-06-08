package co.kubo.indiesco.restAPI.modelo

import co.kubo.indiesco.modelo.RecargoVO
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



/**
 * Created by estacion on 8/06/18.
 */
class ResponseRecargo2 {
    @SerializedName("code")
    @Expose
    val code: String? = null
    @SerializedName("data")
    @Expose
    val data: RecargoVO? = null
}