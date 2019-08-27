package co.kubo.indiesco.restAPI.modelo

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseTotalToPay {

    @SerializedName("code")
    @Expose
    lateinit var code: String
    @SerializedName("data")
    @Expose
    lateinit var data: Data


    class Data {
        @SerializedName("price")
        @Expose
        var price = 0.0
    }

    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }
}