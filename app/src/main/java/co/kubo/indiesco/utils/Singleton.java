package co.kubo.indiesco.utils;

import java.util.ArrayList;

import co.kubo.indiesco.modelo.InmuebleVO;

/**
 * Created by estacion on 23/02/18.
 */

public class Singleton {
    private static Singleton INSTANCE = new Singleton();
    Singleton() {

    }
    public static Singleton getInstance() {
        return INSTANCE;
    }

    Double latitud;
    Double longitud;

    private ArrayList<InmuebleVO> data = new ArrayList<>();

    public ArrayList<InmuebleVO> getData() {
        return data;
    }

    public void setData(ArrayList<InmuebleVO> data) {
        this.data = data;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}