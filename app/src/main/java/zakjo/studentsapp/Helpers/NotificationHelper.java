package zakjo.studentsapp.Helpers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import zakjo.studentsapp.MainForChat;
import zakjo.studentsapp.Messaging;
import zakjo.studentsapp.R;


public class NotificationHelper extends ContextWrapper {

    private static final String ZAKJO_CHANNEL_ID = "instaclone.instagramclone.Cuabarber";
    private static final String ZAKJO_CHANNEL_NAME = "Caubarber";

    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager notificationManager ;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationChannel channel  = new NotificationChannel(ZAKJO_CHANNEL_ID, ZAKJO_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if(notificationManager == null )
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public NotificationCompat.Builder getCauNotification(String title , String msg  , Uri soundUri  , String userPho){

        Intent resultIntent = new Intent(this , Messaging.class);
        resultIntent.putExtra("USERPHONE" , userPho);
        resultIntent.putExtra("CHATYPE" ,"ONE");

        PendingIntent resultPendingIntent  = PendingIntent.getActivity(this , 1 , resultIntent , PendingIntent.FLAG_UPDATE_CURRENT);

        return  new NotificationCompat.Builder(getApplicationContext(),ZAKJO_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(resultPendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSound(soundUri)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
    }

    // tempo func
    public NotificationCompat.Builder getChannelNotification() {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Alarm!")
                .setContentText("Your AlarmManager is working.")
                .setSmallIcon(R.drawable.ic_addwa);
    }


}
