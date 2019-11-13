package com.moyulab.haige.haigelab.common;

public class HaigeLabException extends RuntimeException{

    public HaigeLabException(String message) {
        super(message);
    }

    public HaigeLabException(String message, Exception e) {
        super(message, e);
    }

    public HaigeLabException() {
        super();
    }

}
