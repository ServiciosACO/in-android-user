package co.kubo.indiesco.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Diego on 8/02/2018.
 */

public class Notificaciones {
    @SerializedName("id_notificacion")
    @Expose
    private String id_notificacion;
    @SerializedName("notificacion")
    @Expose
    private String notificacion;
    @SerializedName("fecha")
    @Expose
    private String fecha;

    private String isHeader;

    public String getIsHeader() {
        return isHeader;
    }

    public void setIsHeader(String isHeader) {
        this.isHeader = isHeader;
    }

    public String getId_notificacion() {
        return id_notificacion;
    }

    public void setId_notificacion(String id_notificacion) {
        this.id_notificacion = id_notificacion;
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
