package com.beside.util;

import java.text.SimpleDateFormat;
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


}
