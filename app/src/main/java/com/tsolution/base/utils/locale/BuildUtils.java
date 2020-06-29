package com.tsolution.base.utils.locale;

import android.os.Build;

public class BuildUtils {
    public static Boolean isAtLeast24Api = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    public static Boolean isNougat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
}
