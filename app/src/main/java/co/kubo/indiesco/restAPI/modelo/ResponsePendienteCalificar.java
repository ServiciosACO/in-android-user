package co.kubo.indiesco.restAPI.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import co.kubo.indiesco.modelo.PendienteCalificar;

/**
 * Created by Diego on 24/02/2018.
 */

public class ResponsePendienteCalificar {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private ArrayList<PendienteCalificar> data = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<PendienteCalificar> getData() {
        return data;
    }

    public void setData(ArrayList<PendienteCalificar> data) {
        this.data = data;
    }



}
