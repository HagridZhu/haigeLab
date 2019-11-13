package com.moyulab.haige.haigelab.common;

import lombok.Data;
import lombok.Getter;

@Data
public class Result<T> {

    private String msg;
    private Integer code;
    private T data;

    public Result(String msg, Integer code, T data){
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public static <T> Result<T> success(T data){
        return new Result(null, 200, data);
    }

    public static <T> Result<T> error(String msg){
        return new Result(msg, 500, null);
    }



}
