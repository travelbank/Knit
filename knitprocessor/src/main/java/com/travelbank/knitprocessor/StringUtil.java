package com.travelbank.knitprocessor;

/**
 * Created by omerozer on 5/15/18.
 */

public class StringUtil {

    public static String firstLetterToCaps(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1, string.length());
    }

}
