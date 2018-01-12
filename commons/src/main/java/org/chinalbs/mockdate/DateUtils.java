package org.chinalbs.mockdate;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static jdk.nashorn.internal.objects.Global.println;

public class DateUtils {

    //在给定范围随机获取时间
    /**
     * @author jiangyun
     * @date jiangyun 14:09
      * @param start 起始时间
     * @param end 结束时间
     * @param symbol 间隔符号
     * @return
    */
    public static String getTimeStamp(String start, String end, char symbol) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        if (null == start || start.trim().length() != 10 || null == end || end.trim().length() != 10) {
            throw new IllegalArgumentException("the data you put is illegal");
        }
        if (getStrNumCount(start) != 8 || getStrNumCount(end) != 8 || getSymoblCount(start, symbol) != 2 || getSymoblCount(end, symbol) != 2) {
            println(getStrNumCount(start));
            println(getStrNumCount(end));
            throw new IllegalArgumentException("the data you put is illegal");
        }
        if (DateTime.parse(start, dateTimeFormatter).getMillis() >= DateTime.parse(end, dateTimeFormatter).getMillis()) {
            throw new IllegalArgumentException("the data you put is illegal start time should smaller than the end time");
        }
        long l = DateTime.parse(end, dateTimeFormatter).getMillis() - DateTime.parse(start, dateTimeFormatter).getMillis();
        Long randomTime = Math.round(Math.random() * l) + DateTime.parse(start, dateTimeFormatter).getMillis();
        //String date = new DateTime(randomTime).toString("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime = new DateTime(randomTime);
        return dateTime.toString("yyyy-MM-dd :HH:mm:ss");
    }
//统计字符串数字位个数
    /**
     * @author jiangyun
     * @date jiangyun 14:13
     * @param str
     * @return
    */
    private static int getStrNumCount(String str) {
        int i = 0;
        char[] chars = str.trim().toCharArray();
        for (char c : chars) {
            if (c >= 48 && c <= 57) {
                i++;

            }

        }
        return i;
    }
//统计字符串中字符c的个数
    /**
     * @author jiangyun
     * @date 14:14
     * @params  * @param str
     * @param c
     * @return
    */
    private static int getSymoblCount(String str, char c) {

        int i = 0;

        char[] chars = str.trim().toCharArray();
        for (char t : chars) {
            if (c == t) {
                i++;
            }
        }
        return i;
    }
//把字符串变成指定长度
    public static String getFormatLength(int num, int length) {
        String s = String.valueOf(num);
        int preLengh = s.length();
        int diferent = length - preLengh;
        if (length <= 0) {
            return null;
        } else if (length <= preLengh) {
            return String.valueOf(num);
        } else {
            String str = "";
            for (int i = 0; i < diferent; i++) {
                str += "0";
            }

            return str + num;
        }

    }


}
