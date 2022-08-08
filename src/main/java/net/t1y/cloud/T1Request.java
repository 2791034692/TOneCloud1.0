package net.t1y.cloud;


import static android.content.Context.TELEPHONY_SERVICE;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class T1Request {
    int time = 30;

    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    private JsonParser jsonParser = new JsonParser();


    private Context context;

    public T1Request(Context context){
        this.context = context.getApplicationContext();
    }

    public Gson getGson(){
        return gson;
    }

    public JsonParser getJsonParser() {
        return jsonParser;
    }

    public void setOutTime(int t){
        this.time = t;
    }

    //访问
    protected void post(final String url, final String key, final String data, final String sign,final T1Callback t1Callback){

        new Thread(){
            @Override
            public void run() {
                if(!isNetwork()){
                    if(t1Callback!=null){
                        t1Callback.onFail(T1Cloud.CODE_FAIL_NOT_NETWORK,new T1Exception("无网络"));
                    }
                    return;
                }
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("key",key);
                builder.add("data",data);
                builder.add("signature",sign);
                FormBody formBody = builder.build();
                Request request = new Request.Builder().method("POST",formBody).url(url).build();
                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if(t1Callback!=null){
                            t1Callback.onFail(T1Cloud.CODE_FAIL_IOEXCEPTION,new T1Exception(e.getMessage()));
                        }
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody body = response.body();
                        if(body == null) {
                            if (t1Callback != null) {
                                t1Callback.onFail(T1Cloud.CODE_FAIL_SERVER_ERROR, null);
                            }
                            return;
                        }
                        callback(body.string(),t1Callback);
                    }
                });
            }
        }.start();
    }

    protected abstract void callback(String string,T1Callback callback);


    private boolean isNetwork(){
        //获取网络启
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    //连接服务 CONNECTIVITY_SERVICE
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            //网络信息 NetworkInfo
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo!=null&&activeNetworkInfo.isConnected()){
                //判断是否是wifi
                if (activeNetworkInfo.getType()==(ConnectivityManager.TYPE_WIFI)){
                    //返回无线网络
                    //  Toast.makeText(context, "当前处于无线网络", Toast.LENGTH_SHORT).show();
                    return true;
                    //判断是否移动网络
                }else if (activeNetworkInfo.getType()==(ConnectivityManager.TYPE_MOBILE)){
                    //  Toast.makeText(context, "当前处于移动网络", Toast.LENGTH_SHORT).show();
                    //返回移动网络
                    return true;
                }
            }else {
                //没有网络
                //  Toast.makeText(context, "当前没有网络", Toast.LENGTH_SHORT).show();
                return false;
            }
            //默认返回  没有网络
            return false;
    }
    private T1KEY key;
    public T1Request with(T1KEY t1KEY){
        this.key = t1KEY;
        return this;
    }

    protected T1KEY getKey(){
        return key;
    }
    public static class T1KEY{
        public String PublicKey;
        public String PrivateKey;
    }
    private static final String IV = "t1ivk4o9t1ivk4o9";
    private static final String AES_TYPE = "AES/CBC/PKCS7Padding";
    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;

        try {

            secretBytes = MessageDigest.getInstance("md5").digest(

                    plainText.getBytes());

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("没有这个md5算法！");

        }

        String md5code = new BigInteger(1, secretBytes).toString(16);

        for (int i = 0; i < 32 - md5code.length(); i++) {

            md5code = "0" + md5code;
        }

        return md5code;

    }

    protected static String encode(String AES_KEY,String password){
        try {

            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
            //两个参数，第一个为私钥字节数组， 第二个为加密方式 AES或者DES
            SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance(AES_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            //PKCS5Padding比PKCS7Padding效率高，PKCS7Padding可支持IOS加解密
            //初始化，此方法可以采用三种方式，按加密算法要求来添加。
            // （1）无第三个参数
            // （2）第三个参数为SecureRandom random = new SecureRandom();中random对象，随机数。(AES不可采用这种方法)
            // （3）采用此代码中的IVParameterSpec(ECB不能使用偏移量)
            //加密时使用:ENCRYPT_MODE;  解密时使用:DECRYPT_MODE;

            byte[] encryptedData = cipher.doFinal(password.getBytes());

            return T1Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            //log.error("Exception:{}", e.getMessage());
            return null;
        }
    }

   public int getTime(){
       return (int) (System.currentTimeMillis() / 1000);
   }


    protected static String decode(String AES_KEY,String encodePassword){
        try {
            byte[] decodeByte = T1Base64.getDecoder().decode(encodePassword);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());
            SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance(AES_TYPE);

            //解密模式
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] bytes = cipher.doFinal(decodeByte);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            //log.error("Exception{}", e.getMessage());
            return null;
        }
    }
}
