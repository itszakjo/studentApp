package zakjo.studentsapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import java.util.concurrent.TimeUnit;

import zakjo.studentsapp.Utils.Constants;


public class ZakjoAudioPlayer extends AppCompatActivity implements InternetConnectivityListener {

    VideoView customizedVideo ;

    Button stop ;
    ImageView playPauseRestart  ;
    SeekBar seekBar;
    TextView duration , position ;

    int oTime = 0 , sTime = 0 , eTime = 0 , seekPosition = 0  , lastPosition  =0;
    String ssss , mmmm ;

    boolean beenOut = false , notconnected = false ;

    public Handler handler = new Handler();

    final MediaPlayer mediaPlayer = new MediaPlayer();

    InternetAvailabilityChecker mInternetAvailabilityChecker ;

    ProgressBar buffering  ;

    RelativeLayout overlay ;

    String clickStatus  = "Play" ;

    Intent intent ;

    String vLINK = "" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zakjoaudio_player);

        InternetAvailabilityChecker.init(this);

        intent  = getIntent();

        vLINK  = intent.getStringExtra("AUDIOLINK");

        playPauseRestart = findViewById(R.id.playPauseRestart);

        overlay = findViewById(R.id.overlay);

        stop = findViewById(R.id.stop);

        buffering = findViewById(R.id.buffering);

        seekBar = findViewById(R.id.seek);

        duration = findViewById(R.id.duration);

        position = findViewById(R.id.position);

        customizedVideo = findViewById(R.id.customizedVideo);

        customizedVideo.setVideoPath(vLINK);

        eTime = mediaPlayer.getDuration();
        sTime = mediaPlayer.getCurrentPosition();

        if(oTime == 0){
            seekBar.setMax(eTime);
            oTime =1;
        }
        customizedVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                seekBar.setMax(customizedVideo.getDuration());

                eTime = customizedVideo.getDuration();

                long Endminutes  = TimeUnit.MILLISECONDS.toMinutes(eTime);
                long EndSeconds = TimeUnit.MILLISECONDS.toSeconds(eTime)  - TimeUnit.MINUTES.toSeconds(Endminutes);

                if(EndSeconds < 10 ){ssss = "0"+ String.valueOf(EndSeconds) ; }
                else { ssss = String.valueOf(EndSeconds) ; }

                mmmm = String.valueOf(Endminutes) ;

                duration.setText(mmmm+":"+ssss );

                position.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(sTime),
                        TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );

            }
        });

        // to stop showing can't play this video
        customizedVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });

        playPauseRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(clickStatus.equals("Play")){

                    clickStatus = "Pause";
                    playPauseRestart.setImageResource(R.drawable.ic_pause_black_24dp);
                    playVideo();

                }else if(clickStatus.equals("Pause")) {

                    clickStatus = "Play";
                    playPauseRestart.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    pauseVideo();

                }else{

                    restartVideo();
                    clickStatus = "Pause";
                    playPauseRestart.setImageResource(R.drawable.ic_pause_black_24dp);

                }

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopVideo();
            }

        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekPosition = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                customizedVideo.seekTo(seekPosition);
            }
        });

        customizedVideo.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {

                        buffering.setVisibility(View.GONE);
                        playPauseRestart.setVisibility(View.VISIBLE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                        buffering.setVisibility(View.VISIBLE);
//                        playPauseRestart.setVisibility(View.GONE);
                        playPauseRestart.setImageResource(R.drawable.ic_pause_black_24dp);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END: {

                        buffering.setVisibility(View.GONE);
//                        playPauseRestart.setVisibility(View.VISIBLE);
                        playPauseRestart.setImageResource(R.drawable.ic_pause_black_24dp);


                        return true;
                    }
                }
                return false;
            }
        });

        customizedVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                clickStatus = "Restart";
                playPauseRestart.setImageResource(R.drawable.ic_refresh_black_24dp);

            }
        });


        customizedVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                overlay.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                        overlay.setVisibility(View.GONE);

                    }
                }, 10000);

            }
        });

        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                overlay.setVisibility(View.GONE);
            }
        });


        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);

    }

    private void restartVideo() {

        if(Constants.isConnectedToInternet(this)){

            buffering.setVisibility(View.GONE);

            customizedVideo.stopPlayback();
            customizedVideo.setVideoPath(vLINK);
            customizedVideo.start();

        }else {

            Toast.makeText(this, "No Internet !", Toast.LENGTH_SHORT).show();
            buffering.setVisibility(View.VISIBLE);

        }

    }

    private void playVideo() {

        if(Constants.isConnectedToInternet(this)){
            seekBar.setProgress(sTime);
            handler.postDelayed(UpdateTime, 100);
            if(customizedVideo.isPlaying()){

                customizedVideo.resume();
                buffering.setVisibility(View.GONE);

            }
            else{
                customizedVideo.start();
                buffering.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                        overlay.setVisibility(View.GONE);
                    }
                }, 10000);
            }


            clickStatus  = "Pause";
            playPauseRestart.setImageResource(R.drawable.ic_pause_black_24dp);



        }else {

            Toast.makeText(this, "No Internet !", Toast.LENGTH_SHORT).show();
            buffering.setVisibility(View.VISIBLE);
        }

    }

    private void stopVideo() {
        if(customizedVideo.isPlaying()){
            customizedVideo.stopPlayback();
            customizedVideo.setVideoPath(vLINK);
            handler.removeCallbacksAndMessages(null);

        }
    }

    private void pauseVideo() {
        if(customizedVideo !=null){
            if(customizedVideo.isPlaying()){
                customizedVideo.pause();
                handler.removeCallbacksAndMessages(null);
                clickStatus = "Play" ;
                playPauseRestart.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            }
        }

    }

    private void resumeVideo() {
        if (Constants.isConnectedToInternet(this)){

            customizedVideo.seekTo(lastPosition);
            handler.postDelayed(UpdateTime, 100);
            customizedVideo.start();
            clickStatus = "Pause";
            playPauseRestart.setImageResource(R.drawable.ic_pause_black_24dp);


//            overlay.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

//                    overlay.setVisibility(View.GONE);
                }
            }, 10000);

        }else {

            Toast.makeText(this, "No Internet ! ", Toast.LENGTH_SHORT).show();
            buffering.setVisibility(View.VISIBLE);
            playPauseRestart.setImageResource(R.drawable.ic_play_arrow_black_24dp);


        }

    }


    public Runnable UpdateTime = new Runnable()  {
        @Override
        public void run() {

            String sss , mmm ;
            sTime = customizedVideo.getCurrentPosition();

            long minutes  = TimeUnit.MILLISECONDS.toMinutes(sTime);

            long Seconds = TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime));

            if(Seconds < 10 ){

                sss = "0"+ String.valueOf(Seconds) ;
            }
            else { sss = String.valueOf(Seconds) ; }

            mmm = String.valueOf(minutes) ;

            position.setText(mmm+":"+sss );

            seekBar.setProgress(sTime);


            handler.postDelayed(this, 100);
        }

    };


    @Override
    protected void onPause() {
        super.onPause();

        lastPosition =customizedVideo.getCurrentPosition();
        pauseVideo();
        beenOut = true ;

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(beenOut){
            resumeVideo();
            beenOut = false ;
        }else{
            playVideo();
        }
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if(isConnected && notconnected){
            playVideo();
            buffering.setVisibility(View.GONE);
//            playPauseRestart.setVisibility(View.VISIBLE);
            notconnected = false ;
        }else {
            notconnected = true;
            pauseVideo();
            if(buffering !=null){
                buffering.setVisibility(View.VISIBLE);
            }
//            if(playPauseRestart !=null){
////                playPauseRestart.setVisibility(View.GONE);
//
//            }
        }

    }
}


