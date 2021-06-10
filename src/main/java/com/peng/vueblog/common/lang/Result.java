package com.peng.vueblog.common.lang;

import lombok.Data;

import java.io.Serializable;

// 统一回响应结果封装
@Data
public class Result implements Serializable {

    private int code; // 200是正常，非200是错误
    private String msg;
    private Object data;

    public static Result succ(Object data){
        return succ(200,"操作成功",data);
    }

    public static Result succ(int code,String msg, Object data){
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        System.out.println("Result："+code+"（"+msg+"）");
        return r;
    }

    public static Result fail(String msg){
        return succ(-1,msg,null);
    }

    public static Result fail(int code, String msg) {
        return succ(code,msg,null);
    }

    public static Result fail(int code, String msg, Object data){
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

}
