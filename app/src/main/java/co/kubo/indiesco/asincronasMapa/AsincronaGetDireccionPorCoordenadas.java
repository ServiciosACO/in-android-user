package co.kubo.indiesco.asincronasMapa;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import co.kubo.indiesco.activities.EditarPerfil;
import co.kubo.indiesco.activities.Registro;
import co.kubo.indiesco.utils.Servicios;

public class AsincronaGetDireccionPorCoordenadas extends AsyncTask<Void, Void, String> {

    private Registro context = new Registro();
    private String urlServicio = "";
    private String lat = "";
    private String lon = "";
    private String direccion = "";
    private Boolean band = true;
    Activity activity;
    private int opcion;

    public AsincronaGetDireccionPorCoordenadas(String lat, String lon, Activity activity, int opcion) {
        this.lat = lat;
        this.lon = lon;
        this.activity = activity;
        this.opcion = opcion;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            String request = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "&sensor=true";

            return Servicios.getMethod(request, null);
        } catch (Exception e) {
            //Log.e(TAG, e.toString());
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        String direccion = "";
        String direccionCorta = "";
        String ciudad = "";
        if (!result.isEmpty()) {
            try {
                JSONObject mainObject = new JSONObject(result);
                switch (mainObject.getString("status")) {
                    case "OK":
                        direccion = mainObject.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                        String[] ciudadArray = direccion.split(",");
                        for (int i = 1; i < ciudadArray.length; i++) {
                            ciudad += ciudadArray[i].trim();
                            if (i != ciudadArray.length - 1) {
                                ciudad += ", ";
                            }
                        }
                        direccion = direccion.replace(", " + ciudad, "");
                        ciudad = ciudad.replace(", Colombia", "");
                        try {
                            String data[] = direccion.split(",");
                            if (data != null) {
                                if (data.length > 0) {
                                    if (data[0].contains(" a ")) {
                                        String direc[] = data[0].split(" a ");
                                        direccionCorta = direc[0];
                                    } else {
                                        direccionCorta = data[0];
                                    }
                                }
                            }
                        } catch (Exception e) {
                            direccionCorta = direccion;
                        }
                        break;
                }
            } catch (Exception e) {
                //Log.e(TAG, e.toString());
            }
        }
        if (opcion == 1){
            ((Registro) activity).setDireccion(direccionCorta, ciudad);
        }else{
            ((EditarPerfil) activity).setDireccion(direccionCorta, ciudad);
        }
    }
}

