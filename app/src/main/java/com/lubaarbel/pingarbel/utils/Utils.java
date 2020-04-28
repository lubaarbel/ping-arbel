package com.lubaarbel.pingarbel.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String formatDateString(long timeMillis) {
        Date date = new Date(timeMillis);
        SimpleDateFormat formatter = new SimpleDateFormat(
                "YYYY-MM-DD HH:MM:SS",
                Locale.getDefault());
        return formatter.format(date);
    }
}
