package zakjo.studentsapp.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;

import java.io.IOException;
import java.util.List;

import zakjo.studentsapp.Messaging;
import zakjo.studentsapp.Utils.Constants;


public class SinchCallListener implements CallListener {

    Context context ;
    Activity activity ;


    public SinchCallListener(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }


    @Override
    public void onCallProgressing(Call call) {


//     setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);

        Constants.mediaPlayer = new MediaPlayer();
        try {

            Constants.mediaPlayer.setDataSource(context,Constants.uri);
            Constants.mediaPlayer.prepare();

        }catch (IOException e){

            e.printStackTrace();
        }

        Constants.mediaPlayer.start();

        Constants.state.setText("ringing");

    }

    @Override
    public void onCallEstablished(Call call) {

        Constants.state.setText("connected");


//      setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        if(Constants.mediaPlayer != null){

            Constants.mediaPlayer.stop();

        }

    }

    @Override
    public void onCallEnded(Call endedCall) {

        Constants.call = null;

        Constants.state.setText("Call Ended");

        context.startActivity(new Intent(context , Messaging.class));
        activity.finish();


//      setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);

        if(Constants.mediaPlayer != null){

            Constants.mediaPlayer.stop();

        }

    }
    @Override
    public void onShouldSendPushNotification(Call call, List<PushPair> list) {

    }
}
