package co.kubo.indiesco.interfaces;

import java.util.ArrayList;

import co.kubo.indiesco.adaptadores.DialogDireccionesAdapter;
import co.kubo.indiesco.modelo.Direccion;

/**
 * Created by estacion on 20/02/18.
 */

public interface IDialogDireccionesView {
    public void generarLinearLayoutVertical();
    public void inicializarAdaptadorRvDirecciones(DialogDireccionesAdapter dialogDireccionesAdapter);
    public DialogDireccionesAdapter crearAdaptadorDialogDirecciones(ArrayList<Direccion> direccions);
    public void setDatos(String dir, String lat, String lng);
}
