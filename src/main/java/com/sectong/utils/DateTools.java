package com.sectong.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xueyong on 16/9/28.
 * mobileeasy.
 */
public class DateTools {

    private static final int FIRST_DAY = Calendar.MONDAY;

    public static String[] printWeekdays() {
        Calendar calendar = Calendar.getInstance();
        String[] weeks = new String[7];
        setToFirstDay(calendar);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        weeks[0] = dateFormat.format(calendar.getTime());
        for (int i = 1; i < 7; i++) {
//            printDay(calendar);
            calendar.add(Calendar.DATE, 1);
            weeks[i] = dateFormat.format(calendar.getTime());
        }
        return weeks;
    }

    private static void setToFirstDay(Calendar calendar) {
        while (calendar.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
            calendar.add(Calendar.DATE, -1);
        }
    }

    private static void printDay(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd EE");
        System.out.println(dateFormat.format(calendar.getTime()));
    }

    public static Date getGMT8Time() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(sdf.format(new Date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.HOUR, 8);

        return calendar.getTime();
    }
}
