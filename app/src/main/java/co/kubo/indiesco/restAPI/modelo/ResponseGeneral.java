package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Diego on 3/02/2018.
 */

public class ResponseGeneral {
    @SerializedName("code")
    @Expose
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
