package com.jie1234.jieapiinterface.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回结果
 */
@Data
public class R implements Serializable {
    private Integer code;
    private String msg;
    private Object data;

    public R() {
    }

    public R(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public R(Object data) {
        this.code = 200;
        this.msg = "success";
        this.data = data;
    }

    public R(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
