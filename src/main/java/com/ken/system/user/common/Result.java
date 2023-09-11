package com.ken.system.user.common;

import java.io.Serializable;

public class Result<T>  implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private boolean success;
    private T data;
    private String msg;

    private Result(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.success = 200 == code;
    }

    public static <T> Result<T> data(T data) {
        return data(data, "操作成功");
    }

    public static <T> Result<T> data(T data, String msg) {
        return new Result<>(200, data, msg);
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>(200, null, msg);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>(400, null, msg);
    }

    public static <T> Result<T> status(boolean flag) {
        return flag ? success("操作成功") : fail("操作失败");
    }

    public int getCode() {
        return this.code;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public T getData() {
        return this.data;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

}
