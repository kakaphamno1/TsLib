package com.tsolution.base.utils.locale;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;

import java.util.Locale;

public class LocaleUtils {
    private static Locale sLocale;

    public static void setLocale(Locale locale) {
        sLocale = locale;
        if (sLocale != null) {
            Locale.setDefault(sLocale);
        }
    }

    public static Locale getsLocale() {
        return sLocale;
    }

    public static void updateConfig(ContextThemeWrapper wrapper) {
        if (sLocale != null) {
            Configuration configuration = new Configuration();
            configuration.setLocale(sLocale);
            wrapper.applyOverrideConfiguration(configuration);
        }
    }

    //    @Suppress("DEPRECATION")
    public static void updateConfig(Application app, Configuration configuration) {
        if (sLocale != null) {
            //Wrapping the configuration to avoid Activity endless loop
            Configuration config = new Configuration(configuration);
            config.locale = sLocale;
            Resources res = app.getBaseContext().getResources();
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
    }
}
