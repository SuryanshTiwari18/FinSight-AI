package com.vedansh.smssync.util;

public class DateUtil {

    private static final long TEN_DAYS_IN_MILLIS =
            10L * 24 * 60 * 60 * 1000;

    public static boolean isWithinLastTenDays(long smsTimestamp) {

        long currentTime = System.currentTimeMillis();

        return (currentTime - smsTimestamp) <= TEN_DAYS_IN_MILLIS;
    }
}