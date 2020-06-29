package com.tsolution.base.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;


import java.util.Locale;

public class LanguageUtils {
    public static String[] LANGUAGE_CODE = {"vi", "en", "my"};
    public static Long languageId = 1L;

    /**
     * load current locale and change language
     */
    public static void setLocale(Context context) {
        String code = LANGUAGE_CODE[context.getSharedPreferences("A31b4c24l3kj35d4AKJQ", Context.MODE_PRIVATE)
                .getInt("current_language", 0)];
        changeLanguage(context, code);
    }

    /**
     * change app language
     */
    public static void changeLanguage(Context context, String language) {
        if (language.contains("vi")) {
            languageId = 1L;
        } else if (language.contains("en")) {
            languageId = 2L;
        } else if (language.contains("my")) {
            languageId = 3L;
        }
        updateResources(context, language);
    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}
