package com.example.greenfarm.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    private final static String REGEX_IP = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

    private final static String REGEX_PHONE_NUMBER = "^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";

    /**
     * 判断IP格式
     * @param express
     * @return
     */
    public static boolean checkIP(String express) {
        Pattern pattern = Pattern.compile(REGEX_IP);
        Matcher matcher = pattern.matcher(express);
        return matcher.matches();
    }

    public static boolean checkPhoneNumber(String express) {
        Pattern pattern = Pattern.compile(REGEX_PHONE_NUMBER);
        Matcher matcher = pattern.matcher(express);
        return matcher.matches();
    }


}
