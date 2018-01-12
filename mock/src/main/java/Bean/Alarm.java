package Bean;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class Alarm implements Serializable {

   public int primaryKey;//主键
    public  String equipmentId;//设备编号
    public  String warehousingTime;//入库时间
    public  String alarmType;//报警类型
    public Long alarmTime;//报警时间
    public int artificialState;//人工状态
    public int systemStatus;//系统状态
    public int webReset;//Web复位
    public int state;//状态
    public  String extra;//其他



    public Alarm(int primaryKey, String equipmentId, String warehousingTime, String alarmType, Long alarmTime, int artificialState, int systemStatus, int webReset, int state, String extra) {
        this.primaryKey = primaryKey;
        this.equipmentId = equipmentId;
        this.warehousingTime = warehousingTime;
        this.alarmType = alarmType;
        this.alarmTime = alarmTime;
        this.artificialState = artificialState;
        this.systemStatus = systemStatus;
        this.webReset = webReset;
        this.state = state;
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "primaryKey=" + primaryKey +
                ", equipmentId='" + equipmentId + '\'' +
                ", warehousingTime='" + warehousingTime + '\'' +
                ", alarmType='" + alarmType + '\'' +
                ", alarmTime=" + alarmTime +
                ", artificialState=" + artificialState +
                ", systemStatus=" + systemStatus +
                ", webReset=" + webReset +
                ", state=" + state +
                ", extra='" + extra + '\'' +
                '}';
    }

    public String toJsonString(){
        return JSONObject.toJSONString(this);
    }
}
