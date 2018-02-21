package co.kubo.indiesco.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.ParseException;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by estacion on 31/01/18.
 */

public class Utils {

    public Utils() {
    }

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

    public static String sha1Encrypt(String texto) {
        try {
            java.security.MessageDigest sha = java.security.MessageDigest.getInstance("SHA-1");
            byte[] array = sha.digest(texto.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignore) {
        }
        return "";
    }

    public boolean isEmailValid(String email) {
        //String email = editEmail.getText().toString();
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches())
            return true;
        else
            return false;
    }//public boolean isEmailValid

    public String StringToDate(String fecha){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        try {
            date = format.parse(fecha);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return DateToString(date);
    }
    public String DateToString (java.util.Date date){
        SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
        String datetime="";
        try {
            datetime = dateformat.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return datetime;
    }//public String DateToString
}
