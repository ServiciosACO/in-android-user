package co.kubo.indiesco.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by estacion on 23/02/18.
 */

public class CrearServicio {

    @SerializedName("id_solicitud")
    @Expose
    private String idSolicitud;

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

}