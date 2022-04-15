package zakjo.studentsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class PlatForm extends AppCompatActivity {

    WebView platform_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plat_form);

        WebView myWebView = (WebView) findViewById(R.id.platform_view);
        myWebView.loadUrl("https://www.test2.rozewail.com/student/login");
    }
}