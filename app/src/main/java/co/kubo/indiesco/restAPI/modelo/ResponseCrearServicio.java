package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import co.kubo.indiesco.modelo.CrearServicio;
import co.kubo.indiesco.modelo.Direccion;

/**
 * Created by estacion on 23/02/18.
 */

public class ResponseCrearServicio {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private CrearServicio data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CrearServicio getData() {
        return data;
    }

    public void setData(CrearServicio data) {
        this.data = data;
    }

}
