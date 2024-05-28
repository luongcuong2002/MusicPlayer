package com.kma.musicplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SystemUtil {
    private static Locale myLocale;

    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    // Load lại ngôn ngữ đã lưu và thay đổi chúng
    public static void setLocale(Context context) {
        String language = getPreLanguage(context);
        if (language.equals("")) {
            Configuration config = new Configuration();
            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);
            config.locale = locale;
            context.getResources()
                    .updateConfiguration(config, context.getResources().getDisplayMetrics());
        } else {
            changeLang(language, context);
        }
    }

    // method phục vụ cho việc thay đổi ngôn ngữ.
    public static void changeLang(String lang, Context context) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(context, lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static void saveLocale(Context context, String lang) {
        setPreLanguage(context, lang);
    }

    public static String getPreLanguage(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE);
        Locale.getDefault().getDisplayLanguage();
        String lang;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lang = Resources.getSystem().getConfiguration().getLocales().get(0).getLanguage();
        } else {
            lang = Resources.getSystem().getConfiguration().locale.getLanguage();
        }
        if (!getLanguageApp(mContext).contains(lang)) {
            return preferences.getString("KEY_LANGUAGE", "en");
        } else {
            return preferences.getString("KEY_LANGUAGE", lang);
        }
    }

    public static void setPreLanguage(Context context, String language) {
        if (language == null || language.equals("")) {
            return;
        } else {
            SharedPreferences preferences = context.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE);
            preferences.edit().putString("KEY_LANGUAGE", language).apply();
        }
    }

    public static void increaseCountChooseLanguage(Context context){
        SharedPreferences preferences = context.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("count_choose", preferences.getInt("count_choose", 0)+1);
        editor.commit();
    }
    public static int getCountChooseLanguage(Context context){
        SharedPreferences preferences = context.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE);
        return preferences.getInt("count_choose", 0);
    }

    public static List<String> getLanguageApp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MY_PRE", Context.MODE_PRIVATE);
        boolean openLanguage = sharedPreferences.getBoolean("openLanguage", false);
        List<String> languages = new ArrayList<>();
        languages.add("en");
        languages.add("hi");
        languages.add("es");
        languages.add("fr");
        languages.add("de");
        languages.add("in");
        languages.add("pt");
        return languages;
    }

}
