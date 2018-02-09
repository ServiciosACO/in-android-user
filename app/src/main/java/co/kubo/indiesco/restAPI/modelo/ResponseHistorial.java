package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.modelo.Login;

/**
 * Created by Diego on 8/02/2018.
 */

public class ResponseHistorial {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private ArrayList<Historial> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Historial> getData() {
        return data;
    }

    public void setData(ArrayList<Historial> data) {
        this.data = data;
    }
}
