package co.kubo.indiesco.activities;

import android.content.Context;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import co.kubo.indiesco.R;

/**
 * Created by Diego on 3/02/2018.
 */

public class ObtenerAccessToken {
    Context mContext;
    public ObtenerAccessToken(Context mContext)
    {
        this.mContext = mContext;
    }

    public byte[] encriptar(String textEncriptar, String key) {
        byte[] encrypted = null;
        try {
            String iv = "" + mContext.getResources().getString(R.string.IV);
            IvParameterSpec ivspec = null;
            SecretKeySpec keyspec1 = null;
            Cipher cipher = null;
            String SecretKey = key;
            cipher = Cipher.getInstance("AES/CBC/NoPadding","BC");
            ivspec = new IvParameterSpec(iv.getBytes());
            keyspec1 = new SecretKeySpec(SecretKey.getBytes(), "AES");
            if (textEncriptar == null || textEncriptar.length() == 0) {
                throw new Exception("Empty string");
            }
            cipher.init(Cipher.ENCRYPT_MODE, keyspec1, ivspec);
            if (textEncriptar.length() == 16) {
                encrypted = cipher.doFinal(textEncriptar.getBytes());
            } else {
                encrypted = cipher.doFinal(padString(textEncriptar).getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encrypted;
    }

    public static String bytesToHex(byte[] data)
    {
        if (data==null)
        {
            return null;
        }
        String de = data.toString();
        int len = data.length;
        String str = "";
        for (int i=0; i<len; i++) {
            if ((data[i]&0xFF)<16)
                str = str + "0" + java.lang.Integer.toHexString(data[i]&0xFF);
            else
                str = str + java.lang.Integer.toHexString(data[i]&0xFF);
        }
        return str;
        /*String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;*/
    }

    private static String padString(String source)
    {
        char paddingChar = ' ';
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++)
        {
            source += paddingChar;
        }

        return source;
    }


    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding","BC");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            // byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));
            byte[] original = cipher.doFinal(hexStringToByteArray(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    // utilice este ///
    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: "
                    + Base64.encodeToString(encrypted, Base64.DEFAULT));

            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
