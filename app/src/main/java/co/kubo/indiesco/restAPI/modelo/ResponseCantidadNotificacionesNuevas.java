package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCantidadNotificacionesNuevas {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private Integer data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }
}
