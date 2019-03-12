package co.kubo.indiesco.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego on 24/02/2018.
 */

public class PendienteCalificar {
    @SerializedName("id_solicitud_item")
    @Expose
    private String idSolicitud;
    @SerializedName("id_tipo_inmueble")
    @Expose
    private String idTipoInmueble;
    @SerializedName("dimension")
    @Expose
    private String dimension;
    @SerializedName("valor")
    @Expose
    private String valor;
    @SerializedName("id_direccion")
    @Expose
    private String idDireccion;
    @SerializedName("fecha_servicio")
    @Expose
    private String fechaServicio;
    @SerializedName("urgente")
    @Expose
    private String urgente;
    @SerializedName("hora")
    @Expose
    private String hora;
    @SerializedName("comentario")
    @Expose
    private String comentario;
    @SerializedName("fecha_transaccion")
    @Expose
    private String fechaTransaccion;
    @SerializedName("direccion")
    @Expose
    private String direccion;
    @SerializedName("ciudad")
    @Expose
    private String ciudad;
    @SerializedName("latitud")
    @Expose
    private String latitud;
    @SerializedName("longitud")
    @Expose
    private String longitud;
    @SerializedName("inmueble")
    @Expose
    private String inmueble;

    @SerializedName("personal")
    @Expose
    private ArrayList<Personal> personal = new ArrayList<>();

    private Boolean calificadoPendiente = false;

    public Boolean getCalificadoPendiente() {
        return calificadoPendiente;
    }

    public void setCalificadoPendiente(Boolean calificadoPendiente) {
        this.calificadoPendiente = calificadoPendiente;
    }

    public ArrayList<Personal> getPersonal() {
        return personal;
    }

    public void setPersonal(ArrayList<Personal> personal) {
        this.personal = personal;
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getIdTipoInmueble() {
        return idTipoInmueble;
    }

    public void setIdTipoInmueble(String idTipoInmueble) {
        this.idTipoInmueble = idTipoInmueble;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(String idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(String fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public String getUrgente() {
        return urgente;
    }

    public void setUrgente(String urgente) {
        this.urgente = urgente;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(String fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getInmueble() {
        return inmueble;
    }

    public void setInmueble(String inmueble) {
        this.inmueble = inmueble;
    }
}
