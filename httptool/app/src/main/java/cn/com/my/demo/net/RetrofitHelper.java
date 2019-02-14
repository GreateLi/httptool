package cn.com.my.demo.net;

import android.content.Context;
import android.util.Log;
//import com.hdl.elog.ELog;
import com.client.data.protobuf.Persons;
import com.http.tools.net.common.IdeaApi;
import com.http.tools.utils.Utils;


public class RetrofitHelper {

    private volatile static ApiService mApiService;
    private volatile static RetrofitHelper mRetrofitHelper= new RetrofitHelper();
    private static String HOST = "http://wthrcdn.etouch.cn/";


    private  Persons.Person mResponse;
    //private final static ClientDataLoad mClientDataLoad = new ClientDataLoad();

    public static RetrofitHelper getInstance()
    {
        return mRetrofitHelper;
    }

    public  void Init(Context context,String host )
    {

        if(!host.isEmpty())
            HOST = host;
        Utils.init(context);

        ///set  mClientDataLoa
    }


    private static String getServerURL()
    {
       return HOST  ;
    }

    public static  ApiService getApiService() {
        if (mApiService == null)
            mApiService = IdeaApi.getApiService(ApiService.class, getServerURL());
        return mApiService;
    }


}
