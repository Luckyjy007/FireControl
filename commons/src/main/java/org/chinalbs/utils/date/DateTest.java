package org.chinalbs.utils.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
Create by jiangyun on 2017/12/28
*/
public class DateTest {
    public static void main(String[] args) {
        String d = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        String string = new Date().toString();
        Date date = DateUtil.addMonth(new Date(), 2);
        System.out.println(DateUtil.getDay(date.toString()));
        System.out.println(date);
    }
}
