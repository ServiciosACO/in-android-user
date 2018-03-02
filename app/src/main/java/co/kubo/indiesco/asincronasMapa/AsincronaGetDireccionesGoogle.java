package co.kubo.indiesco.asincronasMapa;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.EditarPerfil;
import co.kubo.indiesco.activities.Registro;


public class AsincronaGetDireccionesGoogle extends AsyncTask<Void, Void, String> {

    private final static String TAG = "C.L.LogAsnDIrCoord";

    private Activity activity;
    private String direccion;
    private int tipo;

    public AsincronaGetDireccionesGoogle(Activity activity, String direccion, int tipo) {
        this.activity = activity;
        this.direccion = direccion;
        this.tipo = tipo;
    }

    @Override
    protected void onPreExecute() {
        //Dialog
    }

    @Override
    protected String doInBackground(Void... params) {
        String  jsonResponse = "";
        try {
            direccion = URLEncoder.encode(direccion, "UTF-8");
            String request = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + direccion +
                    "&sensor=true" + "&key=" + activity.getResources().getString(R.string.key_google_maps) +
                    "&radius=1000&components=country:CO&language=ES";

      //     return Servicios.getMethod(request, null);

            URL url = new URL(request);
            DataInputStream dataInput = new DataInputStream(url.openStream());
            String inputLine;
            while ((inputLine = dataInput.readLine()) != null)
            {
                jsonResponse+=inputLine;
            }
            dataInput.close();
            return jsonResponse;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        ArrayList<String[]> direcciones = new ArrayList<>();
        if (!result.isEmpty()) {
            try {
                JSONObject mainObject = new JSONObject(result);
                switch (mainObject.getString("status")) {
                    case "OK":
                        JSONArray jsonArray = mainObject.getJSONArray("predictions");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (i < 5) {
                                String nombreDireccion = new String(jsonObject.getString("description").getBytes("ISO-8859-1"), "UTF-8").replace(", " +"Colombia", "");
                                direcciones.add(new String[]{nombreDireccion, jsonObject.getString("place_id")});
                            } else {
                                break;
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        if (tipo == 1){
            ((Registro) activity).llenarAutocomplete(direcciones);
        }else{
            ((EditarPerfil) activity).llenarAutocomplete(direcciones);
        }

    }
}
