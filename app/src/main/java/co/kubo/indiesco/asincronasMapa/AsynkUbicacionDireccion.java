package co.kubo.indiesco.asincronasMapa;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import co.kubo.indiesco.activities.Registro;


public class AsynkUbicacionDireccion extends AsyncTask<Void, Void, Boolean> {

    private Registro parent = new Registro();
    private String urlServicio = "";
    private String place_id = "";
    private String key = "";
    private String lon = "";
    private String lat = "";
    private Boolean band = true;
    public AsynkUbicacionDireccion(Registro parent, String place_id, String key, Boolean band){
        this.parent = parent;
        this.place_id = place_id;
        this.key = key;
        this.band = band;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String jsonResponse ="";
        try {
            urlServicio = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&sensor=true&key="+key;
            URL url = new URL(urlServicio);
            DataInputStream dataInput = new DataInputStream(url.openStream());
            String inputLine;
            while ((inputLine = dataInput.readLine()) != null)
            {
                jsonResponse+=inputLine;
            }
            dataInput.close();
            JSONObject catObj = new JSONObject(jsonResponse);
            JSONObject result = catObj.getJSONObject("result");
            JSONObject catLot = result.getJSONObject("geometry");
            catLot = catLot.getJSONObject("location");
            lon = catLot.getString("lng");
            lat = catLot.getString("lat");
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if(result){
            if(!lon.equals("") && !lat.equals("")){
                parent.guardarCoordenadas(lat,lon, band);
            }else{
              //  Utils.toastGenerico(parent, "" +parent.getResources().getString(R.string.errorUbic));
            }
        }else{
         //   Utils.toastGenerico(parent, "" +parent.getResources().getString(R.string.errorUbic));
        }
    }

}
