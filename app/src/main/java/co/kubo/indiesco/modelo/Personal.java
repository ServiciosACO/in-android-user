package co.kubo.indiesco.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Personal implements Cloneable {
    public String getId_personal() {
        return id_personal;
    }

    public void setId_personal(String id_personal) {
        this.id_personal = id_personal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @SerializedName("id_personal")
    @Expose
    private String id_personal = "";
    @SerializedName("nombre")
    @Expose
    private String nombre = "";
    @SerializedName("telefono")
    @Expose
    private String telefono = "";
    @SerializedName("calificacion")
    @Expose
    private String calificacion = "0";
    @SerializedName("foto")
    @Expose
    private String foto = "";

    private Boolean calificado = false;

    public Boolean getCalificado() {
        return calificado;
    }

    public void setCalificado(Boolean calificado) {
        this.calificado = calificado;
    }
}
