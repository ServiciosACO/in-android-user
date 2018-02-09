package co.kubo.indiesco.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Diego on 8/02/2018.
 */

public class Inmueble {
    @SerializedName("id_tipo_inmueble")
    @Expose
    private String id_tipo_inmueble;
    @SerializedName("inmueble")
    @Expose
    private String inmueble;

    public String getId_tipo_inmueble() {
        return id_tipo_inmueble;
    }

    public void setId_tipo_inmueble(String id_tipo_inmueble) {
        this.id_tipo_inmueble = id_tipo_inmueble;
    }

    public String getInmueble() {
        return inmueble;
    }

    public void setInmueble(String inmueble) {
        this.inmueble = inmueble;
    }
}
