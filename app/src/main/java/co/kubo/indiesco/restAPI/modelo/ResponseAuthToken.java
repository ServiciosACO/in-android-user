package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.kubo.indiesco.modelo.AuthToken;

/**
 * Created by Diego on 3/02/2018.
 */

public class ResponseAuthToken {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private AuthToken data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AuthToken getData() {
        return data;
    }

    public void setData(AuthToken data) {
        this.data = data;
    }
}
