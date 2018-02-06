package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
    private ArrayList<Login> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Login> getData() {
        return data;
    }

    public void setData(ArrayList<Login> data) {
        this.data = data;
    }
}
