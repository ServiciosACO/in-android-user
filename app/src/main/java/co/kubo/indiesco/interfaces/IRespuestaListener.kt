package co.kubo.indiesco.interfaces

/**
 * Created by estacion on 13/06/18.
 */
interface IRespuestaListener {
    abstract fun onCancelar()
    abstract fun onAceptar()
    abstract fun onSalir()
}