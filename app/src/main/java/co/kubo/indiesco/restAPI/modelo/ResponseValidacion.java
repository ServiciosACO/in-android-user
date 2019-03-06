package co.kubo.indiesco.restAPI.modelo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import co.kubo.indiesco.modelo.ValidacionDirecciones;

public class ResponseValidacion {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private ArrayList<ValidacionDirecciones> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<ValidacionDirecciones> getData() {
        return data;
    }

    public void setData(ArrayList<ValidacionDirecciones> data) {
        this.data = data;
    }

}
