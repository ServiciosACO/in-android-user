package co.kubo.indiesco.restAPI.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseTipoServicio {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private List<TipoServicio> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<TipoServicio> getData() {
        return data;
    }

    public void setData(List<TipoServicio> data) {
        this.data = data;
    }

    public class TipoServicio {

        @SerializedName("estado")
        @Expose
        private String estado;
        @SerializedName("id_servicio")
        @Expose
        private int idServicio;
        @SerializedName("imagen")
        @Expose
        private String imagen;
        @SerializedName("servicio")
        @Expose
        private String servicio;
        private boolean isSeleccionado = false;

        public TipoServicio(String estado, int idServicio, String imagen, String servicio) {
            this.estado = estado;
            this.idServicio = idServicio;
            this.imagen = imagen;
            this.servicio = servicio;
        }

        public TipoServicio() {
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public int getIdServicio() {
            return idServicio;
        }

        public void setIdServicio(int idServicio) {
            this.idServicio = idServicio;
        }

        public String getImagen() {
            return imagen;
        }

        public void setImagen(String imagen) {
            this.imagen = imagen;
        }

        public String getServicio() {
            return servicio;
        }

        public void setServicio(String servicio) {
            this.servicio = servicio;
        }

        public boolean isSeleccionado() {
            return isSeleccionado;
        }

        public void setSeleccionado(boolean seleccionado) {
            isSeleccionado = seleccionado;
        }
    }
}
