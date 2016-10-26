package me.rokevin.share.util;

import android.util.Log;

/**
 * Created by luokaiwen on 15/4/28.
 * <p/>
 * 日志工具类
 */
public class LogUtil {

    private static final boolean LOGABLE = true;

    public static void e(Class clazz, String msg) {

        if (LOGABLE) {
            Log.e(clazz.getSimpleName(), msg);
        }
    }

    public static void e(String tag, String msg) {

        if (LOGABLE) {
            Log.e(tag, msg);
        }
    }
}
