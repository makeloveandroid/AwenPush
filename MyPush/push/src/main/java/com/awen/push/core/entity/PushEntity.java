package com.awen.push.core.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/***
 * 推送消息内容
 */
@Entity
public final class PushEntity {
    public static int PULSE=-1,NOTIFICATION=1,TOAST=2, IMAGE = 3;


    /***
     * 推送类型
     */
    @Property(nameInDb = "push_type")
    public int type;



    /**
     * 推送消息内容
     * Base64编码的数据
     */
    @Property(nameInDb = "push_base64_data")
    public String baseData;


    /***
     * 推送消息唯一ID
     */
    @Index(unique = true)
    public int id;


    @Generated(hash = 100227260)
    public PushEntity(int type, String baseData, int id) {
        this.type = type;
        this.baseData = baseData;
        this.id = id;
    }


    @Generated(hash = 732106406)
    public PushEntity() {
    }


    public int getType() {
        return this.type;
    }


    public void setType(int type) {
        this.type = type;
    }


    public String getBaseData() {
        return this.baseData;
    }


    public void setBaseData(String baseData) {
        this.baseData = baseData;
    }


    public int getId() {
        return this.id;
    }


    public void setId(int id) {
        this.id = id;
    }



}
