package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import co.kubo.indiesco.modelo.Login;
import co.kubo.indiesco.modelo.Pedido;

/**
 * Created by Diego on 8/02/2018.
 */

public class ResponsePedido {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private ArrayList<Pedido> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Pedido> getData() {
        return data;
    }

    public void setData(ArrayList<Pedido> data) {
        this.data = data;
    }
}
