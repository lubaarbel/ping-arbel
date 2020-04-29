package com.lubaarbel.pingarbel.utils;

import android.content.Context;

import com.lubaarbel.pingarbel.AppHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Base64;
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
//    public static void writeToFile(String path, byte[] value) {
//        String data = Base64.getEncoder().encodeToString(value);
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
//                    AppHolder.getContext().openFileOutput(path, Context.MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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
