package net.t1y.cloud.database;

import android.view.View;

import com.google.gson.reflect.TypeToken;

import net.t1y.cloud.T1Callback;
import net.t1y.cloud.T1Cloud;
import net.t1y.cloud.T1Exception;
import net.t1y.cloud.database.data.Select;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class T1Select<T> {
    private String column = "*";
    private String where;
    private String sort = "desc";
    private String sort_column = "id";
    private int timestamp;
    private Class<?extends T> e;
    private Type ee;
    public T1Select(Class<?extends T> beanClass){
        this.e = beanClass;;
        ee = new TypeToken<List<T>>(){}.getType();
        //Class<? extends Class[]> stringClass = String.class.getClasses().getClass();
    }

    Class<? extends T> getE() {
        return e;
    }

    Type getEe() {
        return ee;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public void setSort_column(String sort_column) {
        this.sort_column = sort_column;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
    public void setColumn(String column) {
        this.column = column;
    }
    public String getColumn() {
        return column;
    }
    public String getWhere() {
        return where;
    }
    public void getObject(T1Callback<T> t1Callback) {
        T1DataRequest dataRequest = T1Cloud.get(T1DataRequest.REQUEST_DATA);
        if(dataRequest == null){
            t1Callback.onFail(T1Cloud.CODE_FAIL_IOEXCEPTION,new T1Exception("请先初始化数据库请求类"));
            return;
        }
        timestamp = dataRequest.getTime();
        Select select = new Select();
        select.setColumn(column);
        select.setSort(sort);
        select.setSort_column(sort_column);
        select.setWhere(where);
        select.setTable(e.getSimpleName());
        select.setTimestamp(timestamp);
        String json = dataRequest.getGson().toJson(select);
        dataRequest.select(this,json,t1Callback);
    }
    public void exists(final OnSelectExistsListener onSelectExistsListener){
        T1Callback<T> t1Callback = new T1Callback<T>() {
            @Override
            public void onFail(int code, T1Exception e) {
                onSelectExistsListener.done(true);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(int code, T obj) {
                if(obj == null){
                    onSelectExistsListener.done(false);
                }else{
                    onSelectExistsListener.done(true);
                }
            }
        };
        T1DataRequest dataRequest = T1Cloud.get(T1DataRequest.REQUEST_DATA);
        if(dataRequest == null){
            t1Callback.onFail(T1Cloud.CODE_FAIL_IOEXCEPTION,new T1Exception("请先初始化数据库请求类"));
            return;
        }
        timestamp = dataRequest.getTime();
        Select select = new Select();
        select.setColumn(column);
        select.setSort(sort);
        select.setSort_column(sort_column);
        select.setWhere(where);
        select.setTable(e.getSimpleName());
        select.setTimestamp(timestamp);
        String json = dataRequest.getGson().toJson(select);
        dataRequest.select(this,json,t1Callback);
    }
    public interface OnSelectExistsListener{
        void done(boolean b);
    }
    public void getObjects(T1Callback<ArrayList<T>> t1Callback){
        T1DataRequest dataRequest = T1Cloud.get(T1DataRequest.REQUEST_DATA);
        if(dataRequest == null){
            t1Callback.onFail(T1Cloud.CODE_FAIL_IOEXCEPTION,new T1Exception("请先初始化数据库请求类"));
            return;
        }
        timestamp = dataRequest.getTime();
        Select select = new Select();
        select.setColumn(column);
        select.setSort(sort);
        select.setSort_column(sort_column);
        select.setWhere(where);
        select.setTable(e.getSimpleName());
        select.setTimestamp(timestamp);
        String json = dataRequest.getGson().toJson(select);
        dataRequest.select(this,json,t1Callback);
    }
}
