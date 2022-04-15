package zakjo.studentsapp.Helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helpers.Selected.Langauge";


    public static Context onAttach(Context context){

        String lang = getPresistedData(context , Locale.getDefault().getLanguage());

        return setLocale(context , lang);
    }


    public static Context onAttach(Context context , String defaultLanguage){

        String lang = getPresistedData(context , defaultLanguage);

        return setLocale(context , lang);
    }


    public static Context setLocale(Context context , String lang){


        presist(context, lang);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N)
            return updateRescources(context , lang);

        return updateresourcesLegacy(context , lang);

    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateRescources(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(Locale.getDefault());

        return context.createConfigurationContext(config);
    }


    @SuppressWarnings("deprecation")
    private static Context updateresourcesLegacy(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);


        Resources resources = context.getResources();

        Configuration config  = resources.getConfiguration();
        config.locale = locale;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1);

        config.setLayoutDirection(Locale.getDefault());

        resources.updateConfiguration(config, resources.getDisplayMetrics());

        return context ;


    }

    private static void presist(Context context, String lang) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(SELECTED_LANGUAGE, lang);
        editor.apply();
    }

    private static String getPresistedData(Context context , String language){


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getString(SELECTED_LANGUAGE ,language);

    }
}
