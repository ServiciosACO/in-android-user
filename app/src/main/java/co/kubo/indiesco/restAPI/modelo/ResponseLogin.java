package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.kubo.indiesco.modelo.Login;

/**
 * Created by Diego on 4/02/2018.
 */

public class ResponseLogin {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private Login data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Login getData() {
        return data;
    }

    public void setData(Login data) {
        this.data = data;
    }
}
