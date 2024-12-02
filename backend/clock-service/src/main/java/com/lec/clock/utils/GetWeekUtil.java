package com.lec.clock.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * 查看今天是星期几
 */
public class GetWeekUtil {
    public static String GetWeekUtil(Date date) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}
