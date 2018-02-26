package co.kubo.indiesco.interfaces;

import java.util.ArrayList;

import co.kubo.indiesco.adaptadores.CalendarioAdapter;
import co.kubo.indiesco.adaptadores.CalificarAdapter;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.modelo.PendienteCalificar;

/**
 * Created by Diego on 25/02/2018.
 */

public interface ICalificarView {
    public void generarLinearLayoutVertical();
    public void inicializarAdaptadorRvCalificar(CalificarAdapter calificarAdapter);
    public CalificarAdapter crearAdaptadorCalificar(ArrayList<PendienteCalificar> calificar);
}
