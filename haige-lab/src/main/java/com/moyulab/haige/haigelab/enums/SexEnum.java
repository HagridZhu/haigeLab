package com.moyulab.haige.haigelab.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

import java.io.Serializable;

public enum  SexEnum implements IEnum {

    MALE("1","男"),FEMALE("2","女");
    private final String value;
    private final String desc;
    SexEnum(final String value, final String desc){
        this.value = value;
        this.desc = desc;
    }
    @Override
    public Serializable getValue() {
        return null;
    }
    public String getDesc() {
        return desc;
    }


}
