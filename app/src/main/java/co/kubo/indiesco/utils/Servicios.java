package co.kubo.indiesco.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Diego on 26/02/2018.
 */

public class Servicios {
    private final static String TAG = "LogServicio";

    @NonNull
    public static String getMethod(String request, Map<String, String> reqProperties) throws
            IOException {

        URL url = new URL(request);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("GET");
        conn.setUseCaches(false);
        if (reqProperties != null) {
            for (Map.Entry<String, String> property : reqProperties.entrySet()) {
                conn.setRequestProperty(property.getKey(), property.getValue());
            }
        }
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        } catch (Exception e) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
        String line;
        StringBuilder responseOutPut = new StringBuilder();

        while ((line = br.readLine()) != null) {
            responseOutPut.append(line);
        }
        br.close();
        Log.v(TAG + "Get" + "Url", request);
        try {
            printFullResponse(new JSONObject(responseOutPut.toString()).toString(2), "Get");
        } catch (JSONException e) {
            try {
                printFullResponse(new JSONArray(responseOutPut.toString()).toString(2), "Get");
            } catch (JSONException ignore) {
                printFullResponse(responseOutPut.toString(), "Get");
            }
        }

        return responseOutPut.toString();

    }

    public static void printFullResponse(String veryLongString, String tipo) {
        String[] lineas = veryLongString.split("\n");
        for (String linea : lineas) {
            Log.v(TAG + tipo + "Respuesta", linea);
        }
    }
}
