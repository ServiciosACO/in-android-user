package co.kubo.indiesco.interfaces;

import java.util.ArrayList;

import co.kubo.indiesco.activities.Calendario;
import co.kubo.indiesco.adaptadores.CalendarioAdapter;
import co.kubo.indiesco.modelo.Historial;

/**
 * Created by Diego on 24/02/2018.
 */

public interface ICalendarioView {
    public void generarLinearLayoutVertical();
    public void inicializarAdaptadorRvCalendario(CalendarioAdapter calendarioAdapter);
    public CalendarioAdapter crearAdaptadorCalendario(ArrayList<Historial> calendario);
    public void pintarSinServicio();
}
