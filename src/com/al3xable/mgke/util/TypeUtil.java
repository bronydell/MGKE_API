package com.al3xable.mgke.util;

import org.apache.commons.io.input.CharSequenceReader;

public class TypeUtil {

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }

    public static String trim(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("\u00A0", "").trim();
    }
}
