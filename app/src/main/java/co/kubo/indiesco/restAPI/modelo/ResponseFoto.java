package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import co.kubo.indiesco.modelo.Foto;
import co.kubo.indiesco.modelo.Registro;

/**
 * Created by Diego on 5/02/2018.
 */

public class ResponseFoto {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private ArrayList<Foto> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Foto> getData() {
        return data;
    }

    public void setData(ArrayList<Foto> data) {
        this.data = data;
    }
}
