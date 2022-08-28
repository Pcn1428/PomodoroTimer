package com.example.myapplication;

import java.util.Locale;

public class HelperMethods {
    public static String MillisToString (long m) {
        int minutes = (int) (m / 1000) / 60;
        int seconds = (int) (m / 1000) % 60;

        String millisFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        return millisFormatted;
    }

    public static long StringToMillis (String s) {
        int value = Integer.parseInt(s);

        long millisFormatted = (long) value * 60000;

        return millisFormatted;
    }

    public static long CountdownToMillis (String s) {
        String[] arrOfS = s.split(":",2);
        long minutes = (long) Integer.parseInt(arrOfS[0]) * 60000;
        long seconds = (long) Integer.parseInt(arrOfS[1]) * 1000;

        long millisFormatted = minutes + seconds;

        return millisFormatted;
    }
}
