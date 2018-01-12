package org.chinalbs.utils;

import java.util.ArrayList;
import java.util.List;

/*
Create by jiangyun on 2017/12/25
*/
public class RowKeyUtil {

    public static String[] spiltRowKey(String rowKey){

        String time = rowKey.substring(0,13);
        String uid = rowKey.substring(13, rowKey.length());
        return new String[]{time,uid};
    }


}
