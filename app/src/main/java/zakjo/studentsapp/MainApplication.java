package zakjo.studentsapp;

import android.app.Application;
import android.content.Context;

import zakjo.studentsapp.Helpers.LocaleHelper;


public class MainApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}

