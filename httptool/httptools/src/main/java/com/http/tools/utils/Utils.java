package com.http.tools.utils;

import android.content.Context;

public class Utils {

    private static Context context;
    private static String strUniqueID="";

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    /**
     * 设置 设备ID 需要动态权限
     * @return
     */
    public   static String  getDeviceID()
    {
        if(strUniqueID.isEmpty())
        {
            strUniqueID =  (new AppDeviceUtils(context)).getUniqueID();
        }

        return strUniqueID;
    }
}