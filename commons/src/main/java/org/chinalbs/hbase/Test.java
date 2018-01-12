package org.chinalbs.hbase;

import org.apache.hadoop.hbase.TableName;

/*
Create by jiangyun on 2017/12/25
*/
public class Test {
    //151427277013113211085
    public static void main(String[] args) {

        HBaseUtil.snapshot("snapshotTest01", TableName.valueOf("17176523678912"));
        System.out.println("successful");


    }
}