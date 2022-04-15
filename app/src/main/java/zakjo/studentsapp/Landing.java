package zakjo.studentsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Landing extends AppCompatActivity {

    ImageView landningImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);


        landningImage = (ImageView) findViewById(R.id.landingImg);

        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.transition);
        landningImage.startAnimation(myAnim);
        final Intent i = new Intent(this ,MainActivity.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    startActivity(i);
                    finish();
                }}
        };

        timer.start();
    }
}
