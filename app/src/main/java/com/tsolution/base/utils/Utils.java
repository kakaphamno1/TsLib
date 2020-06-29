package com.tsolution.base.utils;

import java.util.Collections;
import java.util.List;

public class Utils {
    public static <T> T nvl(T in, T defaultValue){
        return in == null?defaultValue:in;
    }
    public static List safe(List other) {

        return other == null ? Collections.EMPTY_LIST : other;
    }
}
