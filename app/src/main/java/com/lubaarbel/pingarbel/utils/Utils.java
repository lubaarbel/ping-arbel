package com.lubaarbel.pingarbel.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * App general static utils funcs, like
 * date format, filing system...
 * **/
public class Utils {

    /** app constants **/
    // in bigger project, it should be in a separate class
    public static final String PUSH_NOTIFICATION_INTENT_ACTION = "OPEN_ACTIVITY_1";
    public static final String PUSH_NOTIFICATION_INTENT_DATA_KEY = "userInput";
    public static final String PUSH_NOTIFICATION_TOPIC = "input";


    public static String formatDateString(long timeMillis) {
        Date date = new Date(timeMillis);
        SimpleDateFormat formatter = new SimpleDateFormat(
                "YYYY-MM-DD HH:MM:SS",
                Locale.getDefault());
        return formatter.format(date);
    }

    public static void writeToFile(String path, byte[] value) {
        try {
            File file = new File(path);
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(value);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] readFromFile(String path) {
        File file = new File(path);
        byte[] result = new byte[(int) file.length()];
        try {
            FileInputStream fis;
            fis = new FileInputStream(file);
            fis.read(result);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
