package co.kubo.indiesco.interfaces;

import java.util.ArrayList;

import co.kubo.indiesco.adaptadores.HistorialServiciosAdapter;
import co.kubo.indiesco.adaptadores.MisDireccionesAdapter;
import co.kubo.indiesco.modelo.Direccion;
import co.kubo.indiesco.modelo.Historial;

/**
 * Created by Diego on 15/02/2018.
 */

public interface IMisDireccionesView {
    public void generarLinearLayoutVertical();
    public void inicializarAdaptadorRvDirecciones(MisDireccionesAdapter misDireccionesAdapter);
    public MisDireccionesAdapter crearAdaptadorDirecciones(ArrayList<Direccion> direccions);
}
