package net.t1y.cloud.database;

import net.t1y.cloud.T1Callback;
import net.t1y.cloud.T1Cloud;
import net.t1y.cloud.T1Exception;

import java.nio.charset.StandardCharsets;

public abstract class T1Bean {
    private int id = 0;
    private String PRIMARY_KEY = null;
    public final int getId() {
        return id;
    }
    public final void setId(int id) {
        if(this.id!=0){
            throw new T1Exception("错误，id只能初始化时赋值");
        }
        this.id = id;
    }
    @Deprecated
    public final void withKey(String s){
        if(s == null||s.trim().length()==0){
            throw new T1Exception("关键字段不能为空");
        }
        this.PRIMARY_KEY = s;
    }
    final String getPRIMARY_KEY(){
        return this.PRIMARY_KEY;
    }
    public final void delete(T1Callback<String> t1Callback){
        T1DataRequest dataRequest = T1Cloud.get(T1DataRequest.REQUEST_DATA);
        if(dataRequest == null){
            t1Callback.onFail(T1Cloud.CODE_FAIL_IOEXCEPTION,new T1Exception("请先初始化数据库请求类"));
            return;
        }
        String json =  dataRequest.getGson().toJson(this);
        dataRequest.delete(this,json,t1Callback);
    }

    public final void update(T1Callback<String> t1Callback){
        T1DataRequest dataRequest = T1Cloud.get(T1DataRequest.REQUEST_DATA);
        if(dataRequest == null){
            t1Callback.onFail(T1Cloud.CODE_FAIL_IOEXCEPTION,new T1Exception("请先初始化数据库请求类"));
            return;
        }
        String json =  dataRequest.getGson().toJson(this);
        dataRequest.update(this,json,t1Callback);
        //t1Callback.onSuccess(0,json);
    }
    public final void insert(T1Callback<String> t1Callback){
        T1DataRequest dataRequest = T1Cloud.get(T1DataRequest.REQUEST_DATA);
        if(dataRequest == null){
            t1Callback.onFail(T1Cloud.CODE_FAIL_IOEXCEPTION,new T1Exception("请先初始化数据库请求类"));
            return;
        }
        String json = dataRequest.getGson().toJson(this);
        dataRequest.insert(this,json,t1Callback);
    }
    public final String getClassName(){
        return getClass().getSimpleName();
    }

}
