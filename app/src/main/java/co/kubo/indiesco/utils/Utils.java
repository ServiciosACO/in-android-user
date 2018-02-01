package co.kubo.indiesco.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by estacion on 31/01/18.
 */

public class Utils {

    public static boolean checkInternetConnection(Activity context, boolean showToast) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            if (showToast) {
                Toast.makeText(context, "Verifique su conexión a internet", Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }

    public Typeface fuenteBold(Context context){
        return Typeface.createFromAsset(context.getAssets(), "Fonts/Barlow-Bold.ttf");
    }
    public Typeface fuenteMedium(Context context){
        return Typeface.createFromAsset(context.getAssets(), "Fonts/Barlow-Medium.ttf");
    }
    public Typeface fuenteRegular(Context context){
        return Typeface.createFromAsset(context.getAssets(), "Fonts/Barlow-Regular.ttf");
    }
    public Typeface fuenteSemiBold(Context context){
        return Typeface.createFromAsset(context.getAssets(), "Fonts/Barlow-SemiBold.ttf");
    }
    /*

    public Typeface fuenteBold(Context context){
        return Typeface.createFromAsset(context.getAssets(), "Fonts/Catamaran-Bold.ttf");
    }
    public Typeface fuenteRegular(Context context){
        return Typeface.createFromAsset(context.getAssets(), "Fonts/Catamaran-Regular.ttf");
    }

    public static String md5Encrypt(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignore) {
        }
        return "";
    }
     */
}
