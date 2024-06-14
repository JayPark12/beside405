package com.beside.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

public class CommonUtil {

    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
    }

    public static String getMsgId() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String formattedNumber = String.format("%06d", randomNumber);
        return getCurrentTime() + formattedNumber;
    }

    //String 형식의 날짜와 시간을 LocalDateTime 형식으로 변환
    public static LocalDateTime getDateTime(String inputDate) {
        int year = Integer.parseInt(inputDate.substring(0, 4));
        int month = Integer.parseInt(inputDate.substring(4, 6));
        int day = Integer.parseInt(inputDate.substring(6, 8));
        int hour = Integer.parseInt(inputDate.substring(8, 10));
        int minute = Integer.parseInt(inputDate.substring(10, 12));
        return LocalDateTime.of(year, month, day, hour, minute);
    }

}
