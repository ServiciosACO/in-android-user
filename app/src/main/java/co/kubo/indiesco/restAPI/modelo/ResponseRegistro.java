package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.kubo.indiesco.modelo.Login;
import co.kubo.indiesco.modelo.Registro;

/**
 * Created by Diego on 5/02/2018.
 */

public class ResponseRegistro {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private Registro data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Registro getData() {
        return data;
    }

    public void setData(Registro data) {
        this.data = data;
    }
}
