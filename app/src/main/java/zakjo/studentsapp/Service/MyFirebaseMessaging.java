package zakjo.studentsapp.Service;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import zakjo.studentsapp.Call3;
import zakjo.studentsapp.R;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.Helpers.NotificationHelper;


public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        try{

            if(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() !=null){

                updateTokenToFirebase(s);
            }

        }catch (Exception e){

            e.printStackTrace();
        }

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData() !=null ){

            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                sendNotificationAPI26(remoteMessage);
            }else{
                sendNotification(remoteMessage);
            }


            //SharedPrefManager.KEY_STUDENT_NAME = null;
            Map<String, String> data = remoteMessage.getData(); // get data from the notify

            try {

                Constants.userPhone  = data.get("userphone");

                Constants.imcalling_status  = data.get("call_status");

                if(Constants.imcalling_status.equals("ImCalling")){

                    if(!Constants.isCall3active){

                        Intent intent = new Intent(getApplicationContext(), Call3.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("INTENT" , "FROMINCOMING");
                        startActivity(intent);

                    }
                }

            }catch (Exception e){e.printStackTrace();}


            // if u wish to make toasts or some other funcs
//            Handler handler = new Handler(Looper.getMainLooper());
//
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//
//
//                }
//            }, 1000 );

        }

    }


    private void sendNotification(RemoteMessage remoteMessage) {
        Map<String, String> data  = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("message");
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notifi = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notifi.notify(new Random().nextInt() , builder.build());
    }

    private void sendNotificationAPI26(RemoteMessage remoteMessage) {

        Map<String, String> data  = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("message");
        String usephone = data.get("userphone");

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationHelper helper ;
        NotificationCompat.Builder builder ;

        helper = new NotificationHelper(this);
        builder = helper.getCauNotification(title, message, defaultSoundUri , usephone);

        helper.getManager().notify(new Random().nextInt() , builder.build());
    }

    private void updateTokenToFirebase(String token) {

//        MyLabAPI mService = Constants.getAPI();
//        mService.updateToken(
//                SharedPrefManager.getInstance(this).getKeyStudentResponsiblePhone(),
//                token ,
//                "0")
//                .enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//
//                        Log.d("DEBUG" , response.toString());
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//
//                        Log.d("DEBUG" , t.getMessage());
//                    }
//                });

        // insert if not exists , and update if it exists
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("phone" , FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        hashMap.put("token" , token);
        hashMap.put("isServerToken" , "0");

        reference.updateChildren(hashMap);

    }

}
