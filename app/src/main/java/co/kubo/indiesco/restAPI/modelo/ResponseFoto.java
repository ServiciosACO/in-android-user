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
    @SerializedName("foto")
    @Expose
    private String foto;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
