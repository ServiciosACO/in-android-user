package co.kubo.indiesco.activities;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by estacion on 21/02/18.
 */

class NotificacionIDTokenService extends FirebaseInstanceIdService {

    private static final String TAG = "FIREBASE TOKEN";

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        Log.d(TAG, "Solicitando token");
        String token = FirebaseInstanceId.getInstance().getToken();
    }//public void onTokenRefresh

    private void sendRegistrationToServer(String token){
        Log.d(TAG, token);
    }//private void sendRegistrationToServer

}//public class NotificacionIDTokenService