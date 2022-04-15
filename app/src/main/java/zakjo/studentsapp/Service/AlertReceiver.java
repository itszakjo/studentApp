package zakjo.studentsapp.Service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


public class AlertReceiver extends BroadcastReceiver {



    public MediaPlayer mediaPlayer ;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onReceive(final Context context, Intent intent) {

//        NotificationHelper notificationHelper = new NotificationHelper(context);
//
//        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
//        notificationHelper.getManager().notify(1, nb.build());
//
//        context.startActivity(new Intent(context , Bahy.class));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Toast.makeText(context, "Alarm is working !!", Toast.LENGTH_SHORT).show();

         mediaPlayer = MediaPlayer.create(context , Settings.System.DEFAULT_RINGTONE_URI);
         mediaPlayer.start();

//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
//        r.play();
//











    }



}