package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import co.kubo.indiesco.modelo.Notificaciones;
import co.kubo.indiesco.modelo.TasarServicio;

/**
 * Created by Diego on 8/02/2018.
 */

public class ResponseTasarServicio {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private ArrayList<TasarServicio> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<TasarServicio> getData() {
        return data;
    }

    public void setData(ArrayList<TasarServicio> data) {
        this.data = data;
    }
}
