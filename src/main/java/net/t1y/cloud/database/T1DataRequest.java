package net.t1y.cloud.database;
import android.content.Context;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.t1y.cloud.T1Callback;
import net.t1y.cloud.T1Cloud;
import net.t1y.cloud.T1Exception;
import net.t1y.cloud.T1Request;
import net.t1y.cloud.database.data.Callback;
import net.t1y.cloud.database.data.Delete;
import net.t1y.cloud.database.data.Insert;
import net.t1y.cloud.database.data.Update;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class T1DataRequest extends T1Request {
    public static final int REQUEST_DATA = 1;
    private static final String select = "http://api.t1y.net/api/v3/select";
    private static final String update = "http://api.t1y.net/api/v3/update";
    private static final String delete = "http://api.t1y.net/api/v3/delete";
    private static final String insert = "http://api.t1y.net/api/v3/insert";
    public T1DataRequest(Context context) {
        super(context);
    }
    @Override
    protected void callback(String string,T1Callback t1Callback) {
        String data = decode(getKey().PrivateKey,string);
        Callback callback = getGson().fromJson(data,Callback.class);
        if(callback == null){
            t1Callback.onFail(T1Cloud.CODE_FAIL_PUBLIC_KEY_ERROR,new T1Exception("Public_KEY或API_KEY错误"));
            return;
        }
        if(callback.getCode() == 1&&callback.getMsg().contains("查询成功")){
            JsonArray object = getJsonParser().parse(data).getAsJsonObject().get("data").getAsJsonObject().get("data").getAsJsonArray();
            if(object.size()==0){
                t1Callback.onSuccess(T1Cloud.CODE_SUCCESS_YES_CALLBACK,null);
                return;
            }else{
                if(object.size() == 1){
                    String json = object.get(0).toString();
                    t1Callback.onSuccess(T1Cloud.CODE_SUCCESS_YES_CALLBACK,getGson().fromJson(json,t1Select.getE()));
                }else{
                    String json = object.toString();
                    Object objects =  getGson().fromJson(json,t1Select.getEe());
                    //System.out.println(objects);
                    t1Callback.onSuccess(T1Cloud.CODE_SUCCESS_YES_CALLBACK,objects);
                }
            }
            t1Select = null;
            callback.setData("null");
            callback = null;
           //t1Callback.onSuccess(T1Cloud.CODE_SUCCESS_YES_CALLBACK,null);
        }else if(callback.getCode() == 1){
            t1Callback.onSuccess(T1Cloud.CODE_SUCCESS_YES_CALLBACK,callback.getMsg());
        }else{
            if(callback.getMsg().contains("签名")){
                t1Callback.onFail(T1Cloud.CODE_FAIL_SIGNATURE_ERROR,new T1Exception(callback.getMsg()));
            }else if(callback.getMsg().contains("钥")||callback.getMsg().contains("Key")){
                t1Callback.onFail(T1Cloud.CODE_FAIL_PUBLIC_KEY_ERROR,new T1Exception("Public_KEY或API_KEY错误"));
            }else{
                t1Callback.onFail(T1Cloud.CODE_FAIL_CONTENT_ERROR,new T1Exception("内容错误"));
            }
        }
        System.gc();
    }

    private T1Select t1Select = null;
    void select(T1Select select,String json,T1Callback callback){
        this.t1Select = select;
        String en_form = encode(getKey().PrivateKey,json);
        post(T1DataRequest.select,getKey().PublicKey,en_form,stringToMD5(getKey().PublicKey+en_form+getKey().PrivateKey),callback);
    }

    void update(T1Bean bean, String json, T1Callback<String> callback){
       JsonObject object =  getJsonParser().parse(json).getAsJsonObject();
       int id = object.get("id").getAsInt();
       Update update = new Update();
       update.setWhere("id='"+id+"'");
       String string = "";
       update.setTimestamp(getTime());
       for(Map.Entry<String, JsonElement> entry:object.entrySet()){
           String value = entry.getValue().toString();
           if(value.contains("\"")){
               value = value.substring(1,value.length()-1).trim();
           }
           string += "`"+entry.getKey()+"`='"+value+"',";
       }
       String value = string.substring(0,string.length()-1);
       update.setSet(value);
       update.setTable(bean.getClassName());
       String form = getGson().toJson(update);
       String en_form = encode(getKey().PrivateKey,form);
       post(T1DataRequest.update,getKey().PublicKey,en_form,stringToMD5(getKey().PublicKey+en_form+getKey().PrivateKey),callback);
    }
    void insert(T1Bean bean,String json,T1Callback<String> t1Callback) {
        //System.out.println(json);
        JsonObject object = getJsonParser().parse(json).getAsJsonObject();
        Insert insert = new Insert();
        insert.setTable(bean.getClassName());
        String column = "";
        String values = "(";
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            if(Objects.equals(entry.getKey(), "id")){
                continue;
            }
            String value = entry.getValue().toString();
            if (value.contains("\"")) {
                value = value.substring(1, value.length() - 1).trim();
            }
            column += "`" + entry.getKey() + "`,";
            values += "'" + value + "',";
        }
        column = column.substring(0, column.length() - 1);
        values = values.substring(0, values.length() - 1)+")";
        insert.setColumn(column);
        insert.setValue(values);
        insert.setTimestamp(getTime());
        String form = getGson().toJson(insert);
        String en_form = encode(getKey().PrivateKey,form);
        post(T1DataRequest.insert,getKey().PublicKey,en_form,stringToMD5(getKey().PublicKey+en_form+getKey().PrivateKey),t1Callback);
    }
    void delete(T1Bean bean,String json,T1Callback t1Callback){
        Delete delete = new Delete();
        delete.setTable(bean.getClassName());
        int id = bean.getId();
        delete.setTimestamp(getTime());
        delete.setWhere("id = '"+id+"'");
        String form = getGson().toJson(delete);
        String en_form = encode(getKey().PrivateKey,form);
        post(T1DataRequest.delete,getKey().PublicKey,en_form,stringToMD5(getKey().PublicKey+en_form+getKey().PrivateKey),t1Callback);
    }
}
