package cn.com.my.demo;

import android.app.Application;
import android.util.Log;

import com.hdl.elog.ELog;
import com.http.tools.utils.HUtils;

import cn.com.my.demo.net.RetrofitHelper;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //release版本不打印日志
        ELog.setIsDebug(HUtils.isDebug(this));
      //  RetrofitHelper.getInstance().Init(this,"http://wthrcdn.etouch.cn/");//"http://192.168.10.111:8080/");
        RetrofitHelper.getInstance().Init(this,"http://10.0.0.174:8080/MyService/");

        Log.d("MyApplication","MyApplication.onCreate()");

    }
}
