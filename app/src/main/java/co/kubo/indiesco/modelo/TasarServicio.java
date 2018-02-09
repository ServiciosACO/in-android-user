package co.kubo.indiesco.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Diego on 8/02/2018.
 */

public class TasarServicio {
    @SerializedName("valor")
    @Expose
    private String valor;

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
