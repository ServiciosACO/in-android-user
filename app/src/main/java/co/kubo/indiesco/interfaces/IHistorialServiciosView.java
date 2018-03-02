package co.kubo.indiesco.interfaces;

import java.util.ArrayList;

import co.kubo.indiesco.adaptadores.HistorialServiciosAdapter;
import co.kubo.indiesco.modelo.Historial;

/**
 * Created by Diego on 12/02/2018.
 */

public interface IHistorialServiciosView {
    public void generarLinearLayoutVertical();
    public void inicializarAdaptadorRvHistorial(HistorialServiciosAdapter historialServiciosAdapter);
    public HistorialServiciosAdapter crearAdaptadorHistorial(ArrayList<Historial> historials);
    public void pintarSinInfo();
}
