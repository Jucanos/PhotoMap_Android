package com.jucanos.photomap.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateString {
    public static String getString(Date date) {
        Date now = getCurrentDate();
        String ret = null;
        long calDate = now.getTime() - date.getTime();

        long calDateSecond = calDate / 1000;
        Log.e("calDateSecond", "" + calDateSecond);
        long calDateMin = calDate / (60 * 1000);
        Log.e("calDateMin", "" + calDateMin);
        long calDateHour = calDate / (60 * 60 * 1000);
        Log.e("calDateHour", "" + calDateHour);
        long calDateDay = calDate / (60 * 60 * 24 * 1000);
        Log.e("calDateDay", "" + calDateDay);
        long calDateMonth = calDate / (60 * 60 * 24 * 30 * 1000);
        long calDateYear = calDate / (60 * 60 * 24 * 30 * 12 * 1000);

//
//        else if (calDateMonth > 0) {
//            ret = calDateMonth + "달전";
//        } else if (calDateDay > 0) {
//            ret = calDateDay + "달전";
//        }

        if (calDateDay > 0) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
            inputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            ret = inputFormat.format(date);
        } else if (calDateHour > 0) {
            ret = calDateHour + "시간전";
        } else if (calDateMin > 0) {
            ret = calDateMin + "분전";
        } else if (calDateSecond >= 0) {
            ret = calDateSecond + "초전";
        }
        return ret;
    }

    public static Date getCurrentDate() {
        Date now = new Date(System.currentTimeMillis());
        return now;
    }

}
