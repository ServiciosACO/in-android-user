package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import co.kubo.indiesco.modelo.Login;
import co.kubo.indiesco.modelo.Notificaciones;

/**
 * Created by Diego on 8/02/2018.
 */

public class ResponseNotificacion {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private ArrayList<Notificaciones> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Notificaciones> getData() {
        return data;
    }

    public void setData(ArrayList<Notificaciones> data) {
        this.data = data;
    }
}
