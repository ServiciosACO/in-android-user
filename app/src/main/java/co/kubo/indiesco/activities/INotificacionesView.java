package co.kubo.indiesco.activities;

import java.util.ArrayList;

import co.kubo.indiesco.adaptadores.NotificacionesAdapter;
import co.kubo.indiesco.modelo.Notificaciones;

/**
 * Created by estacion on 21/02/18.
 */

public interface INotificacionesView {

    public void generarLinearLayoutVertical();
    public void inicializarAdaptadorRvNotificaciones(NotificacionesAdapter notificacionesAdapter);
    public NotificacionesAdapter crearAdaptadorNotificaciones(ArrayList<Notificaciones> notificaciones);
}
