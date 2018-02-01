package co.kubo.indiesco.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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



}//public class SharedPreferenceManager
