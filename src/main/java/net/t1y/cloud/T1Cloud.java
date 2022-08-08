package net.t1y.cloud;
import android.util.ArrayMap;
public class T1Cloud {
    public static final int  CODE_FAIL_NOT_NETWORK = 0;
    public static final int  CODE_FAIL_SERVER_ERROR = -1;
    public static final int  CODE_FAIL_SIGNATURE_ERROR = -2;
    public static final int  CODE_FAIL_PUBLIC_KEY_ERROR = -3;
    public static final int  CODE_FAIL_OVERRUN__THE_TIME = -4;
    public static final int  CODE_FAIL_CONTENT_ERROR = -5;
    @Deprecated
    public static final int  CODE_FAIL_NO_BODY = -7;
    public static final int  CODE_FAIL_IOEXCEPTION = -6;
    public static final int  CODE_SUCCESS_YES_CALLBACK = 1;

    private static ArrayMap<Integer,T1Request> arrayMap = new ArrayMap<>();
    public static void register(int t,T1Request request){
        arrayMap.put(t,request);
    }
    public static <T extends T1Request> T get(int t){
        return (T) arrayMap.get(t);
    }
    public static void logout(){
        arrayMap.clear();
        arrayMap = null;
    }
}
