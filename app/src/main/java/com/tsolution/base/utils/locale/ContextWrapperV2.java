package com.tsolution.base.utils.locale;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;
import android.util.Log;

import java.util.Locale;

public class ContextWrapperV2 extends ContextWrapper {

    public ContextWrapperV2(Context base) {
        super(base);
    }

    public static ContextWrapperV2 wrap(Context context1, Locale newLocale) {

        Resources res = context1.getResources();
        Configuration configuration = res.getConfiguration();

        if (BuildUtils.isAtLeast24Api) {
            configuration.setLocale(newLocale);
            try {
                LocaleList localeList = new LocaleList(newLocale);
                LocaleList.setDefault(localeList);
                configuration.setLocales(localeList);
                context1 = context1.createConfigurationContext(configuration);
            } catch (NullPointerException e) {
                Log.e("Error config location: ", e.getMessage());
            } catch (IllegalArgumentException e) {
                Log.e("Error config location: ", e.getMessage());
            }

        } else {
            configuration.setLocale(newLocale);
            context1 = context1.createConfigurationContext(configuration);
        }

        return new ContextWrapperV2(context1);
    }
}
