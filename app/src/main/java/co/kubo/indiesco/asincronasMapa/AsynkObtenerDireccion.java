package co.kubo.indiesco.asincronasMapa;

import android.os.AsyncTask;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import co.kubo.indiesco.activities.Registro;

public class AsynkObtenerDireccion extends AsyncTask<Void, Void, Boolean> {

    private Registro context = new Registro();
    private String urlServicio = "";
    private String lat = "";
    private String lon = "";
    private String direccion = "";
    private Boolean band = true;

    public AsynkObtenerDireccion(Registro context, String lat, String lon, Boolean band) {
        this.context = context;
        this.lat = lat;
        this.lon = lon;
        this.band = band;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String jsonResponse = "";
        try {
            urlServicio = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "&sensor=true";
            URL url = new URL(urlServicio);
            DataInputStream dataInput = new DataInputStream(url.openStream());
            String inputLine;
            while ((inputLine = dataInput.readLine()) != null) {
                jsonResponse += inputLine;
            }
            dataInput.close();
            JSONObject catObj = new JSONObject(jsonResponse);
            JSONArray catArray = catObj.getJSONArray("results");
            JSONObject catObj1 = catArray.getJSONObject(0);
            direccion = new String(catObj1.getString("formatted_address").getBytes("ISO-8859-1"), "UTF-8");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        context.valorDireccion(direccion, band);
    }

}
