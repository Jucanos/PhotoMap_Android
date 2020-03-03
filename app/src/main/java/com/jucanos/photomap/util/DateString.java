package com.jucanos.photomap.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateString {
    public static String getString(Date date) {
        String ret = "";
        long now, nowSecond, nowMin, nowHour, nowDay, nowMonth, nowYear;
        long m, mSecond, mMin, mHour, mDay, mMonth, mYear;
        now = getCurrentDate().getTime();
        nowSecond = now / 1000;
        nowMin = now / (1000 * 60);
        nowHour = now / (1000 * 60 * 60);
        nowDay = now / (1000 * 60 * 60 * 24);
        nowMonth = now / (1000 * 60 * 60 * 24 * 30);
        nowYear = now / (1000 * 60 * 60 * 24 * 30 * 12);

        m = date.getTime();
        mSecond = m / 1000;
        mMin = m / (1000 * 60);
        mHour = m / (1000 * 60 * 60);
        mDay = m / (1000 * 60 * 60 * 24);
        mMonth = m / (1000 * 60 * 60 * 24 * 30);
        mYear = m / (1000 * 60 * 60 * 24 * 30 * 12);

        // 같은 날인경우
        if (mDay == nowDay) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("오늘 aaa h:m", Locale.KOREA);
            inputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            ret = inputFormat.format(date);
        } else if (mDay + 1 == nowDay) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("어제 aaa h:m", Locale.KOREA);
            inputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            ret = inputFormat.format(date);
        } else if (mMonth == nowMonth) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("d일 h:m", Locale.KOREA);
            inputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            ret = inputFormat.format(date);
        } else if (mYear == nowYear) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("M월d일", Locale.KOREA);
            inputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            ret = inputFormat.format(date);
        } else {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yy년M월d일", Locale.KOREA);
            inputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            ret = inputFormat.format(date);
        }
        return ret;
    }

    public static Date getCurrentDate() {
        Date now = new Date(System.currentTimeMillis());
        return now;
    }

}
