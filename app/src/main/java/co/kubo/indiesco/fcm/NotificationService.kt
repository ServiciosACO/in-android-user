package co.kubo.indiesco.fcm

import android.app.Notification.DEFAULT_SOUND
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import co.kubo.indiesco.R
import co.kubo.indiesco.activities.Splash
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by estacion on 1/06/18.
 */
class NotificationService : FirebaseMessagingService() {

    val TAG = "FIREBASE"
    val NOTIFICATION_ID = 1

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        //super.onMessageReceived(remoteMessage)
        if (remoteMessage!!.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }
        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
        }
        try {
            sendNotification(remoteMessage.notification!!.body)
        } catch (e: Exception) {
            Log.e(TAG, "Exception push")
        }
    }

    private fun sendNotification(messageBody: String?) {
        val intent = Intent(this, Splash::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val largeIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(messageBody)
                .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
                .setDefaults(DEFAULT_SOUND)
                .setVibrate(longArrayOf(1000, 2000, 3000, 1000, 3000))
                //.setSound(sound) // El sonido, declaro un objeto tipo Uri
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(81323, notificationBuilder.build())
    }
}