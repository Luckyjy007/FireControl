package org.chinalbs.tablemodel;


import org.chinalbs.mongodb.MongoBean;

import java.io.Serializable;

/*
Create by jiangyun on 2017/12/20
*/
public class Alarm extends MongoBean implements Serializable  {
    private static final long serialVersionUID = 330410716100931438L;
    public Long _id;         // 主键
    public String equipmentId;        // 设备编号
    public String warehousingTime;    // 入库时间
    public String alarmType;          // 报警类型
    public Long alarmTime;          // 报警时间
    public int artificialState;    // 人工状态
    public int systemStatus;       // 系统状态
    public int webReset;           // Web复位
    public int state;              // 状态
    public String extra;              // 其他

    public Alarm() {
    }

    public Alarm(Long primaryKey, String equipmentId, String warehousingTime, String alarmType, Long alarmTime,
                 int artificialState, int systemStatus, int webReset, int state, String extra) {
        this._id = primaryKey;
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
                "primaryKey=" + _id +
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


}


