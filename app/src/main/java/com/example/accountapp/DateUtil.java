package com.example.accountapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    //返回当前时间 小时：分
    public static String getFormattedTime(long timeStamp){

        SimpleDateFormat format = new SimpleDateFormat("hh:mm");

        return format.format(new Date(timeStamp));

    }

    public static String getFormattedDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
        return format.format(new Date());
    }

    private static Date strToDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static String getWeekDay(String date){
        String[] weekdays = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strToDate(date));
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekdays[weekDay];

    }

    public static String getDateTitle(String date){
        String[] months = {"1","2","3","4","5","6","7","8","9","10","11","12"};

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(strToDate(date));
        int monthIndex = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return months[monthIndex] + " " + String.valueOf(day);
    }
}
