package co.kubo.indiesco.fcm

import android.util.Log
import co.kubo.indiesco.utils.SharedPreferenceManager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by estacion on 1/06/18.
 */
class NotificacionIDTokenService : FirebaseInstanceIdService() {

    private val TAG = "FIREBASE_TOKEN"
    val sharedPreferenceManager = SharedPreferenceManager()

    override fun onTokenRefresh() {
        //super.onTokenRefresh()
        Log.d(TAG, "Solicitando token")
        val token = FirebaseInstanceId.getInstance().token
        sharedPreferenceManager.setFirebaseToken(applicationContext, token)
        sendRegistrationToServer(token!!)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d(TAG, token)
    }
}