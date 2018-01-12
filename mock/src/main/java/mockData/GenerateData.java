package mockData;

import Bean.Alarm;

import java.util.Random;
import java.util.UUID;

public class GenerateData {
   private Random random = new Random();
    private String alarmTypes[] = {"区域报警系统", "集中报警系统", "控制中心报警系统", "烟感报警", "人工报警", "红外线探测", "紫外线探测", "光电探测", "气体探测", "离子探测", "声音探测"};


    public Alarm GenerateAlarmData() {

        int primaryKey = random.nextInt(10000000) + 100000 + random.nextInt(1000);
        String equipmentId = UUID.randomUUID().toString();
        String warehousingTime = DateUtils.getTimeStamp("2014-01-01", "2016-12-12", '-');
        String alarmType = alarmTypes[random.nextInt(10)];
        Long alarmTime = System.currentTimeMillis() + random.nextInt(100000000);
        int artificialState = random.nextInt(3);
        int systemStatus = random.nextInt(10);
        int webReset = random.nextInt(10);
        int state = random.nextInt(10);
        String extra = "extra";

        return new Alarm(primaryKey, equipmentId, warehousingTime, alarmType, alarmTime, artificialState, systemStatus, webReset, state, extra);
    }

}
