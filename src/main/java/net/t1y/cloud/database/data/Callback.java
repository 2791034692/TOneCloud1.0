package net.t1y.cloud.database.data;

import android.view.ViewTreeObserver;

public class Callback<T> {
    private String msg;
    private int code;
    private Object data;

    public String getMsg() {
        return msg;
    }

    public void setData(Object data) {
        //this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }
}
