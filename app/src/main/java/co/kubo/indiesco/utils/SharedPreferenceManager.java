package co.kubo.indiesco.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import co.kubo.indiesco.modelo.Usuario;

/**
 * Created by estacion on 31/01/18.
 */

public class SharedPreferenceManager {
    private static final String NOMBRE_ARCHIVO = "SharedPreferencesIndiesco";

    public static void setPrimeraVez(Activity activity, boolean primeraVez){
        SharedPreferences prefs = activity.getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("primeraVez", primeraVez);
        editor.apply();
    }//public void setPrimeraVez

    public static boolean getPrimeraVez(Context context){
        SharedPreferences prefs = context.getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        return prefs.getBoolean("primeraVez", false);
    }//public static boolean getPrimeraVez

    public static void setAuthToken(Context context, String authToken) {
        SharedPreferences prefs = context.getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("authToken", authToken);
        editor.apply();
    }

    public static String getAuthToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        return prefs.getString("authToken", "");
    }

    public static void setInfoUsuario(Context context, Usuario usuario) {
        SharedPreferences prefs = context.getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("infoUsuario", new Gson().toJson(usuario));
        editor.apply();
    }

    public static Usuario getInfoUsuario(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        Usuario usuario = new Gson().fromJson(prefs.getString("infoUsuario", ""), Usuario.class);
        if (usuario == null) {
            usuario = new Usuario();
        }
        return usuario;
    }

    public static void setLoged(Activity activity, boolean isLoged){
        SharedPreferences prefs = activity.getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoged", isLoged);
        editor.apply();
    }

    public static boolean getLoged(Context context){
        SharedPreferences prefs = context.getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE);
        return prefs.getBoolean("isLoged", false);
    }
}//public class SharedPreferenceManager
