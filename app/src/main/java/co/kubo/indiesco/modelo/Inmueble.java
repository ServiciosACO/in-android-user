package co.kubo.indiesco.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego on 8/02/2018.
 */

public class Inmueble {
    @SerializedName("id_tipo_inmueble")
    @Expose
    private String idTipoInmueble;
    @SerializedName("inmueble")
    @Expose
    private String inmueble;
    @SerializedName("dimesiones")
    @Expose
    private ArrayList<Dimensiones> dimesiones = null;

    public String getIdTipoInmueble() {
        return idTipoInmueble;
    }

    public void setIdTipoInmueble(String idTipoInmueble) {
        this.idTipoInmueble = idTipoInmueble;
    }

    public String getInmueble() {
        return inmueble;
    }

    public void setInmueble(String inmueble) {
        this.inmueble = inmueble;
    }

    public ArrayList<Dimensiones> getDimesiones() {
        return dimesiones;
    }

    public void setDimesiones(ArrayList<Dimensiones> dimesiones) {
        this.dimesiones = dimesiones;
    }
}
