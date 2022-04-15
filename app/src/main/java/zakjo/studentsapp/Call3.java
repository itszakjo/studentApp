package zakjo.studentsapp;



import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import zakjo.studentsapp.Helpers.SinchCallClientListener;
import zakjo.studentsapp.Helpers.SinchCallListener;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.User;

import static zakjo.studentsapp.Utils.Constants.APP_KEY;
import static zakjo.studentsapp.Utils.Constants.APP_SECRET;
import static zakjo.studentsapp.Utils.Constants.ENVIRONMENT;
import static zakjo.studentsapp.Utils.Constants.callBtn;
import static zakjo.studentsapp.Utils.Constants.mediaPlayer;
import static zakjo.studentsapp.Utils.Constants.vnn;


public class Call3 extends AppCompatActivity {


    private ImageView stop , end_call,   accept , reject , userPic ;

    String userPhone, myPhone , callerPhone ;




    private SinchClient sinchClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call3);

        // to open activity even if the screen is locked

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
//                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
//                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
//                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        end_call = (ImageView) findViewById(R.id.end_call);
        accept = (ImageView) findViewById(R.id.accept);
        reject = (ImageView) findViewById(R.id.reject);
        stop = (ImageView) findViewById(R.id.stop);

        Constants.state = (TextView) findViewById(R.id.status);

        FirebaseAuth auth = FirebaseAuth.getInstance();


        assert Constants.currentUser.getPhone() != null;

        try{

            Constants.userPhone = Constants.currentUser.getPhone();

        }catch (Exception e){

            e.printStackTrace();

        }

        myPhone = auth.getCurrentUser().getPhoneNumber();

        Intent intent = getIntent();
        String MSG  = intent.getStringExtra("INTENT");

        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(myPhone)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener(Call3.this , this));

        if(MSG.equals("FROMSG")){

            accept.setVisibility(View.GONE);
            reject.setVisibility(View.GONE);
            end_call.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);

        }else if (MSG.equals("FROMINCOMING")){


            // receievees a notifcation including phone number of the opponent


            if(Constants.userPhone !=null){

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(Constants.userPhone);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if(user.getTyping_to().equals(myPhone) && user.getStatus().equals("calling")){

                            Constants.state.setText(user.getPhone() +" is calling");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }




            Constants.state.setText("Someone is calling");



//
//          vnn.vibrate(Constants.pattern, 0);

            accept.setVisibility(View.VISIBLE);
            reject.setVisibility(View.VISIBLE);
            end_call.setVisibility(View.GONE);
            stop.setVisibility(View.GONE);

        }

        //        koftaMediaPlayer = new MediaPlayer();
//        koftaMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        try {
//
//            koftaMediaPlayer.setDataSource(call3.this ,koftaUri);
//            koftaMediaPlayer.prepare();
//
//        }catch (IOException e){
//
//            e.printStackTrace();
//        }
//        koftaMediaPlayer.start();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                accept.setVisibility(View.GONE);
                reject.setVisibility(View.GONE);
                end_call.setVisibility(View.VISIBLE);

                if(Constants.call != null){

                    Constants.call.answer();
                    setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                    Constants.call.addCallListener(new SinchCallListener(Call3.this , Call3.this));

                    if(vnn !=null){

                        vnn.cancel();
                    }

                }else {

                    Toast.makeText(Call3.this, "Call Failed !", Toast.LENGTH_SHORT).show();
                    if(vnn !=null){

                        vnn.cancel();
                    }
                }
//                if(koftaMediaPlayer != null){
//
//                    koftaMediaPlayer.stop();
//
//                }
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(Constants.call !=null){

                    Constants.call.hangup();
                }
                if(vnn !=null){

                    vnn.cancel();
                }

                Intent intent = new Intent(Call3.this , Messaging.class);
                startActivity(intent);
                finish();

                //                if(koftaMediaPlayer != null){
//
//                    koftaMediaPlayer.stop();
//
//                }

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Call3.this , Messaging.class);
                startActivity(intent);
                finish();
                if(Constants.call !=null){
                    Constants.call.hangup();
                }
            }
        });

        end_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Call3.this , Messaging.class);
                startActivity(intent);
                finish();
                if(Constants.call !=null){
                    Constants.call.hangup();
                }
            }
        });




    }

    private void status(String status){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myPhone);

        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("status" , status);

        databaseReference.updateChildren(hashMap);

    }


    @Override
    protected void onResume() {
        super.onResume();
        status("calling");
        Constants.isCall3active = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Constants.call !=null){
            Constants.call.hangup();
        }
        Constants.isCall3active = false;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Constants.call !=null){
            Constants.call.hangup();
        }
        Constants.isCall3active = false;
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(Constants.call !=null){
            Constants.call.hangup();
        }
        finish();
    }

    //    private class SinchCallListener implements CallListener {
//        @Override
//        public void onCallProgressing(Call call) {
//
//            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
//
//            mediaPlayer = new MediaPlayer();
//            try {
//
//                mediaPlayer.setDataSource(Call3.this ,Constants.uri);
//                mediaPlayer.prepare();
//
//            }catch (IOException e){
//
//                e.printStackTrace();
//            }
//
//            mediaPlayer.start();
//        }
//
//        @Override
//        public void onCallEstablished(Call call) {
//
//            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
//
//            if(mediaPlayer != null){
//
//                mediaPlayer.stop();
//            }
//        }
//
//        @Override
//        public void onCallEnded(Call endedCall) {
//
//            Constants.call = null;
//            SinchError a = endedCall.getDetails().getError();
//
//            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
//
//            Intent intent = new Intent(Call3.this , Messaging.class);
//            startActivity(intent);
//            finish();
//
//            if(mediaPlayer != null){
//                mediaPlayer.stop();
//            }
//
//        }
//        @Override
//        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
//
//
//
//        }
//    }

//    private class SinchCallClientListener implements CallClientListener {
//
//        @Override
//        public void onIncomingCall(CallClient callClient, Call incomingCall) {
//
//            Constants.call.addCallListener(new CallListener() {
//                @Override
//                public void onCallProgressing(Call call) {
//
//
//                }
//
//                @Override
//                public void onCallEstablished(Call call) {
//
//
//                }
//                @Override
//                public void onCallEnded(Call call) {
//
////                    Constants.vnn.cancel();
//
////                    startActivity(new Intent(Call3.this , Messaging.class));
////
////                    Toast.makeText(Call3.this, "From Call 3 stop", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onShouldSendPushNotification(Call call, List<PushPair> list) {
//
//                }
//            });
//
//
//        }
//    }


}

