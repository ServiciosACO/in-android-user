package co.kubo.indiesco.asincronasMapa;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import co.kubo.indiesco.activities.Registro;
import co.kubo.indiesco.modelo.direccionesGoogleVO;

public class AsynkDireccionPalabra extends AsyncTask<Void, Void, Boolean> {

    private Registro parent = new Registro();
    private String urlServicio = "";
    private String lat = "";
    private String lon = "";
    private String direccion = "";
    private String key = "";
    private List<String> lista = new ArrayList<>();
    private JSONArray array = null;

    public AsynkDireccionPalabra(Registro parent, String lat, String lon, String direccion, String key){
        this.parent = parent;
        this.lat = lat ;
        this.lon = lon;
        this.direccion = direccion;
        this.key = key;
    }

    @Override
    protected void onPreExecute() {
        try {
            direccion = encodeUTF(direccion);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String jsonResponse ="";
        try {
            urlServicio = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+direccion+"&sensor=true"+"&key="+key+"&location="+lat+","+lon+"&radius=1000&components=country:CO&language=ES";
            URL url = new URL(urlServicio);
            DataInputStream dataInput = new DataInputStream(url.openStream());
            String inputLine;
            while ((inputLine = dataInput.readLine()) != null)
            {
                jsonResponse+=inputLine;
            }
            dataInput.close();
            JSONObject catObj = new JSONObject(jsonResponse);
            array = catObj.getJSONArray("predictions");
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            parent.vacio();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            parent.vacio();
        } catch (JSONException e) {
            e.printStackTrace();
            parent.vacio();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        parent.vacio();
        if(result){
            if(array!=null){
                parent.opcionesDireccion(parseo());
            }
        }
    }

    public List<direccionesGoogleVO> parseo(){
        List<direccionesGoogleVO> lista = new ArrayList<>();
        for(int i = 0; i< array.length();i++){
            try {
                JSONObject obj = array.getJSONObject(i);
                direccionesGoogleVO dato = new direccionesGoogleVO();
                String datosCiudad[] = new String(obj.getString("description").getBytes("ISO-8859-1"), "UTF-8").split(",");
                if(datosCiudad.length >= 2){
                    dato.setDescription(datosCiudad[0].trim());
                    dato.setCiudad(datosCiudad[1].trim());
                }else{
                    dato.setDescription(datosCiudad[0].trim());
                    dato.setCiudad(" ");
                }
                dato.setPlaceId(obj.getString("place_id"));
                lista.add(dato);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        }
        return lista;
    }

    public String encodeUTF(String strEncode) throws UnsupportedEncodingException {
        return URLEncoder.encode(strEncode, "utf-8");
    }

}
