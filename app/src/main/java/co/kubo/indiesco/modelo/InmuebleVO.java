package co.kubo.indiesco.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Diego on 8/02/2018.
 */

public class InmuebleVO {
    @SerializedName("id_categoria")
    @Expose
    private String idCategoria;
    @SerializedName("categoria")
    @Expose
    private String categoria;
    @SerializedName("imagen")
    @Expose
    private String imagen;
    @SerializedName("tipo_cobro")
    @Expose
    private String tipoCobro;
    @SerializedName("valor_mt2")
    @Expose
    private String valor_mt2;
    @SerializedName("tipos_inmuebles")
    @Expose
    private ArrayList<InmuebleTipos> tiposInmuebles = null;

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id_servicio")
    @Expose
    private int idServicio;

    Boolean check = false;

    int nPisos = 0;

    public String getValor_mt2() {
        return valor_mt2;
    }

    public void setValor_mt2(String valor_mt2) {
        this.valor_mt2 = valor_mt2;
    }

    public int getnPisos() {
        return nPisos;
    }

    public void setnPisos(int nPisos) {
        this.nPisos = nPisos;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTipoCobro() {
        return tipoCobro;
    }

    public void setTipoCobro(String tipoCobro) {
        this.tipoCobro = tipoCobro;
    }


    public ArrayList<InmuebleTipos> getTiposInmuebles() {
        return tiposInmuebles;
    }

    public void setTiposInmuebles(ArrayList<InmuebleTipos> tiposInmuebles) {
        this.tiposInmuebles = tiposInmuebles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }
}
