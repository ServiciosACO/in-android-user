package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import co.kubo.indiesco.modelo.InmuebleVO;

/**
 * Created by Diego on 8/02/2018.
 */

public class ResponseInmueble {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private ArrayList<InmuebleVO> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<InmuebleVO> getData() {
        return data;
    }

    public void setData(ArrayList<InmuebleVO> data) {
        this.data = data;
    }
}
