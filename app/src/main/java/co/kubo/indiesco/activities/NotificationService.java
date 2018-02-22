package co.kubo.indiesco.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by estacion on 21/02/18.
 */

public class NotificationService extends FirebaseMessagingService {
    public static final String TAG = "FIREBASE";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        //No implementado
        /**Para que no se acumule la misma activity al recibir la notificacion en foreground, tambien se agrego al Manifest file lo siguiente:
         android:launchMode="singleTask"
         android:taskAffinity=""
         android:excludeFromRecents="true"*/

        Intent in = new Intent(this, MisNotificaciones.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this) //trabajamos con la V4 de NotificationCompact
                //.setSmallIcon(R.drawable.ic_catprofile) //Para configurar el icono
                .setContentTitle("Notificacion") // el titulo
                .setContentText(remoteMessage.getNotification().getBody()) // el mensaje
                .setVibrate(new long[] {1000,2000,3000,1000,3000})
                .setSound(sonido) // El sonido, declaro un objeto tipo Uri
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                ;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification.build());
    }
}
