package co.kubo.indiesco.asincronasMapa;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import co.kubo.indiesco.R;
import co.kubo.indiesco.activities.EditarPerfil;
import co.kubo.indiesco.activities.Registro;
import co.kubo.indiesco.utils.Servicios;

public class AsincronaGetDetalleDireccionGoogle extends AsyncTask<Void, Void, String> {

    private final static String TAG = "LogAsnDIrCoord";

    private Activity activity;
    private String placeId;
    private int tipo;

    public AsincronaGetDetalleDireccionGoogle(Activity activity, String placeId, int tipo) {
        this.activity = activity;
        this.placeId = placeId;
        this.tipo = tipo;
    }

    @Override
    protected void onPreExecute() {
        //dialog
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            String request = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&key=" +
                    activity.getResources().getString(R.string.key_google_maps);
            return Servicios.getMethod(request, null);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        String[] latlng = new String[]{"0", "0"};
        if (!result.isEmpty()) {
            try {
                JSONObject mainObject = new JSONObject(result);
                switch (mainObject.getString("status")) {
                    case "OK":
                        JSONObject obj = mainObject.getJSONObject("result").getJSONObject("geometry").getJSONObject
                                ("location");
                        latlng = new String[]{obj.getString("lat"), obj.getString("lng")};
                        break;
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        if (tipo == 1){
         //   ((Registro) activity).setLatitudYLongitud(latlng);
        }else{
            ((EditarPerfil) activity).setLatitudYLongitud(latlng);
        }

    }
}
