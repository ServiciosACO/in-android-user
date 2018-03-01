package co.kubo.indiesco.interfaces;

import java.util.ArrayList;

import co.kubo.indiesco.adaptadores.CalendarioAdapter;
import co.kubo.indiesco.adaptadores.CalendarioDetalleFechaAdapter;
import co.kubo.indiesco.modelo.Historial;

/**
 * Created by estacion on 1/03/18.
 */

public interface ICalendario2View {
    public void generarLinearLayoutVertical();
    public void inicializarAdaptadorRvCalendarioPorFecha(CalendarioDetalleFechaAdapter calendarioDetalleFechaAdapter);
    public CalendarioDetalleFechaAdapter crearAdaptadorCalendarioPorFecha(ArrayList<Historial> calendario);
    public void obtenerCalendario();
    public void pintarSinServicio();
}
