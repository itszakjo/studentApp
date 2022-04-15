package zakjo.studentsapp.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

import zakjo.studentsapp.Call3;
import zakjo.studentsapp.Messaging;
import zakjo.studentsapp.Utils.Constants;

import static zakjo.studentsapp.Utils.Constants.getVibrator;
import static zakjo.studentsapp.Utils.Constants.vnn;

public class SinchCallClientListener implements CallClientListener {

    Context context ;
    Activity activity;


    public SinchCallClientListener(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onIncomingCall(CallClient callClient, Call incomingCall) {

        Constants.call = incomingCall;

//      context.startActivity(new Intent(context , Call3.class));

        Intent intent = new Intent(context , Call3.class);
        intent.putExtra("INTENT" , "FROMINCOMING");

        vnn = getVibrator(context);
        vnn.vibrate(Constants.pattern, 0);

        context.startActivity(intent);
        activity.finish();

        Constants.call.addCallListener(new CallListener() {
            @Override
            public void onCallProgressing(Call call) {

            }
            @Override
            public void onCallEstablished(Call call) {

                Constants.state.setText("Connected");
            }
            @Override
            public void onCallEnded(Call call) {

                Constants.state.setText("Call Ended");

                Constants.vnn.cancel();

                context.startActivity(new Intent(context , Messaging.class));
                activity.finish();

            }
            @Override
            public void onShouldSendPushNotification(Call call, List<PushPair> list) {

            }
        });
    }
}
