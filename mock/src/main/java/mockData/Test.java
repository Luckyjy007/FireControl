package mockData;

import Bean.Alarm;

public class Test {
    public static void main(String[] args) {
        GenerateData generateData = new GenerateData();
        Alarm alarm = generateData.GenerateAlarmData();
        System.out.println(alarm.toString());
    }
}
