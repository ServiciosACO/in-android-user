package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import co.kubo.indiesco.modelo.Direccion;

/**
 * Created by Diego on 9/02/2018.
 */

public class ResponseDireccion {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private ArrayList<Direccion> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Direccion> getData() {
        return data;
    }

    public void setData(ArrayList<Direccion> data) {
        this.data = data;
    }
}
