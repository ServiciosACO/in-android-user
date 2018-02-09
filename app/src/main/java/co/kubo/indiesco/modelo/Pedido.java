package co.kubo.indiesco.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Diego on 8/02/2018.
 */

public class Pedido {
    @SerializedName("id_pedido")
    @Expose
    private String id_pedido;
}
