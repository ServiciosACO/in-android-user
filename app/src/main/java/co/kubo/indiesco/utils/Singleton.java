package co.kubo.indiesco.utils;

import java.util.ArrayList;
import java.util.List;

import co.kubo.indiesco.modelo.InmuebleVO;
import co.kubo.indiesco.modelo.Personal;
import co.kubo.indiesco.modelo.ServiceResumen;

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
    private ArrayList<ServiceResumen> resumen = new ArrayList<>();
    private int position = 0;

    private String posTipoInmueble = "0", idTipoInmueble, categoria;
    private String posDimension = "0", idDimension, dimension;
    private String idDir, direccion;
    private String hora, urgente;
    private String fecha;
    private String nMetros = "0";
    private String nPisos = "0";
    private String posCat;
    private Boolean flagUrgente = false;
    private Double priceFloorOne;
    private Double priceFloorTwo;
    private Double priceFloorThree;

    public String getPrecioFijo() {
        return precioFijo;
    }

    public void setPrecioFijo(String precioFijo) {
        this.precioFijo = precioFijo;
    }

    private String precioFijo = "0";
    private Double DiscountValue = 0.0;
    private String discountCode = "0";
    ArrayList<Personal> arrayListPersonal = new ArrayList<>();
    ArrayList<Personal> arrayListPersonalHistorial = new ArrayList<>();


    public ArrayList<Personal> getArrayListPersonalHistorial() {
        return arrayListPersonalHistorial;
    }

    public void setArrayListPersonalHistorial(ArrayList<Personal> arrayListPersonalHistorial) {
        this.arrayListPersonalHistorial = arrayListPersonalHistorial;
    }

    public ArrayList<Personal> getArrayListPersonal() {
        return arrayListPersonal;
    }

    public void setArrayListPersonal(ArrayList<Personal> arrayListPersonal) {
        this.arrayListPersonal = arrayListPersonal;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public Double getDiscountValue() {
        return DiscountValue;
    }

    public void setDiscountValue(Double discountValue) {
        DiscountValue = discountValue;
    }

    private Boolean validateCoupon = false;

    public Boolean getValidateCoupon() {
        return validateCoupon;
    }

    public void setValidateCoupon(Boolean validateCoupon) {
        this.validateCoupon = validateCoupon;
    }

    public Double getPriceFloorOne() {
        return priceFloorOne;
    }

    public void setPriceFloorOne(Double priceFloorOne) {
        this.priceFloorOne = priceFloorOne;
    }

    public Double getPriceFloorTwo() {
        return priceFloorTwo;
    }

    public void setPriceFloorTwo(Double priceFloorTwo) {
        this.priceFloorTwo = priceFloorTwo;
    }

    public Double getPriceFloorThree() {
        return priceFloorThree;
    }

    public void setPriceFloorThree(Double priceFloorThree) {
        this.priceFloorThree = priceFloorThree;
    }

    private Double difUrgente = 0.0;

    public Double getDifUrgente() {
        return difUrgente;
    }

    public void setDifUrgente(Double difUrgente) {
        this.difUrgente = difUrgente;
    }

    public Boolean getFlagUrgente() {
        return flagUrgente;
    }

    public void setFlagUrgente(Boolean flagUrgente) {
        this.flagUrgente = flagUrgente;
    }

    public String getnPisos() {
        return nPisos;
    }

    public void setnPisos(String nPisos) {
        this.nPisos = nPisos;
    }

    public String getPosCat() {
        return posCat;
    }

    public void setPosCat(String posCat) {
        this.posCat = posCat;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getnMetros() {
        return nMetros;
    }

    public void setnMetros(String nMetros) {
        this.nMetros = nMetros;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUrgente() {
        return urgente;
    }

    public void setUrgente(String urgente) {
        this.urgente = urgente;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<ServiceResumen> getResumen() {
        return resumen;
    }

    public void setResumen(ArrayList<ServiceResumen> resumen) {
        this.resumen = resumen;
    }

    public String getIdDir() {
        return idDir;
    }

    public void setIdDir(String idDir) {
        this.idDir = idDir;
    }

    public String getPosDimension() {
        return posDimension;
    }

    public void setPosDimension(String posDimension) {
        this.posDimension = posDimension;
    }

    public String getIdDimension() {
        return idDimension;
    }

    public void setIdDimension(String idDimension) {
        this.idDimension = idDimension;
    }

    public String getIdTipoInmueble() {
        return idTipoInmueble;
    }

    public void setIdTipoInmueble(String idTipoInmueble) {
        this.idTipoInmueble = idTipoInmueble;
    }

    public String getPosTipoInmueble() {
        return posTipoInmueble;
    }

    public void setPosTipoInmueble(String posTipoInmueble) {
        this.posTipoInmueble = posTipoInmueble;
    }

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