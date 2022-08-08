package net.t1y.cloud;

public interface T1Callback<T> {
    void onFail(int code,T1Exception e);
    void onSuccess(int code,T obj);
}
