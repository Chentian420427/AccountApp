package com.example.accountapp;

import android.util.Log;

import java.io.Serializable;
import java.util.UUID;

public class RecordBean implements Serializable {

    //支出 收入
    public enum RecordType{
        RECORD_TYPE_EXPENSE,RECORD_TYPE_INCOME
    }

    //花费的金额

    private double amount;
    //消费类别
    private RecordType type;
    //类别
    private String catecgory;
    //备注
    private String remark;
    //日期
    private String date;
    //时间撮
    private long timeStamp;
    //唯一识别码
    private String uuid;


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getType() {

        if (this.type == RecordType.RECORD_TYPE_EXPENSE){
            return 1;
        }else{
            return 2;
        }

    }

    public void setType(int type) {

        if (type == 1){
            this.type = RecordType.RECORD_TYPE_EXPENSE;
        }else{
            this.type = RecordType.RECORD_TYPE_INCOME;
        }


    }

    public String getCatecgory() {
        return catecgory;
    }

    public void setCatecgory(String catecgory) {
        this.catecgory = catecgory;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }



    public RecordBean(){
        uuid = UUID.randomUUID().toString();
        timeStamp = System.currentTimeMillis();
        date = DateUtil.getFormattedDate();
        Log.d("RecordBean",date);
    }
}
